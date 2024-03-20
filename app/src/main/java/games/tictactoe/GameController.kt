package games.tictactoe

import kotlin.random.Random

/**
 * Classe que representa o controlador do jogo. Ele é responsável por monitorar a alternância das
 * jogadas entre os dois competidores, inclementar suas pontuações, determinar a condição de final
 * de jogo e iniciar novas partidas.
 * @param player1 Jogador 1
 * @param player2 Jogador 2
 * @param gameListeners Lista de ouvintes do jogo. Recebem notificações de eventos ocorridos no jogo.
 */
class GameController(private val player1: Player, private val player2: Player, private val gameListeners: Array<GameListener>) {

    /**Tabuleiro do jogo da velha.*/
    private val board: Board = Board()
    /**Jogador que vai marcar o tabuleiro na vez.*/
    private var currentPlayer: Player
    /**Pontuação de Jogador 1.*/
    private var player1Score: Int = 0
    /**Pontuação de Jogador 2.*/
    private var player2Score: Int = 0
    /**Ordem da partida.*/
    private var matchNumber: Int = 0
    /**Indica se houve vencedor.*/
    private var wasWinner: Boolean = true
    /**Indica se o jogo está bloqueado (tabuleiro cheio ou vitória de algum jogador).*/
    private var isBlocked: Boolean = false
    /**Jogador da vez para marcar o tabuleiro.*/
    val currentplayer: Player  get() = currentPlayer
    /**Status de jogo bloqueado.*/
    val isblocked: Boolean get() = isBlocked
    /**Pontuação de Jogador 1.*/
    val player1score: Int get() = player1Score
    /**Pontuação de JOgador 2.*/
    val player2score: Int get() = player2Score
    /**Ordem da partida.*/
    val matchnumber: Int get() = matchNumber

    //Inicializa o jogo com jogador aleatório. Este será o que marcará o tabuleiro na primeira
    //vez que iniciar o jogo.
    init {
        currentPlayer = when (Random.nextInt(2)) {
            0 -> player1
            1 -> player2
            else -> player1
        }
    }

    /**
     * Iniciar uma nova partida. Ao iniciar as seguintes condições devem ser verificadas:[<br><br>]
     * Na primeira jogada a escolha do jogador a marcar é aleatória.[<br><br>]
     * A partir da seguinte jogada em diante, verifica-se o seguinte:[<br><br>]
     * [<ul>]
     * [<li>]Havendo vencedor na partida anterior, quem inicia marcando o tabuleiro é este jogador
     * que venceu.[</li>][<br><br>]
     * [<li>]Não havendo vencedor na partida anterior, quem inicia marcando o tabuleiro é o jogador
     * adversário do último que marcou o tabuleiro.[</li>]
     * [</ul>]
     * A ordem da partida é inclementada e aguarda o jogador da vez marcar o tabuleiro no seu turno,
     * para fazer a alternância de jogadores. Em caso de jogador simulado, a ativação de sua escolha
     * não se dará nesta classe, mas na classe que fizer a chamada a esta classe. Aqui faz-se o controle
     * apenas se o jogador certo está marcando o tabuleiro em seu turno.
     */
    fun startNewMatch() {
        isBlocked = false
        matchNumber++
        board.clear()
        currentPlayer = if (wasWinner) {
            currentPlayer
        } else {
            swapPlayers(currentPlayer)
        }
        wasWinner = false
    }

