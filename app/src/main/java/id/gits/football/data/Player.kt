package id.gits.football.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(val idPlayer: String, val strPlayer: String, val strDescriptionEN: String,
                  val strNationality: String, val strCutout: String?, val strPosition: String,
                  val strWeight: String, val strHeight: String, val strFanart1: String?) : Parcelable