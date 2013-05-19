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
lookup(NodeId, NextId, Key) ->
	X = id(NodeId),
	Y = id(NextId),
	if 
		((Key>X) and (Key<Y)) ->
		 		NextId;
		true -> target(NextId) ! {lookup, Key}
	end,
	receive 
		{lookup, Key} -> lookup(NodeId, NextId, Key)
	end.

%%
get(NodeId, HashTable, NextId, Key) -> 
	N = lookup(Key,NodeId,NextId),
	case N of
		NodeId -> 
			M = dict:find(Key,HashTable),
			case M of
			 {ok, Value} -> Value;
			 _else -> io:format("KEY NOT FOUND")
			end;
		_else -> false
	end.
	

%%
put(NodeId, HashTable, NextId, Key, Data) ->
	N = lookup(NodeId,NextId,Key),
	case N of
		NodeId -> HashTable = dict:store(Key,Data,HashTable);
		_else -> ok
	end.


