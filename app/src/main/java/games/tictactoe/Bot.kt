package games.tictactoe

import android.os.SystemClock
import kotlin.random.Random

/**
 * Classe que representa um jogador artificial, do próprio sistema. O usuário pode configurar qual
 * o nível de dificuldade que o jogador do sistema vai oferecer na partida. No nível fácil, o app
 * vai jogar de forma a permitir algumas falhas que podem ser aproveitadas pelo oponente humano. No
 * nível expert o app se torna teoricamente invencível.
 * @param label Marcador do jogador artificial
 * @param opponentLabel Marcador do oponente.
 * @param difficultyLevel Nível de dificuldade da partida.
 */
class Bot(private val label: Byte, private val opponentLabel: Byte, private var difficultyLevel: DifficultyLevel): Player(label) {

    /**
     * Representa as posições de todas as linhas do tabuleiro, formadas por 3 células.
     * @see BoardLine
     **/
    private val boardLines: Array<Array<CellPosition>>

    init {
        //Linhas horizontais.
        val horizontal1 = arrayOf(CellPosition(0, 0), CellPosition(0, 1), CellPosition(0, 2))
        val horizontal2 = arrayOf(CellPosition(1, 0), CellPosition(1, 1), CellPosition(1, 2))
        val horizontal3 = arrayOf(CellPosition(2, 0), CellPosition(2, 1), CellPosition(2, 2))
        //Linhas verticais.
        val vertical1   = arrayOf(CellPosition(0, 0), CellPosition(1, 0), CellPosition(2, 0))
        val vertical2   = arrayOf(CellPosition(0, 1), CellPosition(1, 1), CellPosition(2, 1))
        val vertical3   = arrayOf(CellPosition(0, 2), CellPosition(1, 2), CellPosition(2, 2))
        //Linhas diagonais.
        val diagonal1   = arrayOf(CellPosition(0, 0), CellPosition(1, 1), CellPosition(2, 2))
        val diagonal2   = arrayOf(CellPosition(0, 2), CellPosition(1, 1), CellPosition(2, 0))
        //Array contendo todas as formações de 3 linhas.
        boardLines = arrayOf(
            horizontal1, horizontal2, horizontal3,
            vertical1,   vertical2,   vertical3,
            diagonal1,   diagonal2
        )
    }

