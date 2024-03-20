package games.launcher

/**
 * Classe que representa um item da lista de jogos na tela principal.
 * @param rid identificador da imagem do jogo nos resources do aplicativo.
 * @param name nome do jogo.
 * @param description descrição do jogo.
 * @param onClick função para tratar o evento de clique no item do jogo.
 */
data class GameItem (val rid: Int, val name: String, val description: String, val onClick: ((Int)->Unit))