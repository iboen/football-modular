package id.gits.football.players

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import id.gits.football.R
import id.gits.football.data.Player
import kotlinx.android.synthetic.main.players_row.view.*
import org.jetbrains.anko.layoutInflater

class PlayersAdapter(var list: List<Player>, private val itemListener: PlayerItemListener) : RecyclerView.Adapter<PlayersAdapter.ClubViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        return ClubViewHolder(parent.context.layoutInflater.inflate(R.layout.players_row, parent, false))
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val player = list[position]
        holder.tvName.text = player.strPlayer
        holder.tvPosition.text = player.strPosition
        with(holder.ivLogo) {
            Picasso.get()
                    .load(player.strCutout)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.color.colorError)
                    .into(this)
        }

        holder.itemView.setOnClickListener {
            itemListener.onPlayerClick(player)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivLogo: ImageView = itemView.ivLogo
        var tvName: TextView = itemView.tvName
        var tvPosition: TextView = itemView.tvPosition

    }

    interface PlayerItemListener {
        fun onPlayerClick(clickedPlayer: Player)
    }
}