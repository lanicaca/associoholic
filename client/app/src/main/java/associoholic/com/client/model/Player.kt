package associoholic.com.client.model

data class Player(
        val id: Int,
        val name: String,
        val points: Int
) {
    var isSelected: Boolean = false
}