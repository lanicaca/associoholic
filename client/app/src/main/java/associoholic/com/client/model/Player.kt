package associoholic.com.client.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(
        val id: Int,
        val name: String,
        val points: Int
): Parcelable {
    var isSelected: Boolean = false
}