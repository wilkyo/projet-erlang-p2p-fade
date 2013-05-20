%% @author willy
%% @doc Saves the messages in a file


-module(logger).

%% ====================================================================
%% API functions
%% ====================================================================
-export([init/0]).

init() ->
	{{Year, Month, Day}, {Hour, Minutes, Seconds}} = erlang:localtime(),
	Filename = lists:concat([Year, "-", Month, "-", Day, "_", Hour, ":", Minutes, ":", Seconds, ".log"]),
	{ok, Log} = file:open(Filename, [write,raw,binary]),
	io:format("~w~n", [Log]),
	%erlang:group_leader(Log, self()),
	wait(Filename).

%% ====================================================================
%% Internal functions
%% ====================================================================

wait(Filename) ->
	receive
		{create, Id} -> file:write_file(Filename, io_lib:fwrite("~p~n", [Id]), [append]);
		{next, Id, NextId} -> file:write_file(Filename, io_lib:fwrite("~p.next = ~p.~n", [Id, NextId]), [append]);
		{lookup, Id, Key, Res} -> file:write_file(Filename, io_lib:fwrite("lookup(~p,~p) = ~p~n", [Id, Key, Res]), [append]);
		{get, NodeId, HashTable, NextId, Key, Res} ->  file:write_file(Filename, io_lib:fwrite("get(~p,~p,~p,~p) = ~p~n", [NodeId, HashTable, NextId, Key, Res]), [append]);
		{NodeId, HashTable, NextId, Key, Data} -> file:write_file(Filename, io_lib:fwrite("put(~p,~p,~p,~p,~p)", [NodeId, HashTable, NextId, Key, Data]), [append])
	end,
	wait(Filename).
