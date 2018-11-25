package id.gits.football.utils

/**
 * Helpers to start activities in a modularized world.
 */


/**
 * An [android.app.Activity] that can be addressed by an intent.
 */
interface AddressableActivity {
    /**
     * The activity class name.
     */
    val className: String
}

object ActivityHelper {
    const val PACKAGE_NAME = "id.gits.football"

    object Team : AddressableActivity{
        override val className =  "$PACKAGE_NAME.team.TeamActivity"
        const val EXTRA_TEAM = "TEAM"
    }
}