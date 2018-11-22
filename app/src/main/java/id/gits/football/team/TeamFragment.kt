package id.gits.football.team

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.*
import com.squareup.picasso.Picasso
import id.gits.football.Injection
import id.gits.football.R
import id.gits.football.R.menu.menu_details
import id.gits.football.data.Team
import id.gits.football.players.PlayersFragment
import id.gits.football.players.PlayersPresenter
import kotlinx.android.synthetic.main.team_fragment.*
import kotlinx.android.synthetic.main.team_fragment.view.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast

class TeamFragment : Fragment(), TeamContract.View {

    override lateinit var presenter: TeamContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val team = arguments?.get(ARGUMENT_TEAM) as Team
        val view = inflater.inflate(R.layout.team_fragment, container, false)


        view.teamViewPager.adapter = PagerAdapter(ctx, activity?.supportFragmentManager, team)
        view.teamTabLayout.setupWithViewPager(view.teamViewPager)

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun showTeam(team: Team) {
        teamTvName.text = team.strTeam
        teamTvYear.text = team.intFormedYear
        teamTvStadium.text = team.strStadium

        if (!team.strTeamLogo.isNullOrEmpty()) {
            Picasso.get()
                    .load(team.strTeamLogo)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.color.colorError)
                    .into(teamIvLogo)
        }

        if (!team.strStadiumThumb.isNullOrEmpty()) {
            Picasso.get()
                    .load(team.strStadiumThumb)
                    .placeholder(R.color.colorBackgroundGrey)
                    .error(R.color.colorError)
                    .into(teamIvStadium)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(message: String?) {
        message?.let { toast(it) }
    }

    override fun showAddFavoriteSuccess() {
        view?.let { snackbar(it, getString(R.string.match_added_to_fav)) }
    }

    override fun showRemoveFavoriteSuccess() {
        view?.let { snackbar(it, getString(R.string.match_removed_from_fav)) }
    }

    override fun showToggleFavoriteError() {
        view?.let { snackbar(it, getString(R.string.match_error_fav)) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(menu_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.finish()
                true
            }
            R.id.add_to_favorite -> {
                if (presenter.isFavorite()) presenter.removeFromFavorite() else presenter.addToFavorite()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.findItem(R.id.add_to_favorite).apply {
            if (presenter.isFavorite()) {
                this?.setIcon(R.drawable.ic_added_to_favorite)
            } else {
                this?.setIcon(R.drawable.ic_add_to_favorite)
            }
        }
    }

    override fun invalidateMenu() {
        act.invalidateOptionsMenu()
    }

    companion object {
        private const val ARGUMENT_TEAM = "TEAM"

        fun newInstance(match: Team) =
                TeamFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARGUMENT_TEAM, match)
                    }
                }
    }

    class PagerAdapter(val ctx: Context, fm: FragmentManager?, val team: Team) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {

            return when (position) {
                0 -> TeamOverviewFragment.newInstance(team)
                else -> PlayersFragment.newInstance(team).also {
                    PlayersPresenter(Injection.provideSportsRepository(ctx), it)
                }
            }
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) "Overview" else "Players"
        }
    }
}