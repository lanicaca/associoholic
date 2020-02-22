package associoholic.com.client.ui.player

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.fragment.findNavController
import associoholic.com.client.R
import associoholic.com.client.model.Player
import associoholic.com.client.repository.network.base.Resource
import associoholic.com.client.ui.player.adapter.PairsAdapter
import kotlinx.android.synthetic.main.pairs_fragment.*

class PairsFragment :
        Fragment(R.layout.pairs_fragment),
        View.OnClickListener,
        Observer<Resource<List<Player>>>
{
    private val viewModel: PairsViewModel by viewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    lateinit var adapter : PairsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.assFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        return_button.setOnClickListener(this)
        adapter = PairsAdapter(this, listOf(), pickPair = {p: Player -> pickPair(p)})
        players_recycler.adapter = adapter
        adapter.submitList(listOf())
        viewModel.pairs.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })
    }

    private fun pickPair(pair: Player){
        adapter.list.forEach {
            it.isSelected = false
        }
        pair.isSelected = true
        viewModel.pairs.postValue(pair)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.return_button -> findNavController().navigate(R.id.assFragment)
        }
    }

    override fun onChanged(t: Resource<List<Player>>?) {
        t?.data?.let {
            adapter.submitList(it)
        }
    }
}
