package id.gits.football.main

import id.gits.football.argumentCaptor
import id.gits.football.any
import id.gits.football.capture
import id.gits.football.data.Team
import id.gits.football.data.Match
import id.gits.football.data.source.SportsDataSource
import id.gits.football.data.source.SportsRepository
import id.gits.football.match.MatchContract
import id.gits.football.match.MatchPresenter
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

class MatchPresenterTest {
    @Mock
    private lateinit var sportsRepository: SportsRepository
    @Mock
    private lateinit var view: MatchContract.View

    private lateinit var presenter: MatchPresenter

    @Captor
    private lateinit var getHomeTeamCallbackCaptor: ArgumentCaptor<SportsDataSource.GetTeamCallback>

    private val match = Match("1", "1", "2",
            "Persikasi", "Persib", strDate = "12/12/2012",
            strTime = "14:00:00+00:00", isPast = false)

    private lateinit var teamHome: Team
    private lateinit var teamAway: Team

    @Before
    fun setupPresenter() {
        MockitoAnnotations.initMocks(this)

        presenter = MatchPresenter(match, sportsRepository, view)

        teamHome = Team("1", "Persikasi", "Sample Description", "http://example.com/image.jpg")
        teamAway = Team("2", "Persib", "Sample Description", "http://example.com/image.jpg")
    }

    @Test
    fun createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        presenter = MatchPresenter(match, sportsRepository, view)

        // Then the presenter is set to the view
        verify(view).presenter = presenter
    }

    @Test
    fun loadHomeClubFromRepositoryAndLoadIntoView() {
        with(presenter) {
            getClub()
        }

        // Callback is captured and invoked with stubbed tasks
        verify(sportsRepository, times(2)).getTeam(any(), capture(getHomeTeamCallbackCaptor))
        getHomeTeamCallbackCaptor.value.onTeamLoaded(teamHome)

        val inOrder = inOrder(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()

        val getClubArgumentCaptor = argumentCaptor<Team>()
        verify(view).showClubAway(capture(getClubArgumentCaptor))

        assertTrue(getClubArgumentCaptor.value.idTeam == "1")
    }


    @Test
    fun loadClubFromRepositoryAndShowError() {
        with(presenter) {
            getClub()
        }

        // Callback is captured and invoked with stubbed tasks
        verify(sportsRepository, times(2)).getTeam(any(), capture(getHomeTeamCallbackCaptor))
        val error = "Unknown error"
        getHomeTeamCallbackCaptor.value.onError(error)

        val inOrder = inOrder(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()

        val getClubArgumentCaptor = argumentCaptor<String>()
        verify(view).showError(capture(getClubArgumentCaptor))


        assertTrue(getClubArgumentCaptor.value == error)
    }

}