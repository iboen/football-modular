package id.gits.football.utils

class SearchEvent(val query: String, val type: Int) {

    companion object {
        const val TYPE_MATCH: Int = 0
        const val TYPE_TEAM: Int = 1
        const val TYPE_FAV: Int = 2
    }
}
