package ru.ok.itmo.HomeWork

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {


    private val _uiStateLiveData = MutableLiveData<TimerUiState>()
    val uiStateLiveData: LiveData<TimerUiState>
        get() = _uiStateLiveData

    class Factory internal constructor() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel() as T
        }
    }


    private fun runThreads(period: Int) {
        val t1 = Thread {
            var time: Int = 0
            while (time <= 100) {

                Thread.sleep(period.toLong())
                viewModelScope.launch { handleResult(time) }
                time += 10
            }
        }
        t1.start()
    }

    private fun runJavaRx(period: Int) {
        var time = 0
        val observable = Observable
            .interval((period).toLong(), TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .takeWhile { time <= 100 }
            .subscribe {
                time += 10
                viewModelScope.launch { handleResult(time) }
            }
    }

    fun countdown(mode : Int, period : Int) {
        if (mode == 0) {
            runThreads(period)
        } else {
            runJavaRx(period)
        }
    }

    fun makeAlarm(context : Context, timeInMillis : Long) {
        viewModelScope.launch {
            AlarmFlow.scheduleAlarmWork(context, timeInMillis)
        }
    }
    private fun timerFlow() : Flow<Int> {

        val fl: Flow<Int> = flow {
            var time: Int = 0
            while (time <= 100) {
                delay(100)
                emit(time)
                time += 10
            }
        }
        return fl
    }
    private fun runFlow() {
        viewModelScope.launch {
            timerFlow()
                .flowOn(Dispatchers.IO)
                .collect { handleResult(it) }

        }
    }

    private fun runCoroutine() {
        viewModelScope
            .launch {
                var time: Int = 0
                while (time <= 100) {
                    withContext(Dispatchers.IO) {
                        delay(100)
                    }
                    handleResult(time)
                    time += 10
                }
            }
    }

    private fun handleResult(state : Int) {
        _uiStateLiveData.value = TimerUiState.Data(state)
    }

}