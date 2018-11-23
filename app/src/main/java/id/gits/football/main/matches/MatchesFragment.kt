package id.gits.football.main.matches

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import id.gits.football.data.League
import id.gits.football.data.Match
import id.gits.football.main.MainActivity
import id.gits.football.match.MatchActivity
import id.gits.football.utils.SearchEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.support.v4.toast
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.*
import id.gits.football.utils.PrefHelper
import java.util.*


class MatchesFragment : Fragment(), MatchesContract.View {
    override lateinit var presenter: MatchesContract.Presenter

    private lateinit var swipeLayout: SwipeRefreshLayout

    val items: ArrayList<Match> = arrayListOf()

    var type: MainActivity.TYPE = MainActivity.TYPE.PAST

    private lateinit var listAdapter: MatchesAdapter
    private lateinit var spinner: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = MatchesFragment.MatchesFragmentUI().createView(AnkoContext.create(ctx, this))

        listAdapter = MatchesAdapter(items, object : MatchesAdapter.ScheduleItemListener {
            override fun onAlarmClick(clickedMatch: Match) {
                addToCalendar(clickedMatch)
            }

            override fun onMatchClick(clickedMatch: Match) {
                showMatchDetailUi(clickedMatch)
            }
        })

        with(view.findViewById<RecyclerView>(MatchesFragment.MatchesFragmentUI.recyclerViewId)) {
            adapter = listAdapter
        }

        with(view.findViewById<SwipeRefreshLayout>(MatchesFragmentUI.swipeRefreshId)) {
            swipeLayout = this
            setOnRefreshListener { presenter.getMatches() }
        }

        arguments?.getSerializable(ARGUMENT_TYPE)?.let {
            type = it as MainActivity.TYPE
        }

        presenter.setType(type)
        presenter.setLeague(PrefHelper.getLeague(ctx))

        spinner = view.findViewById(MatchesFragmentUI.leagueSpinnerId)
        if (type == MainActivity.TYPE.FAV) {
            spinner.visibility = View.GONE
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun showError(message: String?) {
        message?.let { toast(it) }
    }

    override fun showLeagues(leagues: List<League>) {
        with(spinner) {
            ArrayAdapter(
                    ctx,
                    android.R.layout.simple_spinner_item,
                    leagues.map { it.strLeague }
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                this.adapter = adapter
            }

            // change selection
            val idx = leagues.indexOf(leagues.find { it.idLeague == PrefHelper.getLeague(ctx) })
            this.setSelection(idx)

            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val leagueId = leagues[position].idLeague
                    PrefHelper.saveLeague(ctx, leagueId)
                    presenter.setLeague(leagueId)
                    presenter.getMatches()
                }

            }
        }
    }

    override fun showMatches(matches: List<Match>) {
        items.clear()
        items.addAll(matches)
        listAdapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        swipeLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeLayout.isRefreshing = false
    }

    override fun showMatchDetailUi(match: Match) {
        context?.startActivity<MatchActivity>(MatchActivity.EXTRA_MATCH to match)
    }

    fun addToCalendar(match: Match) {
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra(CalendarContract.Events.TITLE, "${match.strHomeTeam} vs ${match.strAwayTeam}")
        val dateStart = match.toDate()
        if (dateStart != null) {
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    dateStart.time)
            val cal: Calendar = Calendar.getInstance()
            cal.time = dateStart
            cal.add(Calendar.MINUTE, 90)

            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                    cal.time.time)
            intent.putExtra(CalendarContract.Events.ALL_DAY, false)
        } else {
            intent.putExtra(CalendarContract.Events.ALL_DAY, true)
        }
        intent.putExtra(CalendarContract.Events.DESCRIPTION, match.toFormattedDate())
        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchEvent(event: SearchEvent) {
        if (event.type == SearchEvent.TYPE_MATCH || event.type == SearchEvent.TYPE_FAV)
            listAdapter.filter.filter(event.query)
    }

    companion object {

        private const val ARGUMENT_TYPE = "TYPE"

        fun newInstance(type: MainActivity.TYPE) =
                MatchesFragment().apply {
                    arguments = Bundle().apply { putSerializable(MatchesFragment.ARGUMENT_TYPE, type) }
                }
    }

    class MatchesFragmentUI : AnkoComponent<Fragment> {
        companion object {
            const val swipeRefreshId = 1
            const val recyclerViewId = 2
            const val leagueSpinnerId = 3
        }

        override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
            verticalLayout {
                spinner {
                    id = leagueSpinnerId
                }.lparams {
                    topMargin = dip(8)
                    bottomMargin = dip(8)
                    width = matchParent
                }

                swipeRefreshLayout {
                    id = swipeRefreshId

                    recyclerView {
                        id = recyclerViewId
                        layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        }
    }
}