package id.gits.football.players

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.gits.football.data.Player
import id.gits.football.data.Team
import id.gits.football.player.PlayerActivity
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.startActivity

class PlayersFragment : Fragment(), PlayersContract.View {
    override lateinit var presenter: PlayersContract.Presenter

    private lateinit var swipeLayout: SwipeRefreshLayout

    private val items: ArrayList<Player> = arrayListOf()

    private lateinit var listAdapter: PlayersAdapter
    private lateinit var team: Team

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = PlayersFragmentUI().createView(AnkoContext.create(ctx, this))

        listAdapter = PlayersAdapter(items, object : PlayersAdapter.PlayerItemListener {
            override fun onPlayerClick(clickedPlayer: Player) {
                showPlayerDetailUi(clickedPlayer)
            }
        })

        with(view.findViewById<RecyclerView>(PlayersFragmentUI.recyclerViewId)) {
            adapter = listAdapter
        }

        with(view.findViewById<SwipeRefreshLayout>(PlayersFragmentUI.swipeRefreshId)) {
            swipeLayout = this
            setOnRefreshListener { presenter.getPlayers() }
        }

        arguments?.getParcelable<Team>(ARGUMENT_TEAM)?.let {
            team = it
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.setTeam(team)
        presenter.start()
    }

    override fun showError(message: String?) {
        message?.let { toast(it) }
    }

    override fun showPlayers(players: List<Player>) {
        items.clear()
        items.addAll(players)
        listAdapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        swipeLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeLayout.isRefreshing = false
    }

    override fun showPlayerDetailUi(player: Player) {
        context?.startActivity<PlayerActivity>(PlayerActivity.EXTRA_PLAYER to player)
    }

    companion object {
        private const val ARGUMENT_TEAM = "TEAM"

        fun newInstance(team: Team) = PlayersFragment()
                .apply {
                    arguments = Bundle().apply { putParcelable(ARGUMENT_TEAM, team) }
                }
    }

    class PlayersFragmentUI : AnkoComponent<Fragment> {
        companion object {
            const val swipeRefreshId = 1
            const val recyclerViewId = 2
        }

        override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
            swipeRefreshLayout {
                id = PlayersFragmentUI.swipeRefreshId

                recyclerView {
                    id = PlayersFragmentUI.recyclerViewId
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }
}