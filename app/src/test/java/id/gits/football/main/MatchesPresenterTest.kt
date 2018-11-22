package id.gits.football.main

import id.gits.football.argumentCaptor
import id.gits.football.capture
import id.gits.football.data.Match
import id.gits.football.data.source.SportsDataSource
import id.gits.football.data.source.SportsRepository
import id.gits.football.main.matches.MatchesContract
import id.gits.football.main.matches.MatchesPresenter
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*
import id.gits.football.any

class MatchesPresenterTest {
    @Mock
    private lateinit var sportsRepository: SportsRepository
    @Mock
    private lateinit var view: MatchesContract.View

    private lateinit var presenter: MatchesPresenter

    @Captor
    private lateinit var loadTasksCallbackCaptor: ArgumentCaptor<SportsDataSource.LoadMatchesCallback>

    private lateinit var matches: MutableList<Match>

    private val leagueId = "4328"

    @Before
    fun setupPresenter() {
        MockitoAnnotations.initMocks(this)

        presenter = MatchesPresenter(sportsRepository, view)
        matches = arrayListOf(
                Match("1", "1", "2",
                        "Persikasi", "Persib", strDate = "12/12/2012",
                        strTime = "14:00:00+00:00", isPast = false),
                Match("1", "3", "4",
                        "AC Milan", "PSJ", strDate = "14/12/2012",
                        strTime = "14:00:00+00:00", isPast = false))
    }

    @Test
    fun createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        presenter = MatchesPresenter(sportsRepository, view)

        // Then the presenter is set to the view
        verify(view).presenter = presenter
    }

    @Test
    fun loadNextMatchesFromRepositoryAndLoadIntoView() {
        with(presenter) {
            setType(MainActivity.TYPE.NEXT)
            setLeague(leagueId)
            getMatches()
        }

        // Callback is captured and invoked with stubbed tasks
        verify(sportsRepository).getNextMatches(any(), capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onMatchesLoaded(matches)

        val inOrder = inOrder(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()

        val getMatchesArgumentCaptor = argumentCaptor<List<Match>>()
        verify(view).showMatches(capture(getMatchesArgumentCaptor))

        assertTrue(getMatchesArgumentCaptor.value.size == 2)
    }

    @Test
    fun loadPastMatchesFromRepositoryAndLoadIntoView() {
        with(presenter) {
            setType(MainActivity.TYPE.PAST)
            setLeague(leagueId)
            getMatches()
        }

        // Callback is captured and invoked with stubbed tasks
        verify(sportsRepository).getLastMatches(any(), capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onMatchesLoaded(matches)

        val inOrder = inOrder(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()

        val getMatchesArgumentCaptor = argumentCaptor<List<Match>>()
        verify(view).showMatches(capture(getMatchesArgumentCaptor))

        assertTrue(getMatchesArgumentCaptor.value.size == 2)
    }

    @Test
    fun loadAnyMatchesFromRepositoryAndShowError() {
        with(presenter) {
            setType(MainActivity.TYPE.NEXT)
            setLeague(leagueId)
            getMatches()
        }

        // Callback is captured and invoked with stubbed tasks
        verify(sportsRepository).getNextMatches(any(), capture(loadTasksCallbackCaptor))
        val error = "Unknown error"
        loadTasksCallbackCaptor.value.onError(error)

        val inOrder = inOrder(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()

        val getMatchesArgumentCaptor = argumentCaptor<String>()
        verify(view).showError(capture(getMatchesArgumentCaptor))

        assertTrue(getMatchesArgumentCaptor.value == error)
    }

}