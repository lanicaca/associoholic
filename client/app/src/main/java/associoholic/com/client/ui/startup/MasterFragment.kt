package associoholic.com.client.ui.startup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import associoholic.com.client.R
import associoholic.com.client.model.Game
import associoholic.com.client.repository.Repository
import associoholic.com.client.repository.network.base.Resource
import associoholic.com.client.util.addConditionalTextWather
import associoholic.com.client.util.isInt
import associoholic.com.client.util.setConditionalError
import kotlinx.android.synthetic.main.master_fragment.*

class MasterFragment :
        Fragment(R.layout.master_fragment),
        View.OnClickListener,
        TextWatcher,
        Observer<Resource<Game>> {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        next_button.setOnClickListener(this)
        return_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.next_button -> {
                if (validate()) handleNext()
                else handleError()
            }
            R.id.return_button -> findNavController().navigateUp()
        }
    }

    private fun handleError() {
        handleValidation()
        timer_text.addConditionalTextWather(this)
        rounds_text.addConditionalTextWather(this)
        words_text.addConditionalTextWather(this)
        name_text.addConditionalTextWather(this)
    }

    private fun handleNext() {
        Repository.getInstance().createGame(
                timer_text.text.toString().toInt(),
                rounds_text.text.toString().toInt(),
                words_text.text.toString().toInt(),
                name_text.text.toString()
        ).runAsync(true).observe(viewLifecycleOwner, this)
    }


    private fun validate(): Boolean {
        return !(timer_text.text.isNullOrBlank()
                || rounds_text.text.isNullOrBlank()
                || words_text.text.isNullOrBlank()
                || name_text.text.isNullOrBlank())
    }

    private fun handleValidation() {
        timer_text.text.toString().apply {
            timer_layout.setConditionalError(listOf(isNullOrBlank(), !isInt()))
        }
        rounds_text.text.toString().apply {
            rounds_layout.setConditionalError(listOf(isNullOrBlank(), !isInt()))
        }
        words_text.text.toString().apply {
            words_layout.setConditionalError(listOf(isNullOrBlank(), !isInt()))
        }
        name_text.text.toString().apply {
            name_layout.setConditionalError(listOf(isNullOrBlank()))
        }
    }

    override fun afterTextChanged(s: Editable?) {
        handleValidation()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun onChanged(t: Resource<Game>?) {
        t?.data?.let {
            Repository.getInstance().setCurrentGame(it)
            findNavController().navigate(MasterFragmentDirections.actionMasterFragmentToPlayerFragment(isMaster = 1))
        }
    }
}
