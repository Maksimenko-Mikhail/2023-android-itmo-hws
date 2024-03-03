package ru.ok.itmo.hw

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class StartFragment : Fragment(R.layout.start_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.start_btn).setOnClickListener {
            val fragment = MainNavigationFragment()
            requireActivity().supportFragmentManager.beginTransaction()
//                .setPrimaryNavigationFragment(fragment)
                .replace(R.id.container, fragment, NAVIGATION_TAG)
                .addToBackStack(NAVIGATION_TAG)
                .commit()
        }
    }
}