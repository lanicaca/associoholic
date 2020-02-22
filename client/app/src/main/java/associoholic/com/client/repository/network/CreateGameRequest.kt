package associoholic.com.client.repository.network

import associoholic.com.client.model.Game
import associoholic.com.client.repository.network.base.BaseRequest
import associoholic.com.client.repository.network.base.Resource
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class CreateGameRequest(private val timer: Int,
                        private val rounds: Int,
                        private val words: Int,
                        private val name: String) : BaseRequest<Game>() {
    override fun buildRequest(): Request {
        val json = JsonObject().apply {
            addProperty("timer", timer)
            addProperty("no_of_rounds", rounds)
            addProperty("no_of_words", words)
            addProperty("name",  name)
        }

        request = Request.Builder()
                .url("$BASE_URL$CREATE_GAME")
                .post(RequestBody.create(JSON, json.toString()))

        return super.buildRequest()
    }

    override fun onSuccess(response: Response): Resource<Game> {
        val game: Game = gson.fromJson(
                parseToJson(response).asJsonObject.getAsJsonObject("game"),
                object : TypeToken<Game>() {}.type
        )
        val r = Resource.success(game)
        postValue(r)
        return r
    }
}