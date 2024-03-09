package ru.ok.itmo.hw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class FragmentAViewModel(value : Int?) : ViewModel() {
    private var _uiStateLiveData = MutableLiveData<Int>()
    val uiStateLiveData : LiveData<Int>
        get() = _uiStateLiveData

    init {
        _uiStateLiveData.value = value ?: Random.nextInt(10)
    }

}