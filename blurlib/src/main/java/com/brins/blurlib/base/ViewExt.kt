package com.brins.blurlib.base

import android.app.Activity
import android.content.ContextWrapper
import android.view.View

fun View.getActivityDecorView(): View? {
    var ctx = getContext()
    var i = 0
    while (i < 4 && ctx != null && ctx !is Activity && ctx is ContextWrapper) {
        ctx = (ctx as ContextWrapper).baseContext
        i++
    }
    return if (ctx is Activity) {
        (ctx as Activity).window.decorView
    } else {
        null
    }
}
