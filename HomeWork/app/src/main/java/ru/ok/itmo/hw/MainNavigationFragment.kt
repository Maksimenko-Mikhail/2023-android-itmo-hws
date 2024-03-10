package ru.ok.itmo.hw

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.Random

class MainNavigationFragment : Fragment(R.layout.main_fragment) {
    private var lastCopies = 1
    private var fragmentCounter : Int = 0
    private lateinit var lastName : String

    private var possibleFragmentsCount : Int = 0


    private val fragmentNames = mapOf(
        R.id.menu_item_a to "Fragment A",
        R.id.menu_item_b to "Fragment B",
        R.id.menu_item_c to "Fragment C",
        R.id.menu_item_d to "Fragment D",
        R.id.menu_item_e to "Fragment E"
    )

    private val fragmentMenu = mapOf(
        "Fragment A" to R.id.menu_item_a,
        "Fragment B" to R.id.menu_item_b,
        "Fragment C" to R.id.menu_item_c,
        "Fragment D" to R.id.menu_item_d,
        "Fragment E" to R.id.menu_item_e,
    )


    private lateinit var menu : NavigationBarView

    private var fragmentStack = mutableListOf<String>()
    private var eachFragmentCounter = HashMap<String, Int>()
    companion object {
        private const val FRAGMENT_ID = FragmentA.FRAGMENT_ID
        private const val EACH_FRAGMENT_COUNTER = "each fragment counter"
        private const val FRAGMENT_STACK = "fragment stack"
        private const val LAST_COPIES = "last copies"
        private const val FRAGMENT_COUNTER = "fragment counter"
        private const val POSSIBLE_FRAGMENTS_COUNT = "possible_fragments_count"
        private const val MIN_FRAGMENTS = 3
        private const val MAX_FRAGMENTS = 5
        const val FRAGMENTS_COUNT = "fragments_count"
    }

    private fun addFragment(name : String) {
        lastName = name
        while (lastCopies > 1) {
            lastCopies -= 1
            requireActivity().supportFragmentManager.popBackStack()
        }
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
        if (savedInstanceState != null) {
            val gson = Gson()
            val hashMapToken = savedInstanceState.getString(EACH_FRAGMENT_COUNTER)
            val fragmentStackToken = savedInstanceState.getString(FRAGMENT_STACK)

            var type = object : TypeToken<HashMap<String, Int>>() {}.type
            eachFragmentCounter = gson.fromJson(hashMapToken, type)
            type = object : TypeToken<MutableList<String>>() {}.type
            fragmentStack = gson.fromJson(fragmentStackToken, type)

            fragmentCounter = savedInstanceState.getInt(FRAGMENT_COUNTER)
            lastCopies = savedInstanceState.getInt(LAST_COPIES)
            possibleFragmentsCount = savedInstanceState.getInt(POSSIBLE_FRAGMENTS_COUNT)
            return
        }
        fragmentCounter = requireArguments().getInt(FRAGMENTS_COUNT)
        lastCopies = 1
        possibleFragmentsCount = Random.nextInt(MIN_FRAGMENTS, MAX_FRAGMENTS + 1)
        addFragment("Fragment A")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val gson = Gson()
        var type = object : TypeToken<HashMap<String, Int>>() {}.type
        val eachFragmentCounterJson = gson.toJson(eachFragmentCounter, type)
        type = object : TypeToken<MutableList<String>>() {}.type
        val fragmentStackJson = gson.toJson(fragmentStack, type)
        outState.putString(EACH_FRAGMENT_COUNTER, eachFragmentCounterJson)
        outState.putString(FRAGMENT_STACK, fragmentStackJson)

        outState.putInt(LAST_COPIES, lastCopies)
        outState.putInt(FRAGMENT_COUNTER, fragmentCounter)
        outState.putInt(POSSIBLE_FRAGMENTS_COUNT, possibleFragmentsCount)
        super.onSaveInstanceState(outState)
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
        menu.selectedItemId = fragmentMenu[lastName]!!
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
        menu = view.findViewById(R.id.menu_bar)

        var cnt = 0
        for (i in fragmentNames.keys) {
            cnt++
            if (cnt > possibleFragmentsCount) {
                menu.menu.removeItem(i)
            }
        }
        menu.setOnItemSelectedListener {
            val name = fragmentNames[it.itemId] ?: return@setOnItemSelectedListener false
            addFragment(name)
            return@setOnItemSelectedListener true
        }

    }
}