CREATE KEYSPACE ass WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE ass.games (
    id text,
    no_of_players 
);

CREATE TABLE ass.gamecode_reverse_map (
);
