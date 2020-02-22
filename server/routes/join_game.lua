local logger = require "logger"
local http = require "http_helper"
local data_access = require "da.data_access"

local body = http.get_body()

if not body.code or type(body.code) ~= "string" then
    http.end_phase({ message = "code is required and must be a string", }, ngx.HTTP_BAD_REQUEST)
end

if not body.timer or type(body.name) ~= "string" then
    http.end_phase({ message = "name is required and must be a string", }, ngx.HTTP_BAD_REQUEST)
end

local game, err_join = data_access.join_game(body.code, body.name)
if err_join then
    logger.error("something went wrong joining the game: %s", err_join)
    http.end_phase(err_join, ngx.HTTP_INTERNAL_SERVER_ERROR)
end

http.end_phase({ game = game })
