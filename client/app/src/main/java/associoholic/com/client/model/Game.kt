package associoholic.com.client.model

data class Game(
        val id: String,
        val timer: Int,
        val roundsCount: Int,
        val wordsCount: Int,
        val players: List<Player>,
        val words: List<String>,
        val gameCode: String
)

//id timeuuid,
//    timer int,
//    no_of_rounds int,
//    no_of_words int,
//    players list<text>,
//    words list<text>,
//    code text,
//    PRIMARY KEY (id)