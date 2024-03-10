package ru.ok.itmo.hw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class FragmentA : Fragment() {
    private lateinit var nameTv: TextView
    private lateinit var counterTv : TextView
    private lateinit var numberTv : TextView

    private lateinit var fragmentName: String
    private var count = 0
    private var number : Int = 0
    private lateinit var addFragment: Button


    private lateinit var viewModel : FragmentAViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentName = requireArguments().getString(FRAGMENT_ID)!!
        count = requireArguments().getInt(MainNavigationFragment.FRAGMENTS_COUNT)
        viewModel = FragmentAViewModel(savedInstanceState?.getInt(NUMBER_TAG))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameTv = view.findViewById(R.id.tv1)
        counterTv = view.findViewById(R.id.fragment_number)
        addFragment = view.findViewById(R.id.new_fragment)
        numberTv = view.findViewById(R.id.generated_number)

        nameTv.text = fragmentName

        setCounter(count)

        addFragment.setOnClickListener {
            (requireActivity().supportFragmentManager.findFragmentByTag(NAVIGATION_TAG) as MainNavigationFragment).addLastFragment()
        }

        viewModel.uiStateLiveData.observe(viewLifecycleOwner) {
            number = it
            numberTv.text = it.toString()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(NUMBER_TAG, number)
        super.onSaveInstanceState(outState)
    }
    fun setCounter(newValue : Int) {
        count = newValue
        counterTv = requireActivity().findViewById(R.id.fragment_number)
        counterTv.text = "$fragmentName number is: $newValue"
    }

    fun getFragmentName() = fragmentName

    companion object {
        const val FRAGMENT_ID = "fragment_id"
        const val NUMBER_TAG = "number_tag"
    }
}