DROP KEYSPACE ass;

CREATE KEYSPACE ass WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE IF NOT EXISTS ass.games (
    id timeuuid,
    timer int,
    no_of_rounds int,
    current_round int,
    current_player int,
    no_of_words int,
    players text,
    words text,
    code text,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ass.code_to_game_id (
    code text,
    id timeuuid,
    PRIMARY KEY (code)
);
