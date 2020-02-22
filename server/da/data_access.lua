local cassandra_helper = require "cassandra_helper"
local cassandra = require "cassandra"
local logger = require "logger"

local function generate_code()
    local str="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    local referral_code = {}
    for i = 1, 6 do
        referral_code[i] = string.char(str:byte(math.random(1, #str)))
    end
    return table.concat(referral_code)
end

local function set_code_to_game_id(code, id)
    local query = "UPDATE code_to_game_id SET id = ? where code = ?"
    local args = { id, code }
    logger.error("data acces args %s", args)
    return cassandra_helper.query(query, { id, code }, {})
end

local function get_game_id_from_code(code)
    local query = "SELECT id FROM code_to_game_id WHERE code = ?"
    return cassandra_helper.query(query, { code }, {})
end

local function get_game(id)
    local query = "SELECT * FROM games WHERE id = ?"
    return cassandra_helper.query(query, { id }, {})
end

local function update_game(id, timer, no_of_rounds, no_of_words, no_of_players, words, points, code)
    local query = [[
        UPDATE games SET
            timer = ?,
            no_of_rounds = ?,
            no_of_words = ?,
            no_of_players = ?,
            words = ?,
            points = ?,
            code = ?
        WHERE
            id = ?
    ]]

    local args = {
        timer or cassandra.unset,
        no_of_rounds or cassandra.unset,
        no_of_words or cassandra.unset,
        no_of_players or cassandra.unset,
        words or cassandra.unset,
        points or cassandra.unset,
        code or cassandra.unset,
        id
    }

    return cassandra_helper.query(query, args, {})
end

local function create_game(timer, no_of_rounds, no_of_words)
    local code = generate_code()
    local id = cassandra.timeuuid(ngx.time())

    local _, err_set = set_code_to_game_id(code, id)
    if err_set then
        return nil, err_set
    end

    local _, err_create = update_game(
        id,
        timer,
        no_of_rounds,
        no_of_words,
        0,
        {},
        {},
        code
    )
    if err_create then
        return nil, err_create
    end

    return id
end

return {
    create_game = create_game,
    update_game = update_game,
    get_game = get_game,
}
