package games.tictactoe

/**
 * Classe que representa um ouvinte de eventos do jogo.
 */
interface GameListener {

    /**
     * Evento disparado em caso de partida terminada com vencedor.
     * @param winner jogador vencedor da partida.
     * @param line linha preenchida com os mesmos marcadores.
     */
    fun onWinning(winner: Player, line: BoardLine)

    /**
     * Evento disparado em caso de partida terminada sem vencedor, após terminar de marcar as 9 casas
     * do tabuleiro.
     */
    fun onFillingBoard()

}