local cassandra_helper = require "cassandra_helper"
local cassandra = require "cassandra"

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

local function update_game(id, timer, words, no_of_players, no_of_rounds, points, code)
    local query = [[
        UPDATE games SET
            timer = ?,
            words = ?,
            no_of_players = ?,
            no_of_rounds = ?,
            points = ?,
            code = ?
        WHERE
            id = ?
    ]]

    local args = { timer, words, no_of_players, no_of_rounds, points, code or cassandra.unset, id }
    return cassandra_helper.query(query, args, {})
end

local function create_game(timer, no_of_rounds)
    local code = generate_code()
    local id = cassandra.timeuuid(ngx.time())
    local res, err
    res, err = set_code_to_game_id()
    if err then
        return nil, err
    end

    res, err = update_game(
        id,
        timer,
        {},
        0,
        no_of_rounds,
        {},
        code
    )
    if err then
        return nil, err
    end

    return id
end

return {
    create_game = create_game,
    update_game = update_game,
    get_game = get_game,
}
