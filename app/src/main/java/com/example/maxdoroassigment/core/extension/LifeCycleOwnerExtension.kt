package com.example.maxdoroassigment.core.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, onChanged: (T) -> Unit) {
    liveData.observe(this) { onChanged(it) }
}