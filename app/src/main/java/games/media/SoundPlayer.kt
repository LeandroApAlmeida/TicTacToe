package games.media

import android.content.Context
import android.media.MediaPlayer
import games.tictactoe.R

/**
 * Tocador de sons no aplicativo.
 */
class SoundPlayer(val context: Context) {

    /**
     * Executar uma trilha sonora que está inserida nos resources do aplicativo.
     * @param rid identificador da trilha sonora nos resources.
     */
    fun execute(rid: Int) {
        when (rid) {
            R.raw.sound1 -> {
                val player: MediaPlayer = MediaPlayer.create(context, R.raw.sound1)
                player.isLooping = false
                player.start()

            }
            R.raw.sound2 -> {
                val player: MediaPlayer = MediaPlayer.create(context, R.raw.sound2)
                player.isLooping = false
                player.start()
            }
            R.raw.sound3 -> {
                val player: MediaPlayer = MediaPlayer.create(context, R.raw.sound3)
                player.isLooping = false
                player.start()
            }
        }
    }

}