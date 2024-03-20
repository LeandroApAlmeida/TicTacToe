package games.preferences

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

/**
 * Configurações do aplicativo.
 * @param activity activity relacionada.
 */
class Settings(private val activity: Activity) {

    /**Leitor das configurações.*/
    private val prefs: SharedPreferences = activity.getPreferences(AppCompatActivity.MODE_PRIVATE)
    /**Editor das configurações.*/
    private val editor: SharedPreferences.Editor = activity.getPreferences(AppCompatActivity.MODE_PRIVATE).edit()

    /**
     * Obter um valor Int de configuração.
     * @param key chave da configuração.
     * @param default valor default.
     * @return valor Int de configuração.
     */
    fun getInt(key: String, default: Int): Int = prefs.getInt(key, default)

    /**
     * Obter um valor String de configuração.
     * @param key chave da configuração.
     * @param default valor default.
     * @return valor String de configuração.
     */
    fun getString(key: String, default: String): String? = prefs.getString(key, default)

    /**
     * Obter um valor Boolean de configuração.
     * @param key chave da configuração.
     * @param default valor default.
     * @return valor Boolean de configuração.
     */
    fun getBoolean(key: String, default: Boolean): Boolean = prefs.getBoolean(key, default)

    /**
     * Obter um valor Long de configuração.
     * @param key chave da configuração.
     * @param default valor default.
     * @return valor Long de configuração.
     */
    fun getLong(key: String, default: Long): Long = prefs.getLong(key, default)

    /**
     * Escrever um valor Int de configuração.
     * @param key chave de configuração.
     * @param value valor da configuração.
     * @return true, escrito com sucesso, false, não foi escrito.
     */
    fun setInt(key: String, value: Int): Boolean = with (editor) {
        putInt(key, value)
        commit()
    }

    /**
     * Escrever um valor String de configuração.
     * @param key chave de configuração.
     * @param value valor da configuração.
     * @return true, escrito com sucesso, false, não foi escrito.
     */
    fun setString(key: String, value: String): Boolean = with (editor) {
        putString(key, value)
        commit()
    }

    /**
     * Escrever um valor Boolean de configuração.
     * @param key chave de configuração.
     * @param value valor da configuração.
     * @return true, escrito com sucesso, false, não foi escrito.
     */
    fun setBoolean(key: String, value: Boolean): Boolean = with (editor) {
        putBoolean(key, value)
        commit()
    }

    /**
     * Escrever um valor Long de configuração.
     * @param key chave de configuração.
     * @param value valor da configuração.
     * @return true, escrito com sucesso, false, não foi escrito.
     */
    fun setLong(key: String, value: Long): Boolean = with (editor) {
        putLong(key, value)
        commit()
    }

}