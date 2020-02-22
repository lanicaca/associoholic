local cassandra_helper = require "cassandra_helper"
local cassandra = require "cassandra"
local uuid = require "resty.uuid"
local cjson = require "cjson"

local logger = require "logger"

local function generate_code()
    local text = ""
    for _ = 1, 6 do
        text = text .. string.char(math.random(65,90))
    end
    return text
end

local function set_code_to_game_id(code, id)
    local query = "UPDATE code_to_game_id SET id = ? where code = ?"
    local args = { cassandra.timeuuid(id), code }
    return cassandra_helper.query(query, args, {})
end

local function get_empty_array()
    return setmetatable({}, cjson.array_mt)
end

local function get_game(id)
    local query = "SELECT * FROM games WHERE id = ?"
    local result, error = cassandra_helper.query(query, { cassandra.timeuuid(id) }, {})
    if error then
        return nil, error
    end
    if #result < 1 then
        logger.error("Nothing to return")
        return nil, { message = "Nothing to return" }
    end

    local game = result[1]

    return {
        id = game.id,
        code = game.code,
        timer = game.timer,
        current_round = game.current_round,
        current_player = game.current_player,
        no_of_words = game.no_of_words,
        no_of_rounds = game.no_of_rounds,
        players = game.players ~= "null" and cjson.decode(game.players) or get_empty_array(),
        words = game.words ~= "null" and cjson.decode(game.words) or get_empty_array(),
    }
end

local function update_game(id, timer, no_of_rounds, no_of_words, players, words, code, current_round, current_player)
    local query = [[
        UPDATE games SET
            timer = ?,
            no_of_rounds = ?,
            no_of_words = ?,
            players = ?,
            words = ?,
            code = ?,
            current_round = ?,
            current_player = ?
        WHERE
            id = ?
    ]]

    local args = {
        timer or cassandra.unset,
        no_of_rounds or cassandra.unset,
        no_of_words or cassandra.unset,
        cjson.encode(players) or cassandra.unset,
        cjson.encode(words) or cassandra.unset,
        code or cassandra.unset,
        current_round or cassandra.unset,
        current_player or cassandra.unset,
        cassandra.timeuuid(id)
    }

    return cassandra_helper.query(query, args, {})
end

local function create_game(timer, no_of_rounds, no_of_words, name)
    local code = generate_code()
    local id = uuid.generate_time_safe()

    local _, err_set = set_code_to_game_id(code, id)
    if err_set then
        return nil, err_set
    end

    local _, err_create = update_game(
        id,
        timer,
        no_of_rounds,
        no_of_words,
        { [1] = { name = name, points = 0 } },
        nil,
        code,
        0,
        0
    )

    if err_create then
        return nil, err_create
    end

    return id
end

local function get_game_id_from_code(code)
    local query = "SELECT id FROM code_to_game_id WHERE code = ?"
    local result, err =  cassandra_helper.query(query, { code }, {})
    if err then
        return nil, err
    end
    if #result < 1 then
        local message = string.format("Game with code %s does not exist", code)
        logger.error(message)
        return nil, { message = message }
    end

    return result[1].id
end

local function add_a_player(id, name)
    local game, err = get_game(id)
    if err then
        return nil, err
    end

    for _, data in pairs(game.players) do
        if data.name == name then
            return nil, { message = string.format("Name %s already used, please use another one", name) }
        end
    end
    table.insert(game.players, { name = name, points = 0})

    local _, err_update = update_game(id, nil, nil, nil, game.players, nil, nil)
    if err_update then
        return nil, err_update
    end

    return game
end

local function join_game(code, name)
    local game_id, err = get_game_id_from_code(code)
    if err then
        return nil, err
    end
    return add_a_player(game_id, name)
end

return {
    generate_code = generate_code,
    create_game = create_game,
    join_game = join_game,
    get_game = get_game,
}
