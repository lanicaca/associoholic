package associoholic.com.client.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
        val id: String,
        val timer: Int,
        @SerializedName("no_of_rounds")val roundsCount: Int,
        @SerializedName("no_of_words") val wordsCount: Int,
        val players: List<Player>,
        val words: List<String>,
        val code: String,
        @SerializedName("current_player") val currentPlayer: Int,
        @SerializedName("current_round") val currentRound: Int
): Parcelable

//id timeuuid,
//    "players": [
//            {
//                "name": "Lana",
//                "points": 0
//            }
//        ],
//        "no_of_words": 10,
//        "id": "25be14de-55bf-11ea-a42f-0242ac110003",
//        "current_player": 0,
//        "words": [],
//        "timer": 100,
//        "code": "YZTRGA",
//        "no_of_rounds": 7,
//        "current_round": 0