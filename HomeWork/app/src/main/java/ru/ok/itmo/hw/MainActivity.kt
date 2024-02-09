package ru.ok.itmo.hw

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    fun changeTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val configuration = this.getResources().configuration;
            val currentNightMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_NO -> {setTheme(android.R.style.ThemeOverlay_Material_Light)} // Night mode is not active, we're using the light theme.
                Configuration.UI_MODE_NIGHT_YES -> {setTheme(android.R.style.ThemeOverlay_Material_Dark)} // Night mode is active, we're using dark theme.
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        changeTheme()
        super.onCreate(savedInstanceState)
        val login : EditText = findViewById(R.id.et2)
        val password : EditText = findViewById(R.id.et3)
        val enter : Button = findViewById(R.id.btn1)

        enter.setOnClickListener {

            val log = login.getText().toString()
            val passwrd = password.getText().toString()
            if (log.isEmpty()) login.setError("login is empty")
            else if (log.length < 9) login.setError("login is to short")
            else if (!log.substring(log.length - 8).equals("@mail.ru")) {
                login.setError("login should end with @mail.ru")
            }
            else if (passwrd.length < 6) {
                password.setText("")
                password.setError("password is too short: 6 symbols required")
            } else if (passwrd.equals("qwerty") || passwrd.equals("123456")) {
                password.setText("")
                password.setError("password is too easy")
            }
        }
        password.setOnKeyListener(
            View.OnKeyListener {
                    v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    val len = password.getText().toString().length
                    password.getText().delete(len - 1, len)
                    enter.performClick()
                    return@OnKeyListener true
                }
                false
            }
        )
    }
}