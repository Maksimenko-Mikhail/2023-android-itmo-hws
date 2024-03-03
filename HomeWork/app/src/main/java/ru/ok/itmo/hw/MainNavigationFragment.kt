package ru.ok.itmo.hw

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.commit

class MainNavigationFragment : Fragment(R.layout.main_fragment) {

    private lateinit var btnA : Button
    private lateinit var btnB : Button
    private lateinit var btnC : Button
    private var fragmentStack = mutableListOf<CharSequence>()
    companion object {
        private const val FRAGMENT_ID = "fragment_id"
    }

    private fun addFragment(name : CharSequence) {

        if (!fragmentStack.contains(name)) {
            requireActivity().supportFragmentManager.commit {
                val bundle = Bundle().apply { putCharSequence(FRAGMENT_ID, name) }
                if (fragmentStack.isNotEmpty()) {
                    val prev = fragmentStack.last()
                    val prevFragment = requireActivity().supportFragmentManager.findFragmentByTag(prev.toString())!!
                    hide(prevFragment)
                }
                add(R.id.frame1, FragmentA::class.java, bundle, name.toString())
                addToBackStack(name.toString())
            }
            fragmentStack.add(name)
            return
        }
        while (fragmentStack.last() != name) {

            val previousFragment =
                requireActivity().supportFragmentManager.findFragmentByTag(fragmentStack.last().toString())!!
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(previousFragment)
                .commit()
            fragmentStack.removeLast()
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .show(requireActivity().supportFragmentManager
                .findFragmentByTag(fragmentStack.last().toString())!!)
            .commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment("Fragment A")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun onBack() : Int {
        if (fragmentStack.isEmpty()) {
            return 0
        }
        if (fragmentStack.size == 1) {
//            requireActivity().supportFragmentManager.popBackStack(HOME_TAG, POP_BACK_STACK_INCLUSIVE)
            fragmentStack.removeLast()
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this)
                .replace(R.id.container, StartFragment())
                .commit()
            return 1
        }
        val name = fragmentStack.last().toString()
        fragmentStack.removeLast()
        val old = requireActivity().supportFragmentManager.findFragmentByTag(name)!!
        val new = requireActivity().supportFragmentManager.findFragmentByTag(
            fragmentStack.last().toString()
        )!!
        requireActivity().supportFragmentManager.beginTransaction()
            .show(new)
            .remove(old)
            .commit()
        return fragmentStack.size
    }

    private fun initView(view : View) {
        btnA = view.findViewById(R.id.btn1)
        btnB = view.findViewById(R.id.btn2)
        btnC = view.findViewById(R.id.btn3)

        btnA.setOnClickListener {
            addFragment("Fragment A")
        }
        btnB.setOnClickListener {
            addFragment("Fragment B")
        }
        btnC.setOnClickListener {
            addFragment("Fragment C")
        }

    }




}