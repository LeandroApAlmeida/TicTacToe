package games.launcher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import games.tictactoe.R

class GameAdapter(private val context: Context, private val gamesList: MutableList<GameItem>): RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    inner class GameViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.game_image)
        val name: TextView = itemView.findViewById(R.id.game_name)
        val description: TextView = itemView.findViewById(R.id.game_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val gameItem = LayoutInflater.from(context).inflate(R.layout.activity_game_item, parent, false)
        return GameViewHolder(gameItem)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.image.setImageResource(gamesList[position].rid)
        holder.name.text = gamesList[position].name
        holder.description.text = gamesList[position].description
        holder.itemView.setOnClickListener{
            gamesList[position].onClick.invoke(position)
        }
    }

    override fun getItemCount(): Int = gamesList.size

}