    /**
     * Realizar a jogada. A classe [Player] têm o método abstract [Player.getPosition] que retorna
     * a casa do tabuleiro que foi selecionada por um jogador. O tabuleiro receberá o marcador do
     * jogador nesta casa. Após fazer a marcação do tabuleiro, será realizada a verificação do estado
     * do mesmo da seguinte forma: [<br><br>]
     * [<ol>]
     * [<li>]Caso nenhuma linha horizontal, vertical ou diagonal tenha sido completada com os mesmos
     * marcadores, verifica se ainda há casas vazias no tabuleiro, e havendo, alterna a vez da jogada.
     * Não havendo mais casas vazias a serem marcadas no tabuleiro, notifica objetos ouvintes que
     * implementam a interface [GameListener] registrados sobre a condição de tabuleiro cheio
     * através da chamada ao método [GameListener.onFillingBoard]. As jogadas são bloqueadas até que
     * se inicie uma nova partida com a chamada ao método [startNewMatch].[</li>][<br><br>]
     * [<li>]Caso alguma linha vertical, horizontal ou diagonal tenha sido completada com os
     * mesmos marcadores do jogador, este vence a partida. Sua pontuação é inclementada e todos os
     * objetos ouvintes que implementam a interface [GameListener] registrados são notificados sobre
     * a condição de vitória do jogador através da chamada ao método [GameListener.onWinning] que
     * identifica a linha completada e o jogador que completou. As jogadas são bloqueadas até que
     * se inicie uma nova partida com a chamada ao método [startNewMatch].[</li>]
     * [</ol>]
     * @param player jogador que realizará a jogada.
     * @return posição do tabuleiro marcada pelo jogador.
     */
    fun makeTheMove(player: Player): CellPosition? {
        var position: CellPosition? = null
        if (!isBlocked) {
            if (player == currentPlayer) {
                position = player.getPosition(board.clone())
                board[position.line, position.column] = currentPlayer.getLabel()
                //Verifica se este jogador completou alguma das linhas do tabuleiro com marcadores seus.
                val boardLine: BoardLine? = checkTheBoard(position)
                if (boardLine == null) {
                    //Nenhuma linha foi completada com marcadores do jogador.
                    if (board.isFull()) {
                        //O tabuleiro não tem mais posições vazias.
                        isBlocked = true //Bloqueia o jogo.
                        //Notifica os ouvintes sobre o evento.
                        gameListeners.forEach {
                            it.onFillingBoard()
                        }
                    } else {
                        currentPlayer = swapPlayers(player)
                    }
                } else {
                    //Alguma linha foi completada com os marcadores do jogador.
                    isBlocked = true //Bloqueia o jogo.
                    wasWinner = true //Indica que houve vencedor.
                    //Atualiza a pontuação do jogador.
                    if (currentPlayer == player1) {
                        player1Score++
                    } else {
                        player2Score++
                    }
                    //Notifica os ouvintes sobre o evento.
                    gameListeners.forEach {
                        it.onWinning(currentPlayer, boardLine)
                    }
                }
            } else {
                throw Exception("It's the opponent's turn to mark.")
            }
        }
        return position
    }

    /**
     * Trocar os jogadores. Caso [player] seja [player1], retorna [player2]. Caso [player] seja [player2],
     * retorna [player1].
     * @param player Jogador.
     * @return jogador trocado.
     */
    private fun swapPlayers(player: Player): Player = if (player == player1) {
        player2
    } else {
        player1
    }

