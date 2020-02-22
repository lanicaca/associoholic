local logger = require "logger"
local http = require "http_helper"
local data_access = require "da.data_access"

local body = http.get_body()

if not body.timer or type(body.id) ~= "string" then
    http.end_phase({ message = "id is required and must be a string", }, ngx.HTTP_BAD_REQUEST)
end

local game, err_get = data_access.get_game(body.id)
if err_get then
    logger.error("something went wrong getting the game: %s", err_get)
    http.end_phase(err_get, ngx.HTTP_INTERNAL_SERVER_ERROR)
end

http.end_phase({ game = game })
