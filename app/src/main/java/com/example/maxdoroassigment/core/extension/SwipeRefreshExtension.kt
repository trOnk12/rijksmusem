package com.example.maxdoroassigment.core.extension

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun SwipeRefreshLayout.actionOnRefresh(action : () -> Unit){
        setOnRefreshListener {
            action()
            isRefreshing = false
        }
}