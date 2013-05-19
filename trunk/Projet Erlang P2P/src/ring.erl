%% @author willy
%% @doc The ring module creates a ring and call a method of another module for each node in it.


-module(ring).

%% ====================================================================
%% API functions
%% ====================================================================
-export([launch/3, init/3, test/0]).

%% To save some time typing the parameters...
test() -> launch(chord, chord, [alice, dave, eve, bob, charlie]).

%% Initializes a node getting its successor.
%% @param Mod The module to call when the ring is finished
%% @param Fun The function to call when the ring is finished
%% @param NodeId The structure {id, {name, node}} of the node
init(Mod, Fun, NodeId) ->
	{_, {Name, _}} = NodeId,
	register(Name, self()),
	HashTable = dict:new(),
	receive
		{next, NextId} ->
			Mod:Fun(NodeId, HashTable, NextId);
		_ -> io:format("ERROR IN INIT ~w~n", [Name])
	end.


%% Creates a static ring from a list of names.
%% @param Mod The module to call when the ring is finished
%% @param Fun The function to call when the ring is finished
%% @param Names The list of names for the nodes of the ring
launch(Mod, Fun, Names) ->
	Hashed = lists:map(fun(T) ->
							   Couple = {T, node()},
							   {crypto:sha(term_to_binary(Couple)), Couple}
					   end, Names),
	Sorted = lists:sort(Hashed),
	build(Mod, Fun, Sorted).


%% ====================================================================
%% Internal functions
%% ====================================================================


%% Creates the last node and sets its next to the first node of the ring.
%% @param Mod The module to call when the ring is finished
%% @param Fun The function to call when the ring is finished
%% @param H The structure {id, {name, node}} of the new node
%% @param First The Pid of the first node of the ring
%% @return The Pid of the new node
build_aux(Mod, Fun, [H], First) ->
	Pid = spawn(?MODULE, init, [Mod, Fun, H]),
	Pid ! {next, First},
	H;
%% Creates a node and asks to create the next.
%% @param Mod The module to call when the ring is finished
%% @param Fun The function to call when the ring is finished
%% @param H The structure {id, {name, node}} of the new node
%% @param T The Names of the following nodes
%% @param First The Pid of the first node of the ring
%% @return The Pid of the new node
build_aux(Mod, Fun, [H|T], First) ->
	Pid = spawn(?MODULE, init, [Mod, Fun, H]),
	Pid ! {next, build_aux(Mod, Fun, T, First)},
	H.

%% Creates the first node and asks to create the next.
%% @param Mod The module to call when the ring is finished
%% @param Fun The function to call when the ring is finished
%% @param H The structure {id, {name, node}} of the new node
%% @param T The Names of the following nodes
build(Mod, Fun, [H|T]) ->
	Pid = spawn(?MODULE, init, [Mod, Fun, H]),
	Pid ! {next, build_aux(Mod, Fun, T, H)},
	ok.



