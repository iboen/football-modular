package id.gits.football.main.teams

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import id.gits.football.R
import id.gits.football.data.Team
import kotlinx.android.synthetic.main.teams_row.view.*
import org.jetbrains.anko.layoutInflater

class TeamsAdapter(var list: List<Team>, private val itemListener: TeamItemListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<TeamsAdapter.ClubViewHolder>(), Filterable {
    private var listFiltered: List<Team> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        return ClubViewHolder(parent.context.layoutInflater.inflate(R.layout.teams_row, parent, false))
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val team = listFiltered[position]
        holder.tvName.text = team.strTeam
        with(holder.ivLogo) {
            Picasso.get()
                    .load(team.strTeamLogo)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.color.colorError)
                    .into(this)
        }

        holder.itemView.setOnClickListener {
            itemListener.onTeamClick(team)
        }
    }

    override fun getItemCount(): Int {
        return listFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                listFiltered = if (charString.isEmpty()) {
                    list
                } else {
                    val filteredList = arrayListOf<Team>()
                    for (row in list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.strTeam.toLowerCase().contains(charString.toLowerCase()))
                            filteredList.add(row)

                    }

                    filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = listFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                listFiltered = filterResults.values as List<Team>
                notifyDataSetChanged()
            }
        }
    }

    inner class ClubViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var ivLogo: ImageView = itemView.ivLogo
        var tvName: TextView = itemView.tvName

    }

    interface TeamItemListener {
        fun onTeamClick(clickedTeam: Team)
    }
}