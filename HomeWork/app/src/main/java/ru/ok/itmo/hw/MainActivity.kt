package ru.ok.itmo.hw

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import ru.ok.itmo.hw.R


const val NAVIGATION_TAG = "navigation"
const val HOME_TAG = "home"
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, StartFragment(), HOME_TAG)
                .addToBackStack(HOME_TAG)
                .commit()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        val fragment = supportFragmentManager.findFragmentByTag(NAVIGATION_TAG)
        if (fragment == null || (fragment as MainNavigationFragment).onBack() == 0) {
            finish()
        }
    }
}