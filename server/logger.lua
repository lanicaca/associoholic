local cjson = require "cjson.safe"

local ERR = ngx.ERR

local function _flatten_arg(v)
    local vType = type(v)

    if vType == "table" then
        return cjson.encode(v) or "(unserializable table)"
    elseif vType == "function" or vType == "userdata" or vType == "thread" then
        return tostring(v)
    elseif v == nil then
        return "(nil)"
    else
        return v
    end
end

local function _flatten_args(...)
    local new_args = {}
    for i = 1, select("#", ...) do
        local v = select(i, ...)
        new_args[#new_args + 1] = tostring(_flatten_arg(v))
    end

    return unpack(new_args)
end

local function error(message, ...)
    if ... then
        message = string.format(message, _flatten_args(...))
    end
    ngx.log(ERR, message)
end

return {
    error = error
}
