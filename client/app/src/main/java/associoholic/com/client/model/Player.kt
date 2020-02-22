package associoholic.com.client.model

data class Player(
        val id: String,
        val name: String
) {
    var isSelected: Boolean = false
}