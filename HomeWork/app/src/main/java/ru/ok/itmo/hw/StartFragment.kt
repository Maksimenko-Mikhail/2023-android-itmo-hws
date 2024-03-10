package ru.ok.itmo.hw

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener

class StartFragment : Fragment(R.layout.start_fragment) {


    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(MainNavigationFragment.FRAGMENTS_COUNT) {
                requestKey, bundle -> count = bundle.getInt(requestKey)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.fragment_counter).text = "total number of fragments is $count"
        view.findViewById<Button>(R.id.start_btn).setOnClickListener {
            val bundle = bundleOf(MainNavigationFragment.FRAGMENTS_COUNT to count)
            val fragment = MainNavigationFragment()
            requireActivity().supportFragmentManager.beginTransaction()

                .replace(R.id.container, fragment::class.java, bundle, NAVIGATION_TAG)
                .addToBackStack(NAVIGATION_TAG)
                .commit()
        }
    }
}