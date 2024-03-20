package games.tictactoe

/**
 * Classe que representa a posição de uma célula do tabuleiro do jogo da velha, identificada por
 * seus parâmetros de [[linha,coluna]]. Exemplo, a primeira célula é indicada por [[0,0]].
 * @param line índice da linha da célula.
 * @param column índice da coluna da célula.
 */
class CellPosition(val line: Int, val column: Int): Cloneable {

    /**
     * Clonar a posição.
     * @return cópia da posição em memória.
     */
    public override fun clone(): CellPosition {
        return CellPosition(line, column)
    }

    /**
     * Comparar objetos. O critério para comparar posições é se apontam para a mesma linha e coluna.
     * @param other outro objeto a ser comparado.
     * @return true, são posições iguais, false, são diferentes.
     */
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is CellPosition) return false
        return other.line == this.line && other.column == this.column
    }

    /**
     * Formatar como String.
     * @return String formatada.
     */
    override fun toString(): String {
        return "[$line, $column]"
    }

}