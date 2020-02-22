package associoholic.com.client.repository


import androidx.lifecycle.Observer
import associoholic.com.client.common.PrefManager
import associoholic.com.client.model.Game
import associoholic.com.client.repository.network.CreateGameRequest
import associoholic.com.client.repository.network.JoinGameRequest
import com.bumptech.glide.load.engine.Resource


class Repository : Observer<String?> {

    private lateinit var currentGame: Game

    fun createGame(timer: Int, rounds: Int, words: Int, name: String) = CreateGameRequest(timer, rounds, words, name)
    fun joinGame(gameCode: String, name: String) = JoinGameRequest(gameCode, name)

    fun endGame() {
        PrefManager.getInstance()?.setUserToken(null, null)
    }

    init {
        PrefManager.getInstance()?.liveDataToken?.observeForever(this)
    }

    companion object {

        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(): Repository =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: Repository().also { INSTANCE = it }
                }

        fun resetInstance() {
            INSTANCE?.let { PrefManager.getInstance()?.liveDataToken?.removeObserver(it) }
            INSTANCE = null
        }
    }

    override fun onChanged(t: String?) {
        //todo
    }

    fun setCurrentGame(game: Game) {
        currentGame = game
    }
}