%% @author willy
%% @doc The ring module creates a ring and call a method of another module for each node in it.


-module(ring).

%% ====================================================================
%% API functions
%% ====================================================================
-export([launch/3, init/3, test/0]).


%% Initializes a node getting its successor.
%% @param Mod The module to call when the ring is finished
%% @param Fun The function to call when the ring is finished
%% @param Name The name of the node
init(Mod, Fun, Name) ->
	receive
		{next, Next} -> hello(Mod, Fun, Next, Name)
	end.


%% Creates a static ring from a list of names.
%% @param Mod The module to call when the ring is finished
%% @param Fun The function to call when the ring is finished
%% @param Names The list of names for the nodes of the ring
launch(Mod, Fun, Names) -> build(Mod, Fun, Names).


test() -> launch(chord, chord, ["alice", "dave", "eve", "bob", "charlie"]).

%% ====================================================================
%% Internal functions
%% ====================================================================


%% Creates the last node and sets its next to the first node of the ring.
%% @param Mod The module to call when the ring is finished
%% @param Fun The function to call when the ring is finished
%% @param H The Name of the new node
%% @param First The Pid of the first node of the ring
%% @return The Pid of the new node
build_aux(Mod, Fun, [H], First) ->
	Pid = spawn(?MODULE, init, [Mod, Fun, H]),
	Pid ! {next, First},
	%io:format("build_auxF end(~w, ~w) Pid: ~w~n", [H, TeteDeListe, Pid]),
	Pid;
%% Creates a node and asks to create the next.
%% @param Mod The module to call when the ring is finished
%% @param Fun The function to call when the ring is finished
%% @param H The Name of the new node
%% @param T The Names of the following nodes
%% @param First The Pid of the first node of the ring
%% @return The Pid of the new node
build_aux(Mod, Fun, [H|T], First) ->
	Pid = spawn(?MODULE, init, [Mod, Fun, H]),
	Pid ! {next, build_aux(Mod, Fun, T, First)},
	Pid.

%% Creates the first node and asks to create the next.
%% @param Mod The module to call when the ring is finished
%% @param Fun The function to call when the ring is finished
%% @param H The Name of the new node
%% @param T The Names of the following nodes
build(Mod, Fun, [H|T]) ->
	Pid = spawn(?MODULE, init, [Mod, Fun, H]),
	Pid ! {next, build_aux(Mod, Fun, T, Pid)},
	ok.

hello(Mod, Fun, Pid, Name) ->
	io:format("~w:~w # ~w # ~w -> ~w)~n", [Mod, Fun, Name, self(), Pid]).