    /**
     * Marcar uma posição vazia do tabuleiro do jogo da velha. O algoritmo analizará uma série de
     * regras, a depender do nível de dificuldade oferecido na partida, para que possa explorar
     * qualquer possibilidade de vencer o oponente humano, ao mesmo tempo em que procura não ser
     * vencido por este. No nível máximo de dificuldade, ele sempre conduzirá a um empate ou uma
     * vitória a seu favor.
     * @param board Tabuleiro do jogo da velha aonde está ocorrendo as partidas.
     */
    override fun getPosition(board: Board): CellPosition {
        var positions: MutableList<CellPosition>
        if (!board.isEmpty()) {
            positions = getEmptyPositionsThatCompleteALine(board, this.label)
            if (positions.isNotEmpty()) {
                simulateHumanDecisionTime(500L, 1000L)
                //Vai completar uma linha. Marca e vence o adversário humano.
                return getRandomPosition(positions)
            }
            positions = getEmptyPositionsThatCompleteALine(board, this.opponentLabel)
            if (positions.isNotEmpty()) {
                simulateHumanDecisionTime(500L, 1000L)
                //Vai desarmar o adversário humano para não perder o jogo para este.
                return getRandomPosition(positions)
            }
            //Determina se o computador vai seguir a melhor estratégia na jogada ou não.
            //Se o nível de dificuldade estiver definido como "Expert", o computador sempre
            //segue a melhor estratégia. Do contrário, esta decisão será estatística, sendo
            //que no nível "Difícil" ele têm uma probabilidade maior de seguir a melhor
            //estratégia que no nível "Fácil".
            val bestStrategy: Boolean = when (difficultyLevel) {
                DifficultyLevel.NORMAL -> (Random.nextInt(1000) + 1 < 400)
                DifficultyLevel.HARD -> (Random.nextInt(10000) + 1 < 9500)
                DifficultyLevel.INVINCIBLE -> true
            }
            if (bestStrategy) {
                //Vai seguir a melhor estratégia de jogada...
                positions = getEmptyPositionsThatLeadToAVictory(board, this.label)
                if (positions.isNotEmpty()) {
                    simulateHumanDecisionTime(500L, 1000L)
                    //Se há posições que levam a uma situação de vitória a seu favor,
                    //marca alguma e define a vitória contra o adversário humano no
                    //próximo turno.
                    return getRandomPosition(positions)
                }
                positions = getEmptyPositionsOnCondition(board, this.label, 'V')
                if (positions.isNotEmpty()) {
                    simulateHumanDecisionTime(500L, 1000L)
                    //Se há posições que,se marcadas levam a uma vitória inevitável
                    //nos próximos turnos, marca alguma, e define a vitória contra o
                    //adversário humano.
                    return getRandomPosition(positions)
                }
                positions = getSafeEmptyPositionsToMark(board, this.label)
                if (positions.isNotEmpty()) {
                    simulateHumanDecisionTime(500L, 1000L)
                    //Marca uma posição qualquer, que não ofereça risco de dar a vitória ao adversário.
                    return getRandomPosition(positions)
                } else {
                    simulateHumanDecisionTime(500L, 1000L)
                    //Marca uma posição aleatória qualquer.
                    positions = getAllEmptyPositions(board)
                    return getRandomPosition(positions)
                }
            } else {
                //Não vai seguir a melhor estratégia...
                //Impede qualquer chance que o computador tenha de forçar uma vitória contra
                //o seu oponente humano.
                val positions1: List<CellPosition> = getEmptyPositionsOnCondition(board, this.label, 'V')
                val positions2: List<CellPosition> = getEmptyPositionsThatLeadToAVictory(board, this.label)
                positions = getAllEmptyPositions(board)
                for (position in positions1) {
                    positions.remove(position)
                }
                for (position in positions2) {
                    positions.remove(position)
                }
                if (positions.isNotEmpty()) {
                    simulateHumanDecisionTime(500L, 1000L)
                    return getRandomPosition(positions)
                } else {
                    simulateHumanDecisionTime(500L, 1000L)
                    positions = getAllEmptyPositions(board)
                    return getRandomPosition(positions)
                }
            }
        } else {
            //Neste caso, não há nenhuma posição do tabuleiro marcada. Vai marcar
            //a primeira posição do tabuleiro. A escolha da posição é aleatória.
            positions = getAllEmptyPositions(board)
            simulateHumanDecisionTime(500L, 1000L)
            return getRandomPosition(positions)
        }
    }

    /**
     * Selecionar uma posição aleatória na lista passada para marcar o tabuleiro. Esta posição é
     * devolvida no retorno do método.
     * @param positions lista de posições vazias do tabuleiro recuperadas de algum processo.
     * @return posição vazia aleatória da lista.
     */
    private fun getRandomPosition(positions: List<CellPosition>): CellPosition {
        val idx: Int = Random.nextInt(positions.size)
        return positions[idx]
    }

    /**
     * Simular o tempo que um ser humano leva para tomar uma decisão de marcar o tabuleiro.
     * @param minTime tempo mínimo para retornar a posição vazia do tabuleiro.
     * @param maxTime tempo máximo para retornar a posição vazia do tabuleiro.
     */
    private fun simulateHumanDecisionTime(minTime: Long, maxTime: Long) {
        SystemClock.sleep(Random.nextLong(minTime, maxTime + 1))
    }

    /**
     * Obter todas as posições vazias do tabuleiro.
     * @param tabuleiro tabuleiro do jogo.
     * @return posições vazias no tabuleiro.
     */
    private fun getAllEmptyPositions(board: Board): MutableList<CellPosition> {
        val emptyPositions: MutableList<CellPosition> = mutableListOf()
        for (line in 0 until Board.LINES) {
            for (column in 0 until Board.COLUMNS) {
                if (board[line, column] == Board.EMPTY) {
                    emptyPositions.add(CellPosition(line, column))
                }
            }
        }
        return emptyPositions
    }

