package ru.ok.itmo.hw

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult

class MainNavigationFragment : Fragment(R.layout.main_fragment) {
    private var lastCopies = 1
    private var fragmentCounter : Int = 0
    private lateinit var lastName : String
    private lateinit var btnA : Button
    private lateinit var btnB : Button
    private lateinit var btnC : Button
    private var fragmentStack = mutableListOf<String>()
    private var eachFragmentCounter = HashMap<String, Int>()
    companion object {
        private const val FRAGMENT_ID = FragmentA.FRAGMENT_ID
        const val FRAGMENTS_COUNT = "fragments_count"
    }

    private fun addFragment(name : String) {
        lastName = name
        while (lastCopies > 1) {
            lastCopies -= 1
            requireActivity().supportFragmentManager.popBackStack()
        }
//        if (lastCopies > 1) {
//            requireActivity().supportFragmentManager.popBackStack(fragmentStack.last(), 1)
//            lastCopies = 1
//        }
//        lastCopies = 1
        assert(lastCopies == 1)
        eachFragmentCounter[name] = (eachFragmentCounter[name] ?: 0) + 1
        fragmentCounter += 1
        if (!fragmentStack.contains(name)) {
            Log.v("STATE", "here")
            requireActivity().supportFragmentManager.commit {
                val bundle = bundleOf(
                    FRAGMENT_ID to name,
                    FRAGMENTS_COUNT to eachFragmentCounter[name]
                )
                if (fragmentStack.isNotEmpty()) {
                    val prev = fragmentStack.last()
                    Log.v("STATE", prev)
                    val prevFragment = requireActivity().supportFragmentManager.findFragmentByTag(prev)!!
                    hide(prevFragment)
                }
                add(R.id.frame1, FragmentA::class.java, bundle, name)
//                addToBackStack(name)
            }
            fragmentStack.add(name)
            return
        }
        while (fragmentStack.last() != name) {

            val previousFragment =
                requireActivity().supportFragmentManager.findFragmentByTag(fragmentStack.last())!!
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(previousFragment)
                .commit()
            fragmentStack.removeLast()
        }
        val fragment = requireActivity().supportFragmentManager.findFragmentByTag(fragmentStack.last())!!
        (fragment as FragmentA).setCounter(eachFragmentCounter[name]!!)
        requireActivity().supportFragmentManager.beginTransaction()
            .show(fragment)
            .commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentCounter = requireArguments().getInt(FRAGMENTS_COUNT)
        lastCopies = 1
        assert(fragmentStack.isEmpty())
        assert(requireActivity().supportFragmentManager.backStackEntryCount == 2)
        addFragment("Fragment A")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
    }



    fun onBack() : Int {
        if (lastCopies > 1) {
            requireActivity().supportFragmentManager.popBackStack()
            lastCopies -= 1
            return fragmentStack.size + lastCopies
        }

        if (fragmentStack.isEmpty()) {
            return 0
        }
        if (fragmentStack.size == 1) {

            for (i in 1..<requireActivity().supportFragmentManager.backStackEntryCount) {
                requireActivity().supportFragmentManager.popBackStack()
            }
            setFragmentResult(FRAGMENTS_COUNT, bundleOf(FRAGMENTS_COUNT to fragmentCounter))

            requireActivity().supportFragmentManager.beginTransaction()
//                .remove(this)
                .remove(requireActivity().supportFragmentManager.findFragmentByTag(fragmentStack.last())!!)
                .replace(R.id.container, requireActivity().supportFragmentManager.findFragmentByTag(HOME_TAG)!!, HOME_TAG)
                .commit()


            fragmentStack.removeLast()
            return 1
        }
        val name = fragmentStack.last()
        fragmentStack.removeLast()
        val old = requireActivity().supportFragmentManager.findFragmentByTag(name)!!
        val new = requireActivity().supportFragmentManager.findFragmentByTag(
            fragmentStack.last()
        )!!
        lastCopies = 1
        lastName = fragmentStack.last()
        requireActivity().supportFragmentManager.beginTransaction()
            .show(new)
            .remove(old)
            .commit()
        return fragmentStack.size
    }

    fun addLastFragment() {
        lastCopies += 1
        lastName = fragmentStack.last()
        fragmentCounter += 1
        eachFragmentCounter[lastName] = eachFragmentCounter[lastName]!! + 1
        val bundle = bundleOf(FRAGMENT_ID to "$lastName~",
            FRAGMENTS_COUNT to eachFragmentCounter[lastName])
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame1, FragmentA::class.java, bundle, "$lastName~")
            .addToBackStack("$lastName~")
            .commit()

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