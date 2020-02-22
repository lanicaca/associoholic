package associoholic.com.client.ui.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import associoholic.com.client.model.Player

class PairsViewModel(handle: SavedStateHandle) : ViewModel() {

    val pairs: MutableLiveData<Player> = handle.getLiveData(PAIRS, null)

    companion object {
        const val PAIRS = "pairs"
    }
}