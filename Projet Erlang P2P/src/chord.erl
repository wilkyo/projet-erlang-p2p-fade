%% @author vincent
%% @doc @todo Add description to chord.


-module(chord).

%% ====================================================================
%% API functions
%% ====================================================================
-export([chord/3]).

chord(NodeId, HashTable, NextId) ->
	io:format("IN CHORD: ~w -> ~w~n", [target(NodeId), target(NextId)]),
	receive
		{lookup, Key} -> io:format("RECEIVED: ~w -> ~w~n", [target(NodeId), Key]);
		exit -> io:format("EXITING: ~w~n", [target(NodeId)]), exit(target(NodeId));
		_ -> ok
	end,
	chord(NodeId, HashTable, NextId).
  


%% ====================================================================
%% Internal functions
%% ====================================================================

%% Gets the Hashed id from our node.
%% @param Id The Hashed {name, node} of the node
%% @return The Id
id({Id, _}) -> Id.

%% Gets the target of the hashed id.<br />
%% Can be used to send a message to it.
%% @param Target The tuple {name, node} that has been hashed to Id
%% @return The target
target({_, Target}) -> Target.

%Node
%node(Id,Succ,Table)-> ok.

% Lookup/1
%lookup(HashedKey) -> ok.

% get/1
%gets(HashedKey) -> 
	%identifiant du succ qui est responsable du Hashedkey
%	Who=lookup(Hashedkey),
	
	%recupère le hashtable
	%node(Who,Succ,HTable)-> HTable,
	%Value -> findValue(Htable).
	
% put/2
%puts(HashedKey, Data) -> ok.