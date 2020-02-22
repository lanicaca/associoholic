package associoholic.com.client.ui.player

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import associoholic.com.client.R
import kotlinx.android.synthetic.main.player_fragment.*

class PlayerFragment : Fragment(R.layout.player_fragment), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.assFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        return_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.return_button -> findNavController().navigate(R.id.assFragment)
        }
    }
}
