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
@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            val mainViewModel: MainViewModel = viewModel()
            val initialSeconds = 5
            var targetSeconds by remember { mutableStateOf("") }
            val seconds: State<Int> = mainViewModel.seconds.observeAsState(initialSeconds)
            OutlinedTextField(
                modifier = Modifier.padding(8.dp),
                value = targetSeconds,
                onValueChange = { newValue ->
                    targetSeconds = if (newValue.isEmpty()) {
                        ""
                    } else {
                        newValue
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(
                modifier = Modifier.padding(12.dp),
                text = secondsToString(seconds.value),
                fontSize = 60.sp,
            )

            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    if (targetSeconds.isNotEmpty() && targetSeconds.toInt() > 0) {
                        mainViewModel.startCountdown(targetSeconds.toInt())
                    }
                }
            ) {
                Text(text = " Go! ", fontSize = 16.sp)
            }
        }
    }
}

fun secondsToString(value: Int): String {
    val minutes = value / 60
    val seconds = value % 60
    return buildString {
        if (minutes < 10) {
            append("0")
        }
        append(minutes)
        append(":")
        if (seconds < 10) {
            append("0")
        }
        append(seconds)
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
