package id.gits.football.main.teams

import android.content.Intent
import android.os.Bundle
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
import id.gits.football.data.Team
import id.gits.football.utils.PrefHelper
import id.gits.football.utils.SearchEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.support.v4.toast
import id.gits.football.utils.ActivityHelper

class TeamsFragment : Fragment(), TeamsContract.View {
    override lateinit var presenter: TeamsContract.Presenter

    private lateinit var swipeLayout: SwipeRefreshLayout

    private val items: ArrayList<Team> = arrayListOf()

    var type: TeamsContract.TYPE = TeamsContract.TYPE.NORMAL

    private lateinit var listAdapter: TeamsAdapter
    private lateinit var spinner: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = TeamsFragmentUI().createView(AnkoContext.create(ctx, this))

        listAdapter = TeamsAdapter(items, object : TeamsAdapter.TeamItemListener {
            override fun onTeamClick(clickedTeam: Team) {
                showTeamDetailUi(clickedTeam)
            }
        })

        with(view.findViewById<RecyclerView>(TeamsFragmentUI.recyclerViewId)) {
            adapter = listAdapter
        }

        with(view.findViewById<SwipeRefreshLayout>(TeamsFragmentUI.swipeRefreshId)) {
            swipeLayout = this
            setOnRefreshListener { presenter.getTeams() }
        }

        arguments?.getSerializable(ARGUMENT_TYPE)?.let {
            type = it as TeamsContract.TYPE
        }

        spinner = view.findViewById(TeamsFragmentUI.leagueSpinnerId)
        if (type == TeamsContract.TYPE.FAV) {
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.setLeague(PrefHelper.getLeague(ctx))
        presenter.setType(type)
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
                    presenter.getTeams()
                }

            }
        }
    }

    override fun showTeams(teams: List<Team>) {
        items.clear()
        items.addAll(teams)
        listAdapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        swipeLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeLayout.isRefreshing = false
    }

    override fun showTeamDetailUi(team: Team) {
        val intent = Intent()
        intent.putExtra(ActivityHelper.Team.EXTRA_TEAM, team)
        intent.setClassName(ActivityHelper.PACKAGE_NAME, ActivityHelper.Team.className)
        context?.startActivity(intent)
//        context?.startActivity<TeamActivity>(ActivityHelper.Team.EXTRA_TEAM to team)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchEvent(event: SearchEvent) {
        if (event.type == SearchEvent.TYPE_TEAM || event.type == SearchEvent.TYPE_FAV)
            listAdapter.filter.filter(event.query)
    }

    companion object {
        private const val ARGUMENT_TYPE = "TYPE"

        fun newInstance(type: TeamsContract.TYPE) = TeamsFragment()
                .apply {
                    arguments = Bundle().apply { putSerializable(ARGUMENT_TYPE, type) }
                }
    }

    class TeamsFragmentUI : AnkoComponent<Fragment> {
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
                    id = TeamsFragmentUI.swipeRefreshId

                    recyclerView {
                        id = TeamsFragmentUI.recyclerViewId
                        layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        }
    }
}