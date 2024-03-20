package games.tictactoe

/**
 * Classe que representa um jogador humano que interage com a interface gráfica do aplicativo.
 * @param label marcador do jogador humano.
 */
class HumanPlayer(private val label: Byte): Player(label) {

    private var p: CellPosition = CellPosition(0,0)

    /**
     * Ler ou escrever uma posição do tabuleiro (get/set) para denotar a posição selecionada.
     */
    var position: CellPosition get() = p ; set(v) {this.p = v}

    override fun getPosition(board: Board): CellPosition {
        return position
    }

}