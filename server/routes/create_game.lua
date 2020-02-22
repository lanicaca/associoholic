local logger = require "logger"
local http = require "http_helper"
local data_access = require "da.data_access"

local body = http.get_body()

local id, err_create = data_access.create_game(body.timer, body.no_of_rounds)
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