    /**
     * Retornar uma lista com todas as posições vazias no tabuleiro que se marcadas
     * completam uma linha horizontal, vertical ou diagonal com o marcador passado.
     * @param board tabuleiro do jogo.
     * @param label marcador de referência.
     * @return lista com as posições vazias que atendam à condição ou lista vazia, caso não encontre
     * nenhuma.
     */
    private fun getEmptyPositionsThatCompleteALine(board: Board, label: Byte): MutableList<CellPosition> {
        /*
        A ideia aqui é localizar este tipo de formação no tabuleiro, considerando-se o marcador X como
        exemplo e denotando as casas vazias a retornar pelo algoritmo por #:

             .     .                  .     .                  .     .                  .     .
          X  .  #  .  X               .     .  X            X  .     .              #   .     .
        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .
          #  .  #  .                  .  #  .               X  .  #  .  X               .  X  .
        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .
          X  .     .               X  .     .               #  .     .                  .     . X
             .     .                  .     .                  .     .                  .     .
           (Exemplo 1)              (Exemplo 2)              (Exemplo 3)              (Exemplo 4)

         Nota-se que o que se busca com este algoritmo é localizar todas as linhas na horizontal,
         vertical ou diagonal que tenham duas casas com o mesmo marcador e uma casa vazia. O algoritmo
         retorna então todas as casas vazias que estão sob esta condição. Qualquer destas casas se
         forem marcadas com um X leva o jogador à vitória na partida.

         Executando o algoritmo para os exemplos mostrados, teríamos o seguinte resultado para X no
         retorno do algoritmo (aonde vazio está marcado com #):

         Exemplo 1: [0,2] [1,0] [1,1]
         Exemplo 2: [1,1]
         Exemplo 3: [1,1] [2,0]
         Exemplo 4: [0,0]
         */
        val emptyPositions: MutableList<CellPosition> = mutableListOf()
        var labelCounter: Int
        var emptyCounter: Int
        for (boardLine in boardLines) {
            //Conta a quantidade de marcadores e de vazios na linha.
            labelCounter = 0
            emptyCounter = 0
            for (position in boardLine) {
                if (board[position.line, position.column] != Board.EMPTY) {
                    if (board[position.line, position.column] == label) {
                        labelCounter++
                    }
                } else {
                    emptyCounter++
                }
            }
            //Havendo 2 marcadores e 1 vazio, localiza o vazio e adiciona à lista de
            //retorno.
            if (labelCounter == 2 && emptyCounter == 1) {
                for (position in boardLine) {
                    if (board[position.line, position.column] == Board.EMPTY) {
                        emptyPositions.add(position)
                        break
                    }
                }
            }
        }
        return emptyPositions
    }

