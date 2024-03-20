package games.tictactoe

/**
 * Classe que representa uma linha qualquer na horizontal, vertical ou diagonal do tabuleiro do jogo
 * da velha.
 */
enum class BoardLine {
    /**Primeira linha do tabuleiro (acima).*/
    HORIZONTAL_1,
    /**Segunda linha do tabuleiro (centro).*/
    HORIZONTAL_2,
    /**Terceira linha do tabuleiro (abaixo).*/
    HORIZONTAL_3,
    /**Primeira coluna do tabuleiro (à esquerda).*/
    VERTICAL_1,
    /**Segunda coluna do tabuleiro (centro).*/
    VERTICAL_2,
    /**Terceira coluna do tabuleiro (à direita)*/
    VERTICAL_3,
    /**Diagonal à esquerda (\).*/
    DIAGONAL_1,
    /**Diagonal à direita (/).*/
    DIAGONAL_2
}