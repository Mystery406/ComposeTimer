package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException

@ObsoleteCoroutinesApi
class MainViewModel : ViewModel() {

    private val _seconds = MutableLiveData(0)
    val seconds: LiveData<Int>
        get() = _seconds

    private var tickerChannel: ReceiveChannel<Unit> = ticker(1000, 1000)

    fun startCountdown(startSeconds: Int) {
        tickerChannel.cancel(CancellationException("Restart Countdown!"))
        tickerChannel = ticker(1000, 1000)
        _seconds.value = startSeconds
        viewModelScope.launch {
            for (event in tickerChannel) {
                if ((_seconds.value ?: 0) > 0) {
                    _seconds.postValue(_seconds.value?.minus(1))
                } else {
                    tickerChannel.cancel(CancellationException("Countdown to zero!"))
                }
            }
        }
    }
}