    /**
     * Retornar as duas posições vazias de uma linha se ela tem alguma casa marcada com o marcador
     * passado.
     * @param board tabuleiro do jogo.
     * @param boardLine linha do tabuleiro na horizontal, vertical ou diagonal.
     * @param label marcador de referência.
     * @return lista com as duas posições vazias na linha com um marcador ou lista vazia, caso não
     * tenha casas vazias que atenda ao critério.
     */
    private fun getEmptyPositionsInLineWithASingleLabel(board: Board, boardLine: Array<CellPosition>,
    label: Byte): MutableList<CellPosition> {
        /*
        A ideia aqui é localizar este tipo de formação numa linha horizontal, vertical ou diagonal
        do tabuleiro, considerando-se o marcador X como exemplo e denotando as casas vazias a retornar
        pelo algoritmo por #:

        HORIZONTAL_1   ->        VERTICAL_1     ->        DIAGONAL_1     ->        VERTICAL_2     ->
             .     .                  .     .                  .     .                  .     .
          X  .  #  .  #            #  .     .               X  .     .                  .  #  .
        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .
             .     .               X  .     .                  .  #  .                  .  X  .
        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .
             .     .               #  .     .                  .     .  #               .  #  .
             .     .                  .     .                  .     .                  .     .
           (Exemplo 1)              (Exemplo 2)              (Exemplo 3)              (Exemplo 4)

         Nota-se que o que se busca com este algoritmo é verificar se nesta linha do tabuleiro tem-se
         a seguinte configuração:

             1. Em 1 casa tem o marcador passado.
             2. Em 2 casas estão vazias.

         Verificada esta condição, retorna as duas posições da linha que estão vazias.

         Executando o algoritmo para os exemplos mostrados, teríamos o seguinte resultado para X no
         retorno do algoritmo:

         Exemplo 1 (HORIZONTAL_1): [0,1] [0,2]
         Exemplo 2 (VERTICAL_1)  : [0,0] [2,0]
         Exemplo 3 (DIAGONAL_1)  : [1,1] [2,2]
         Exemplo 4 (VERTICAL_2)  : [0,1] [2,1]
         */
        val emptyPositions: MutableList<CellPosition> = mutableListOf()
        var labelCounter = 0
        var emptyCounter = 0
        //Conta quantos marcadores e vazios a linha contém.
        for (position in boardLine) {
            if (board[position.line, position.column] != Board.EMPTY) {
                if (board[position.line, position.column] == label) {
                    labelCounter++
                }
            } else {
                emptyCounter++
            }
        }
        //Havendo 1 marcador e 2 vazios, localiza os vazios e retorna.
        if (labelCounter == 1 && emptyCounter == 2) {
            for (position in boardLine) {
                if (board[position.line, position.column] == Board.EMPTY) {
                    emptyPositions.add(position)
                }
            }
        }
        return emptyPositions
    }

