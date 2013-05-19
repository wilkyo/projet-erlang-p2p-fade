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
		{lookup, Who, Key} -> Who ! lookup(NodeId, NextId, Key);
		{get, Who, Key} -> Who ! get(NodeId, HashTable, NextId, Key);
		{put, _, Key, Data} -> put(NodeId, HashTable, NextId, Key, Data);
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

%%
lookup(NodeId, NextId, Key) -> ok.

%%
get(NodeId, HashTable, NextId, Key) -> ok.

%%
put(NodeId, HashTable, NextId, Key, Data) -> ok.


