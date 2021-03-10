/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
