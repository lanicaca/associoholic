local logger = require "logger"
local http = require "http_helper"
local data_access = require "da.data_access"

local body = http.get_body()

if not body.timer or type(body.timer) ~= "number" then
    http.end_phase({ message = "timer is required and must be a number", }, ngx.HTTP_BAD_REQUEST)
end

if not body.timer or type(body.no_of_rounds) ~= "number" then
    http.end_phase({ message = "no_of_rounds is required and must be a number", }, ngx.HTTP_BAD_REQUEST)
end

if not body.timer or type(body.no_of_words) ~= "number" then
    http.end_phase({ message = "no_of_words is required and must be a number", }, ngx.HTTP_BAD_REQUEST)
end

local id, err_create = data_access.create_game(body.timer, body.no_of_rounds, body.no_of_words)
if err_create then
    logger.error("something went wrong: %s, %s", id, err_create)
    ngx.exit(ngx.HTTP_INTERNAL_SERVER_ERROR)
end

local game, err_get = data_access.get_game(id)
if err_get then
    logger.error("something went wrong getting the game: %s, %s", game, err_get)
    ngx.exit(ngx.HTTP_INTERNAL_SERVER_ERROR)
end

http.end_phase({ game = game })
