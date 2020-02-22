package associoholic.com.client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_ass.*

class AssFragment : Fragment(R.layout.fragment_ass), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        create_button.setOnClickListener (this)
        join_button.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
       when (v?.id) {
           R.id.create_button -> {
               findNavController().navigate(R.id.action_assFragment_to_masterFragment)
           }
           R.id.join_button -> {
              findNavController().navigate(R.id.action_assFragment_to_joinFragment)
           }
       }
    }
}
