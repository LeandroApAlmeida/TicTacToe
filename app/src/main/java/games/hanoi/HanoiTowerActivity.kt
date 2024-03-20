package games.hanoi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import games.tictactoe.R

class HanoiTowerActivity : AppCompatActivity() {

    private val numberDiscs: Array<Int> =  arrayOf(3,4,5,6)
    private lateinit var spnNumberDiscs: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hanoi_tower)
        spnNumberDiscs = findViewById<Spinner>(R.id.spnNumberDiscs)
        val arrayAdapter: ArrayAdapter<Int> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, numberDiscs)
        spnNumberDiscs.adapter = arrayAdapter
    }
}