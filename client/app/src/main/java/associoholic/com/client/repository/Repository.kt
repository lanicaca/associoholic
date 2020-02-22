package associoholic.com.client.repository


import androidx.lifecycle.Observer
import associoholic.com.client.common.PrefManager


class Repository : Observer<String?> {

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
}