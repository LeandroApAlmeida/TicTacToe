package games.tictactoe

/**
 * Define um jogador. Um jogador deve, obrigatóriamente, ter uma marcador, ou rótulo com o qual
 * ele deverá marcar o tabuleiro em seu turno.[<br><br>]
 * A classe é abstrata pois não define o comportamento de um jogador, a forma como ele marcará o
 * tabuleiro quando fizer sua jogada. Logo, o método que define o comportamento de um jogador ao
 * fazer a sua jogada é o [getPosition]. Ele devolve a posição vazia do tabuleiro selecionada pelo
 * jogador em seu turno.
 * @param label marcador do jogador.
 */
abstract class Player(private val label: Byte) {

    /**
     * Obter o marcador do jogador.
     * @return marcador do jogador.
     */
    fun getLabel(): Byte = label

    /**
     * Marcar um posição vazia do tabuleiro. Cada classe que herda de [Player] deve implementar o método
     * de acordo com as regras específicas de seleção de casas vazias. O parâmetro [board] é uma
     * cópia do tabuleiro do jogo, para um jogador automatizado "enxergar" o tabuleiro conforme a
     * configuração atual do jogo e decidir sua jogada.
     * @param board cópia do tabuleiro do jogo da velha.
     */
    abstract fun getPosition(board: Board): CellPosition

    /**
     * Comparar objetos. O critério para comparar jogadores é se eles tem os mesmo marcador.
     * @param other outro objeto a ser comparado.
     */
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Player) return false
        return other.label == this.label
    }

}