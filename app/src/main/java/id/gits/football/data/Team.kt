package id.gits.football.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team (val idTeam: String, val strTeam: String, val strDescriptionEN: String?,
                 val strTeamLogo: String?, val intFormedYear: String? = null, val strStadium: String? = null,
                 val strStadiumThumb: String? = null)
    : Parcelable