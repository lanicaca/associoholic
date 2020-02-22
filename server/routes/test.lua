local logger = require "logger"

local data_access = require "da.data_access"

local map = {}

for _ = 1, 100 do
    local code = data_access.generate_code()
    if map[code] then
        map[code] = map[code] + 1
    else
        map[code] = 1
    end
end

ngx.say(require"cjson.safe".encode(map))