    /**
     * Verifica a condição do tabuleiro logo após a jogada. A posição que foi marcada pelo jogador
     * é recebida no parâmetro position. Com base nesta posição, o método verifica se se completou
     * uma linha horizontal, vertical ou diagonal com os mesmos marcadores do jogador que acaba de
     * marcar. Se completou, devolve a identificação da linha no método. Caso não tenha completado,
     * devolve [<i>]null[</i>].
     * @param position posição do tabuleiro que foi marcada na jogada atual.
     * @return linha do tabuleiro identificada por [BoardLine] caso tenha completado, ou null, caso
     * não complete nenhuma linha.
     */
    private fun checkTheBoard(position: CellPosition): BoardLine? {
        //Identifica o marcador do jogador.
        val label: Byte = board[position.line, position.column]
        //Identifica a linha completada. Null é a condição padrão.
        var boardLine: BoardLine? = null
        when (position.line) {
            0 -> {
                when (position.column) {
                    0 -> {
                        if (label == board[0, 1] && label == board[0, 2]) {
                            boardLine = BoardLine.HORIZONTAL_1
                        } else if (label == board[1, 0] && label == board[2, 0]) {
                            boardLine = BoardLine.VERTICAL_1
                        } else if (label == board[1, 1] && label == board[2, 2]) {
                            boardLine = BoardLine.DIAGONAL_1
                        }
                    }
                    1 -> {
                        if (label == board[0, 0] && label == board[0, 2]) {
                            boardLine = BoardLine.HORIZONTAL_1
                        } else if (label == board[1, 1] && label == board[2, 1]) {
                            boardLine = BoardLine.VERTICAL_2
                        }
                    }
                    2 -> {
                        if (label == board[0, 0] && label == board[0, 1]) {
                            boardLine = BoardLine.HORIZONTAL_1
                        } else if (label == board[1, 2] && label == board[2, 2]) {
                            boardLine = BoardLine.VERTICAL_3
                        } else if (label == board[1, 1] && label == board[2, 0]) {
                            boardLine = BoardLine.DIAGONAL_2
                        }
                    }
                }
            }
            1 -> {
                when (position.column) {
                    0 -> {
                        if (label == board[1, 1] && label == board[1, 2]) {
                            boardLine = BoardLine.HORIZONTAL_2
                        } else if (label == board[0, 0] && label == board[2, 0]) {
                            boardLine = BoardLine.VERTICAL_1
                        }
                    }
                    1 -> {
                        if (label == board[1, 0] && label == board[1, 2]) {
                            boardLine = BoardLine.HORIZONTAL_2
                        } else if (label == board[0, 1] && label == board[2, 1]) {
                            boardLine = BoardLine.VERTICAL_2
                        } else if (label == board[0, 0] && label == board[2, 2]) {
                            boardLine = BoardLine.DIAGONAL_1
                        } else if (label == board[0, 2] && label == board[2, 0]) {
                            boardLine = BoardLine.DIAGONAL_2
                        }
                    }
                    2 -> {
                        if (label == board[1, 0] && label == board[1, 1]) {
                            boardLine = BoardLine.HORIZONTAL_2
                        } else if (label == board[0, 2] && label == board[2, 2]) {
                            boardLine = BoardLine.VERTICAL_3
                        }
                    }
                }
            }
            2 -> {
                when (position.column) {
                    0 -> {
                        if (label == board[2, 1] && label == board[2, 2]) {
                            boardLine = BoardLine.HORIZONTAL_3
                        } else if (label == board[0, 0] && label == board[1, 0]) {
                            boardLine = BoardLine.VERTICAL_1
                        } else if (label == board[0, 2] && label == board[1, 1]) {
                            boardLine = BoardLine.DIAGONAL_2
                        }
                    }
                    1 -> {
                        if (label == board[2, 0] && label == board[2, 2]) {
                            boardLine = BoardLine.HORIZONTAL_3
                        } else if (label == board[0, 1] && label == board[1, 1]) {
                            boardLine = BoardLine.VERTICAL_2
                        }
                    }
                    2 -> {
                        if (label == board[2, 0] && label == board[2, 1]) {
                            boardLine = BoardLine.HORIZONTAL_3
                        } else if (label == board[0, 2] && label == board[1, 2]) {
                            boardLine = BoardLine.VERTICAL_3
                        } else if (label == board[0, 0] && label == board[1, 1]) {
                            boardLine = BoardLine.DIAGONAL_1
                        }
                    }
                }
            }
        }
        return boardLine
    }

    /**
     * Verifica se uma determinada casa do tabuleiro está vazia.
     * @param position posição da casa a ser verificada.
     * @return true, a casa está vazia, false, está marcada.
     */
    fun isEmptyBoardPosition(position: CellPosition): Boolean = board[position.line, position.column] == Board.EMPTY

    /**
     * Bloqueia o jogo.
     */
    fun block() {
        isBlocked = true
    }

}