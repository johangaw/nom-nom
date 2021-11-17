package com.example.nomnom.services

import kotlinx.coroutines.CancellationException

fun <T> Result<T>.throwOnCancellation(): Result<T> {
    return onFailure { if(it is CancellationException) throw it else it }
}