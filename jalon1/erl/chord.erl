%% @author vincent
%% @doc @todo Add description to chord.


-module(chord).

%% ====================================================================
%% API functions
%% ====================================================================
-export([chord/3]).

%%
chord(NodeId, HashTable, NextId) ->
	io:format("~w IN CHORD: ~w -> ~w~n", [self(), target(NodeId), target(NextId)]),
	receive
		{lookup, Who, Key} ->
			Msg = lookup(NodeId, NextId, Key),
			Who ! Msg,
			logger ! {lookup, Who, Key, Msg},
			chord(NodeId, HashTable, NextId);
		{get, Who, Key} ->
			Msg = get(NodeId, HashTable, NextId, Key),
			Who ! Msg,
			logger ! {get, NodeId, Key, Msg},
			chord(NodeId, HashTable, NextId);
		{getself, Who, Key} ->
			Who ! getself(HashTable, Key),
			chord(NodeId, HashTable, NextId);
		{put, _, Key, Data} ->
			NewHashTable = put(NodeId, HashTable, NextId, Key, Data),
			logger ! {NodeId, Key, Data},
			chord(NodeId, NewHashTable, NextId);
		{putself, _, Key, Data} ->
			NewHashTable = putself(HashTable, Key, Data),
			chord(NodeId, NewHashTable, NextId);
		exit -> io:format("EXITING: ~w~n", [target(NodeId)]), exit(target(NodeId));
		_ -> chord(NodeId, HashTable, NextId)
	end.
  


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
		((Key>X) and (Y<X)) ->
			NextId;
		true -> target(NextId) ! {lookup, self(), Key},
				receive
					Value -> Value
				end
	end.

%%
get(NodeId, HashTable, NextId, Key) ->
	io:format("get ~w~n", [target(NodeId)]),
	N = lookup(NodeId, NextId, Key),
	case N of
		NodeId -> getself(HashTable, Key);
		ResponsibleId ->
			target(ResponsibleId) ! {getself, self(), Key},
			receive
				Value -> Value
			end
	end.

%%
getself(HashTable, Key) ->
	M = dict:find(Key,HashTable),
	case M of
	 {ok, Value} -> Value;
	 _ -> io:format("KEY NOT FOUND")
	end.
	

%%
put(NodeId, HashTable, NextId, Key, Data) ->
	N = lookup(NodeId,NextId,Key),
	case N of
		NodeId -> putself(HashTable, Key, Data);
		ResponsibleId ->
			target(ResponsibleId) ! {putself, self(), Key, Data},
			HashTable
	end.

%%
putself(HashTable, Key, Data) ->
	dict:store(Key,Data,HashTable).