    /**
     * Retornar uma lista com todas as posições vazias do tabuleiro que, se marcadas, levam o jogador
     * com aquele marcador a uma condição de vitória a seu favor. Uma condição de vitória é aquela em
     * que se cria simultâneamente duas formações com 2 marcadores numa casa e uma casa vazia. Desta
     * forma, mesmo que uma formação seja "desarmada", a outra permite a vitória do jogador.
     * @param board tabuleiro do jogo.
     * @param label marcador de referência.
     * @return lista com as casas vazias que atendam ao critério, ou lista vazia, caso nenhuma casa
     * atenda à condição.
     */
    private fun getEmptyPositionsThatLeadToAVictory(board: Board, label: Byte): MutableList<CellPosition> {
        /*
        A ideia aqui é localizar este tipo de formação no tabuleiro, considerando-se o marcador X como
        exemplo, marcador O como sendo do adversário e denotando as casas vazias a retornar pelo
        algoritmo por #:

             .     .                  .     .                  .     .                  .     .
          X  .  O  .               X  .  X  .  O               .     .  O               .  O  .  X
        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .
          #  .  X  .               O  .  #  .               #  .  X  .                  .  #  .  X
        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .        . . . . . . . . .
          #  .     .  O               .     .               X  .  O  .                  .     .  O
             .     .                  .     .                  .     .                  .     .
           (Exemplo 1)              (Exemplo 2)              (Exemplo 3)              (Exemplo 4)

         Considera-se que a vez de marcar o tabuleiro será de quem tem o marcador X. Analisemos então
         o caso do Exemplo 1:

             .     .             -------------------------------------------------------------------
          X  .  O  .             A configuração do tabuleiro quando o jogador com marcador X vai
        . . . . . . . . .        jogar é esta. O que o algoritmo verificará é se há alguma casa
             .  X  .             vazia que se marcada leva a formar simultâneamente uma configuração
        . . . . . . . . .        tal que se tenha 2 casas com marcadores X e 1 vazia em duas linhas.
             .     .  O          No exemplo, há duas casas vazias que atendem à este critério. São
             .     .             elas:

             .     .             -------------------------------------------------------------------
          X  .  O  .             [1,0]: Se marcar a casa [1,0], na linha HORIZONTAL_2 temos 2 casas
        . . . . . . . . .        com X, [1,0] e [1,1], e 1 casa vazia [1,2]. Ao mesmo tempo, teremos
         (X) .  X  .  #          outra formação com 2 casas com o marcador X e 1 casa vazia. É na
        . . . . . . . . .        linha VERTICAL_1, com marcadores nas casas [0,0] e [1,0] e vazio na
          #  .     .  O          casa [2,0]. No turno do jogador com O, se ele "desarmar" em [1,2]
             .     .             o com X pode vencer marcando em [2,0] e vice-versa.

             .     .             -------------------------------------------------------------------
          X  .  O  . #           [2,0]: Se marcar a casa [2,0], na linha VERTICAL_1 temos 2 casas
        . . . . . . . . .        com X, [0,0] e [2,0], e 1 casa vazia [1,0]. Ao mesmo tempo, teremos
          #  .  X  .             outra formação com 2 casas com o marcador X e 1 casa vazia. É na
        . . . . . . . . .        linha DIAGONAL_2, com marcadores nas casas [1,1] e [2,0] e vazio na
         (X) .     .  O          casa [0,2]. No turno do jogador com O, se ele "desarmar" em [0,2]
             .     .             o com X pode vencer marcando em [1,0] e vice-versa.

         Executando o algoritmo para os exemplos mostrados, teríamos o seguinte resultado para X no
         retorno do algoritmo (aonde vazio está marcado com #):

         Exemplo 1: [1,0] [2,0]
         Exemplo 2: [1,1]
         Exemplo 3: [1,0]
         Exemplo 4: [1,1]
         */
        val emptyPositions: MutableList<CellPosition> = mutableListOf()
        val auxiliaryList: MutableList<CellPosition> = mutableListOf()
        for (boardLine in boardLines) {
            val positions: List<CellPosition> = getEmptyPositionsInLineWithASingleLabel(
                board,
                boardLine,
                label
            )
            for (position in positions) {
                if (auxiliaryList.contains(position)) {
                    if (!emptyPositions.contains(position)) {
                        emptyPositions.add(position)
                    }
                } else {
                    auxiliaryList.add(position)
                }
            }
        }
        return emptyPositions
    }

    /**
     * Verificar se retorna mais de uma casa vazia que atenda à condição de [getEmptyPositionsThatCompleteALine].
     * Havendo, o jogador com marcador [label] pode vencer.
     * @param board tabuleiro do jogo.
     * @param label marcador de referência.
     * @return true, se existe mais de uma casa vazia que atenda à condição de [getEmptyPositionsThatCompleteALine],
     * false, se não existe.
     */
    private fun leadsToAWinCondition(board: Board, label: Byte): Boolean {
        val emptyPositions: List<CellPosition> = getEmptyPositionsThatCompleteALine(board, label)
        return (emptyPositions.size > 1)
    }

    /**
     * Obter o marcador do oponente do jogador com o marcador passado.
     * @param label marcador do jogador.
     * @return marcador do jogador adversário.
     */
    private fun getOponnentLabel(label: Byte): Byte = when (label) {
        this.label -> this.opponentLabel
        else -> this.label
    }

