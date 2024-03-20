package games.utils

import android.R
import android.app.Activity
import android.view.WindowManager
import androidx.core.content.ContextCompat

/**
 * Troca a cor da barra de status do sistema android de acordo com a activity selecionada.
 * @param activity activity selecionada.
 */
fun setDefaultStatusBarTheme(activity: Activity) {
    with (activity.window) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = ContextCompat.getColor(activity, R.color.white)
    }
}