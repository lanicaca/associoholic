local logger = require "logger"
local cjson = require "cjson.safe"

local function get_claims()
    local headers = ngx.req.get_headers()
    return {
        game_id = headers.game_id,
        player_id = headers.player_id
    }
end

local function get_body()
    ngx.req.read_body()
    return cjson.decode(ngx.req.get_body_data() or nil) or {}
end

local function end_phase(response, status)
    ngx.status = status or ngx.HTTP_OK
    ngx.say(cjson.encode(response))
    return ngx.exit(ngx.HTTP_OK)
end

return {
    get_claims = get_claims,
    get_body = get_body,
    end_phase = end_phase,
}
