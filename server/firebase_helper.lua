local firebase = require "firebase"

local firebase_pid = "associoholic-4542d"


local function test(path)
    firebase.post(firebase_pid,path,{name = "suad"},true)
end

return {
    test = test
}
