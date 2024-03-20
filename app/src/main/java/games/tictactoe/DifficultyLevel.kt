package games.tictactoe

/**
 * Nível de dificuldade oferecido pelo jogador do sistema (jogador simulado) em uma partida.
 */
enum class DifficultyLevel {
    /**Normal: Neste nível é possível vencer o app com certa frequência.*/
    NORMAL,
    /**Difícil: Neste nível é possível vencer o app com bastante dificuldade.*/
    HARD,
    /**Invencível: Neste nível é impossível vencer o app, apenas empatar.*/
    INVINCIBLE
}