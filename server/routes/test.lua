local logger = require "logger"
local cjson = require "cjson.safe"
local data_access = require "da.data_access"
local fb = require "firebase_helper"

ngx.req.read_body()
local body = cjson.decode(ngx.req.get_body_data())

local a = fb.test("/users/user1/")

ngx.say(require"cjson.safe".encode(a))
