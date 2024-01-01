package com.example.carbuddy.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withMainContext(block: suspend () -> T): T {
    return withContext(Dispatchers.Main) {
        block.invoke()
    }
}

suspend fun <T> withIoContext(block: suspend () -> T): T {
    return withContext(Dispatchers.IO) {
        block.invoke()
    }
}