    /**
     * Simular uma partida. A simulação termina quando uma das seguintes condições for verificada:[<br><br>]
     * [<ol>]
     * [<li>]Há uma condição de vitória em favor do jogador com o marcador [label]. [</li>] [<br><br>]
     * [<li>]Não há mais posicões que obriguem o adversário a desarmar ou que possam lhe
     * dar uma vitória contra o jogador no próximo turno.[</li>]
     * [</ol>]
     * A instancia do tabuleiro no parâmetro é alterada ao longo das recursões do método.
     * @param board tabuleiro em que o jogo será simulado.
     * @param position posição vazia a ser marcada no tabuleiro.
     */
    private fun simulateMatch(board: Board, position: CellPosition, label: Byte): Byte {
        //Marca a posição vazia com o marcador no parâmetro...
        board[position.line, position.column] = label
        //Verifica se levou a uma condição de vitória para o jogador com o marcador
        //no parâmetro ao marcá-la...
        if (!leadsToAWinCondition(board, label)) {
            //Se não levou a uma condição de vitória em favor do jogador com o
            //marcador definido, é momento de trocar o contexto para
            //simular a jogada do adversário deste jogador.
            val opponentLabel: Byte = getOponnentLabel(label)
            var positions = getEmptyPositionsThatCompleteALine(board, label)
            //Verifica qual(is) posição(ões) completa(m) uma linha do tabuleiro a favor
            //do jogador com o marcador definido. Essas posiçãos serão marcadas
            //considerando-se que o adversário vai desarmá-las.
            if (positions.isNotEmpty()) {
                //Usa a recursividade do método para simular a ação do
                //adversário de desarmar esta linha...
                val newPosition: CellPosition = getRandomPosition(positions)
                return simulateMatch(board, newPosition, opponentLabel)
            }
            //Não tendo uma linha do tabuleiro que o jogador vai desarmar...
            //Verifica se o adversário têm a possibilidade de criar uma
            //condição de vitória a seu favor, e tendo, simula a marcação
            //desta posição.
            positions = getEmptyPositionsThatLeadToAVictory(board, opponentLabel)
            if (positions.isNotEmpty()) {
                val newPosition: CellPosition = getRandomPosition(positions)
                return simulateMatch(board, newPosition, opponentLabel)
            }
        }
        return label
    }

    /**
     * Retornar uma lista com todas as posições vazias do tabuleiro que levam à condição definida se
     * o tabuleiro receber o marcador.[<br><br>]
     * As condições são as seguintes: [<br><br>]
     * 'V': retorna todas as casas vazias que se marcadas com o marcador, levam o jogador com o mesmo
     * à vitória inevitável nos próximos turnos.[<br><br>]
     * 'D': retorna todas as casas vazias que se marcadas com o marcador, levam o jogador com o mesmo
     * à derrota inevitável para o seu adversário nos próximos turnos.[<br><br>]
     * Se não houver alguma posição que leve à condição definida, retorna uma lista vazia.
     * @param board tabuleiro do jogo.
     * @param label marcador de referência.
     * @param condition condição de análise.
     * @return lista com as casas vazias que atendam ao critério, ou lista vazia, caso nenhuma casa
     * atenda à condição.
     */
    private fun getEmptyPositionsOnCondition(board: Board, label: Byte, condition: Char): MutableList<CellPosition> {
        //Obtém todas as posições vazias do tabuleiro, que, se marcadas pelo
        //jogador com o marcador passado vão forçar o jogador adversário a
        //desarmar.
        val positionsThatForceToDisarm: MutableList<CellPosition> = mutableListOf()
        for (boardLine in boardLines) {
            val emptyPositions: List<CellPosition> = getEmptyPositionsInLineWithASingleLabel(
                board,
                boardLine,
                label
            )
            for (position in emptyPositions) {
                if (!positionsThatForceToDisarm.contains(position)) {
                    positionsThatForceToDisarm.add(position)
                }
            }
        }
        val opponentLabel: Byte = getOponnentLabel(label)
        //Simula o adversário desarmando em cada uma das posições.
        val emptyPositions: MutableList<CellPosition> = mutableListOf()
        for (position in positionsThatForceToDisarm) {
            val boardClone: Board = board.clone()
            simulateMatch(boardClone, position.clone(), label)
            when (condition) {
                'V' -> {
                    //A sequência da simulação leva o jogador à vitória.
                    if (leadsToAWinCondition(boardClone, label)) {
                        emptyPositions.add(position)
                    }
                }
                'D' -> {
                    //A sequência da simulação leva o jogador à derrota.
                    if (leadsToAWinCondition(boardClone, opponentLabel)) {
                        emptyPositions.add(position)
                    }
                }
            }
        }
        return emptyPositions
    }

