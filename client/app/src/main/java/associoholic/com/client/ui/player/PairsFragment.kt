package associoholic.com.client.ui.player

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import associoholic.com.client.R
import associoholic.com.client.model.Player
import associoholic.com.client.repository.Repository
import associoholic.com.client.repository.network.base.Resource
import associoholic.com.client.ui.player.adapter.PairsAdapter
import kotlinx.android.synthetic.main.pairs_fragment.*

class PairsFragment :
        Fragment(R.layout.pairs_fragment),
        View.OnClickListener,
        Observer<Resource<List<Player>>> {
    private val viewModel: PairsViewModel by viewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private val args: PairsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.assFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        return_button.setOnClickListener(this)

        players_recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        players_recycler.adapter = PairsAdapter(this, listOf(), pickPair = { p: Player -> pickPair(p) })

        game_code.text = "CODE: \n ${Repository.getInstance().getGame().code}"
        viewModel.pairs.observe(viewLifecycleOwner, Observer {
            (players_recycler.adapter as PairsAdapter).notifyDataSetChanged()
        })
        button_start.visibility = if (args.isMaster == 0) {
            players_recycler.setPadding(8)
            View.GONE
        }else View.VISIBLE
        button_start.setOnClickListener {
            //todo on start !
        }
        (players_recycler.adapter as PairsAdapter).submitList(listOf(
                Player(1, "First name", 0),
                Player(2, "Seconf name", 0),
                Player(3, "Third name", 0),
                Player(4, "Fourth name", 0),
                Player(3, "Third name", 0),
                Player(4, "Fourth name", 0),
                Player(3, "Third name", 0),
                Player(4, "Fourth name", 0),
                Player(3, "Third name", 0),
                Player(4, "Fourth name", 0)
        ))
    }

    private fun pickPair(pair: Player) {
        (players_recycler.adapter as PairsAdapter).list.forEach {
            it.isSelected = false
        }
        pair.isSelected = true
        viewModel.pairs.postValue(pair)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.return_button -> findNavController().navigate(R.id.assFragment)
        }
    }

    override fun onChanged(t: Resource<List<Player>>?) {
        t?.data?.let {
            (players_recycler.adapter as PairsAdapter).submitList(it)
        }
    }
}
