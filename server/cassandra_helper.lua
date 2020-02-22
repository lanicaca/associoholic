local cassandra = require "cassandra"
local logger = require "logger"

local global_connection

local function get_connection()
    if (not global_connection) then
        local peer = assert(cassandra.new {
            host = "10.254.1.237",
            port = 9042,
            keyspace = "ass"
        })
        peer:settimeout(1000)
        global_connection = peer
    end

    return global_connection

end

local function query(q, args, options)
    local session = get_connection()
    if not session then
        assert(false, "Failed to get Cassandra session")
    end

    assert(session:connect())
    local result, err = session:execute(q, args, options)
    session:close()

    if (not result) then
        logger.error("Failed to run query: %s args: %s options: %s error: %s", q, args, options, err)
    end

    return result, err
end


return {
    get_connection = get_connection,
    query = query,
}