    /**
     * Verificar se com a marcação da posição definida do tabuleiro, o jogador com o marcador passado
     * acaba numa situação de derrota inevitável para o seu adversário.
     * @param board tabuleiro do jogo.
     * @param position posição a ser marcada.
     * @param label marcador de referência.
     * @return true, se a ação levará o jogador a uma condição de derrota, false, caso não exista
     * este risco.
     */
    private fun markingThePositionLeadsToDefeat(board: Board, position: CellPosition, label: Byte): Boolean {
        //Obtém uma cópia do tabuleiro original...
        val boardClone: Board = board.clone()
        //Identifica o jogador do adversário, no contexto do método...
        val opponentLabel: Byte = getOponnentLabel(label)
        //Simula a marcação da posição definida...
        val lastToMark: Byte = simulateMatch(boardClone, position, label)
        //Se na sequência da simulação houver uma condição de vitória...
        if (leadsToAWinCondition(boardClone, lastToMark)) {
            //Se for a favor do jogador adversário a posição oferece risco,
            //do contrário ela não oferece.
            return (lastToMark == this.opponentLabel)
        } else {
            //Na primeira simulação não houve uma condição de vitória...
            val emptyPositions: List<CellPosition> = getAllEmptyPositions(boardClone)
            if (emptyPositions.isNotEmpty()) {
                if (lastToMark == label) {
                    //Não houve uma condição de vitória, mas há posições vazias e
                    //parou na vez do adversário marcar...
                    for (emptyPosition in emptyPositions) {
                        //Verifica todas as posições vazias nesta cópia do tabuleiro
                        //original, que passou pelo processo de simulação, e foi
                        //alterada, e simula novamente, com o marcador do adversário.
                        //Caso na sequência haja uma condição de vitória a favor do
                        //adversário, então a marcação da posição leva o jogador à
                        //derrota para o adversário.
                        val boardClone2: Board = boardClone.clone()
                        simulateMatch(boardClone2, emptyPosition, opponentLabel)
                        if (leadsToAWinCondition(boardClone2, opponentLabel)) {
                            return true
                        }
                    }
                    return false
                } else {
                    //Não houve uma condição de vitória na primeira simulação e parou na vez
                    //do jogador marcar. A única forma de avaliar se a posição em questão
                    //oferece risco é se com a marcação de qualquer das posições vazias
                    //restantes leve a uma condição de vitória a favor do seu adversário.
                    var winningCounter = 0
                    for (emptyPosition in emptyPositions) {
                        val boardClone2: Board = boardClone.clone()
                        simulateMatch(boardClone2, emptyPosition, label)
                        if (leadsToAWinCondition(boardClone2, opponentLabel)) {
                            ++winningCounter
                        }
                    }
                    return (winningCounter == emptyPositions.size)
                }
            } else {
                return false
            }
        }
    }

    /**
     * Retorna uma lista com todas as posições vazias do tabuleiro que são seguras de serem marcadas.
     * Uma posição é segura se marcada pelo jogador, não possibilita na sequência a vitória do seu
     * adversário (derrota inevitável). Se não houver posições vazias nesta condição, retorna uma
     * lista vazia.
     * @param board tabuleiro do jogo.
     * @param label marcador de referência.
     * @return lista com as casas vazias que atendam ao critério, ou lista vazia, caso nenhuma casa
     * atenda à condição.
     */
    private fun getSafeEmptyPositionsToMark(board: Board, label: Byte): MutableList<CellPosition> {
        val emptyPositions: List<CellPosition> = getAllEmptyPositions(board)
        val safePositions: MutableList<CellPosition> = mutableListOf()
        for (position in emptyPositions) {
            if (!markingThePositionLeadsToDefeat(board, position, label)) {
                safePositions.add(position)
            }
        }
        return safePositions
    }

    /**
     * Modificar o nível de dificuldade oferecido durante a partida.
     * @param difficultyLevel novo nível de dificuldade.
     */
    fun changeDifficultyLevel(difficultyLevel: DifficultyLevel) {
        this.difficultyLevel = difficultyLevel
    }

}