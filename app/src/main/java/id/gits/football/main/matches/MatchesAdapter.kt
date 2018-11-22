package id.gits.football.main.matches

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import id.gits.football.R
import id.gits.football.data.Match
import kotlinx.android.synthetic.main.matches_row.view.*
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.layoutInflater

class MatchesAdapter(var list: List<Match>, private val itemListener: ScheduleItemListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<MatchesAdapter.ClubViewHolder>(), Filterable {

    private var listFiltered: List<Match> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        return ClubViewHolder(parent.context.layoutInflater.inflate(R.layout.matches_row, parent, false))
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val match = listFiltered[position]
        holder.tvHomeName.text = match.strHomeTeam
        holder.tvAwayName.text = match.strAwayTeam

        if (match.isPast) {
            holder.tvHomeScore.text = String.format(match.intHomeScore.toString())
            holder.tvAwayScore.text = String.format(match.intAwayScore.toString())
            holder.bgAlarm.backgroundColorResource = R.color.colorBackgroundGrey
            holder.ivAlarm.visibility = View.GONE
        } else {
            holder.tvHomeScore.visibility = View.GONE
            holder.tvAwayScore.visibility = View.GONE
            holder.ivAlarm.visibility = View.VISIBLE
            holder.bgAlarm.backgroundColorResource = R.color.colorWhite
            holder.ivAlarm.setOnClickListener {
                itemListener.onAlarmClick(match)
            }
        }

        holder.tvDate.text = match.toFormattedDate()

        holder.layoutDetail.setOnClickListener {
            itemListener.onMatchClick(match)
        }
    }

    override fun getItemCount(): Int {
        return listFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listFiltered = list
                } else {
                    val filteredList = arrayListOf<Match>()
                    for (row in list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.strHomeTeam.toLowerCase().contains(charString.toLowerCase()) ||
                                row.strAwayTeam.contains(charSequence)) {
                            filteredList.add(row)
                        }
                    }

                    listFiltered = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = listFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                listFiltered = filterResults.values as List<Match>
                notifyDataSetChanged()
            }
        }
    }

    inner class ClubViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var tvDate: TextView = itemView.tvDate
        var tvHomeName: TextView = itemView.tvHomeName
        var tvHomeScore: TextView = itemView.tvHomeScore
        var tvAwayName: TextView = itemView.tvAwayName
        var tvAwayScore: TextView = itemView.tvAwayScore
        var ivAlarm: ImageView = itemView.matchesIvAlarm
        var bgAlarm: View = itemView.matchesBgAlarm
        var layoutDetail: View = itemView.matchesDetail

    }

    interface ScheduleItemListener {
        fun onMatchClick(clickedMatch: Match)
        fun onAlarmClick(clickedMatch: Match)
    }
}