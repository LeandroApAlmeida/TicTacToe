package games.launcher

import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import games.hanoi.HanoiTowerActivity
import games.tictactoe.R
import games.tictactoe.TicTacToeActivity
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Activity principal. Contém a lista com todos os jogos que estão disponíveis no pacote.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar? = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_games)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = GameAdapter(this, getRecyclerViewItems())
    }

    private fun getRecyclerViewItems(): MutableList<GameItem> {
        val gameList: MutableList<GameItem> = mutableListOf()
        gameList.add(GameItem(
            R.drawable.tic_tac_toe_art,
            resources.getString(R.string.game_item_title_tictactoe),
            resources.getString(R.string.game_item_description_tictactoe),
            ::itemClick
        ))
        gameList.add(GameItem(
            R.drawable.hanoi_tower_art,
            resources.getString(R.string.game_item_title_hanoi_tower),
            resources.getString(R.string.game_item_description_hanoi_tower),
            ::itemClick
        ))
        gameList.add(GameItem(
            R.drawable.hangman_art,
            resources.getString(R.string.game_item_title_hangman),
            resources.getString(R.string.game_item_description_hangman),
            ::itemClick
        ))
        gameList.add(GameItem(
            R.drawable.maze_game_art,
            resources.getString(R.string.game_item_title_maze_game),
            resources.getString(R.string.game_item_description_maze_game),
            ::itemClick
        ))
        gameList.add(GameItem(
            R.drawable.memory_game_art,
            resources.getString(R.string.game_item_title_memory_game),
            resources.getString(R.string.game_item_description_memory_game),
            ::itemClick
        ))
        gameList.add(GameItem(
            R.drawable.hidden_suspect_art,
            resources.getString(R.string.game_item_title_hidden_suspect),
            resources.getString(R.string.game_item_description_hidden_suspect),
            ::itemClick
        ))
        return gameList
    }

    private fun itemClick(position: Int): Unit {
        when (position) {
            0 -> startActivity(Intent(this, TicTacToeActivity::class.java))
            1 -> startActivity(Intent(this, HanoiTowerActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_about -> {
            showAboutDialog()
            true
        }
        R.id.menu_tutorial -> {
            //showTutorial()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutDialog() {
        val text: TextView = TextView(this)
        text.setTextColor(Color.BLUE)
        val sb: StringBuilder = StringBuilder()
        sb.append("\n")
        sb.append(resources.getString(R.string.app_name).toUpperCase())
        sb.append(" ")
        sb.append(resources.getString(R.string.about_dialog_version_number))
        sb.append("\n\n")
        sb.append(resources.getString(R.string.about_dialog_developer))
        sb.append("\n")
        sb.append(resources.getString(R.string.about_dialog_version_date))
        MaterialAlertDialogBuilder(this)
        .setTitle(resources.getString(R.string.toolbar_menu_about))
        .setMessage(sb.toString())
        .setPositiveButton("OK", null)
        .show()
    }

    private fun showTutorial() {
        val fileName: String = "Projeto.pdf"
        val assetManager: AssetManager = assets
        var istream: InputStream?
        var ostream: OutputStream?
        val file: File = File(filesDir, "")
        try {
            //istream = assetManager.open(fileName)
            istream = getResources().openRawResource(R.raw.projeto)
            //val dest = Environment.getExternalStorageDirectory().absolutePath + "/" + fileName
            ostream = openFileOutput(fileName, MODE_PRIVATE)
            var buffer = ByteArray(1024)
            var read: Int
            do {
                read = istream.read(buffer)
                if (read != -1) {
                    ostream.write(buffer, 0, read)
                }
            } while (read != -1)
            istream.close()
            ostream.flush()
            ostream.close()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse("file://$filesDir/$fileName"), "application/pdf")
            startActivity(intent)
            intent.type = "application/pdf"
            val path: File = File(Environment.getExternalStorageDirectory().absolutePath, fileName)
            val uri: Uri = Uri.fromFile(path)
            intent.putExtra(Intent.ACTION_ALL_APPS, uri)
            startActivity(intent)
        } catch (ex: Exception) {
            Snackbar.make(
                findViewById(R.id.recycler_view_games),
                ex.toString(),
                5000
            ).show()
        }
    }

}