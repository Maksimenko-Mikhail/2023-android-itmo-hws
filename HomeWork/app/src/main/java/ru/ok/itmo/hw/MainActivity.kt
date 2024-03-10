package ru.ok.itmo.hw

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var nightTheme = false
    fun changeTheme() {
        if (nightTheme) setTheme(android.R.style.ThemeOverlay_Material_Dark)
        else setTheme(android.R.style.ThemeOverlay_Material_Light)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        nightTheme = sharedPref.getBoolean("night_theme",
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                resources.configuration.isNightModeActive
            else false)
        changeTheme()
        super.onCreate(savedInstanceState)


        val login : EditText = findViewById(R.id.et2)
        val password : EditText = findViewById(R.id.et3)
        val enter : Button = findViewById(R.id.btn1)
        val themeBtn : Button = findViewById(R.id.btn2)


        enter.setOnClickListener {

            val log = login.text.toString()
            val passwrd = password.text.toString()
            if (log.isEmpty()) login.setError("login is empty")
            else if (log.length < 9 || !log.substring(log.length - 8).equals("@mail.ru")) {
                login.error = "login should end with @mail.ru"
            }

            else if (passwrd.length < 6) {
                password.setText("")
                password.error = "password is too short: 6 symbols required"
            } else if (passwrd.equals("qwerty") || passwrd.equals("123456")) {
                password.setText("")
                password.error = "password is too easy"
            }
        }
        password.setOnKeyListener(
            View.OnKeyListener {
                    v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    enter.performClick()
                    return@OnKeyListener true
                }
                false
            }
        )
        themeBtn.setOnClickListener {
            nightTheme = !nightTheme
            with(sharedPref.edit()) {
                putBoolean("night_theme", nightTheme)
                apply()
            }
            changeTheme()
            recreate()
        }


    }
}