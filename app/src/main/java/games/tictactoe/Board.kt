package games.tictactoe

/**
 * Classe que representa o tabuleiro do jogo da velha. O tabuleiro do jogo da velha é uma matriz
 * de 3x3 posições formando ao total 9 células ou casas. Para denotar uma casa como estando vazia,
 * usa-se o valor da constante [EMPTY].[<br><br>]
 * Para marcar uma posição do tabuleiro use:[<br><br>]
 * val board: Board = Board() [<br>]
 * board[[0, 0]] = 1 [<br><br>]
 * Para obter o valor numa posição do tabuleiro use:[<br><br>]
 * val board: Board = Board() [<br>]
 * val value: Byte = board[[0,0]]
 */
class Board(): Cloneable {

    companion object {
        /**Marcador DEFAULT para indicar célula vazia. Valor constante 0.*/
        const val EMPTY: Byte = 0
        /**Número de linhas do tabuleiro. Valor constante 3.*/
        const val LINES: Int = 3
        /**Número de colunas do tabuleiro. Valor constante 3.*/
        const val COLUMNS: Int = 3
    }

    /**Matriz 3x3 contendo as 9 células (casas) do tabuleiro.*/
    private var cells:Array<Array<Byte>> = Array(LINES){Array(COLUMNS){EMPTY}}
    /**Número de casas marcadas do tabuleiro.*/
    private var filledCells: Int = 0

    /**
     * Sobrescrever o operador [[l, c]] para obter valor da célula. Para isso usa-se a expressão na
     * forma matricial:[<br><br>]
     * val label = board[[line, column]]
     * @param line número da linha.
     * @param column número da coluna.
     * @return valor na célula [[linha,coluna]]
     */
    operator fun get(line: Int, column: Int): Byte {
        if (line !in 0 until LINES || column !in 0 until COLUMNS) {
            throw Exception("Invalid cell index.")
        }
        return cells[line][column]
    }

    /**
     * Sobrescrever o operador [[l, c]] para atribuir um valor à célula. Só pode ser marcada uma
     * célula que está vazia. Para isso usa-se a expressão na forma matricial:[<br><br>]
     * val board: Board = Board() [<br>]
     * board[[line, column]] = 1
     * @param line número da linha.
     * @param column número da coluna.
     * @param value valor a ser gravado
     */
    operator fun set(line: Int, column: Int, value: Byte) {
        if (line !in 0 until LINES || column !in 0 until COLUMNS) {
            throw Exception("Invalid cell index")
        }
        if (value == EMPTY) {
            throw Exception("Label cannot be $EMPTY")
        }
        if (!isFull()) {
            if (cells[line][column] == EMPTY) {
                cells[line][column] = value
                filledCells++
            } else {
                throw Exception("Overwriting a cell is prohibited.")
            }
        } else {
            throw Exception("All cells are marked.")
        }
    }

    /**
     * Marcar todas as células do tabuleiro com [EMPTY] que indica que a célula está 'vazia'.
     */
     fun clear() {
        for (line in 0 until LINES) {
            for (column in 0 until COLUMNS) {
                cells[line][column] = EMPTY
            }
        }
        filledCells = 0
    }

    /**
     * Clonar o tabuleiro em memória.
     * @return cópia do tabuleiro em memória.
     */
    public override fun clone(): Board {
        var clone: Board = Board()
        var cellsClone:Array<Array<Byte>> = Array(LINES){Array(COLUMNS){EMPTY}}
        for (line in 0 until LINES) {
            for (column in 0 until COLUMNS) {
                cellsClone[line][column] = this.cells[line][column]
            }
        }
        clone.filledCells = this.filledCells
        clone.cells = cellsClone
        return clone
    }

    /**
     * Verificar se o tabuleiro está cheio (com todas as casas preenchidas).
     * @return true, o tabuleiro está cheio, false, ele não está.
     */
    fun isFull() = (filledCells == 9)

    /**
     * Verificar se o tabuleiro está vazio (com nenhuma célula preenchida).
     * @return true, o tabuleiro está vazio, false, ele não está.
     */
    fun isEmpty() = (filledCells == 0)

    /**
     * Sobrescreve o método para formatar o tabuleiro em forma de String.
     * @return String formatada.
     */
    override fun toString(): String {
        var sb: StringBuilder = StringBuilder()
        for (l in 0 until LINES) {
            for (c in 0 until COLUMNS) {
                sb.append(cells[l][c].toString())
                sb.append("    ")
            }
            sb.append("\n")
        }
        return sb.toString()
    }

}