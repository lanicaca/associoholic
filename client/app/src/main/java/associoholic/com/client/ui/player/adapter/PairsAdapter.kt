package associoholic.com.client.ui.player.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import associoholic.com.client.R
import associoholic.com.client.model.Player
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_player.*

class PairsAdapter(
        var fragment: Fragment,
        var list: List<Player> = listOf(),
        val pickPair: (Player) -> Unit
) : RecyclerView.Adapter<PairsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            fragment.layoutInflater.inflate(
                    R.layout.item_player, parent, false
            )
    )

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.player = list[position]
        holder.bind()
    }

    fun submitList(list: List<Player>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView),
            LayoutContainer, View.OnClickListener {

        lateinit var player: Player

        init {
            card_view.setOnClickListener(this)
        }

        fun bind() {
            player_name.text = player.name
            card_view.setCardBackgroundColor(
                    if (player.isSelected) fragment.resources.getColor(R.color.colorPrimary)
                    else fragment.resources.getColor(R.color.white)
            )
        }

        override fun onClick(v: View?) {
            pickPair(player)
        }
    }
}
