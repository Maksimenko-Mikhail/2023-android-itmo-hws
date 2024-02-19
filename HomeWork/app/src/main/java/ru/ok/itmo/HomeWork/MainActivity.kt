package ru.ok.itmo.HomeWork

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.ok.itmo.HomeWork.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Log.d("notify permission: ", "result: $it")
        }
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, MainFragment())
                .commit()
        }
    }
}