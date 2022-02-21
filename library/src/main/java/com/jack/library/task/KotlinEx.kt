package com.jack.library.task

import android.util.Log


fun logi(tag: String, msg: String) {
    Log.i(tag, msg)
}

fun logd(tag: String, msg: String?) {
    Log.i(tag, msg ?: "")
}

fun loge(tag: String, msg: String?) {
    Log.i(tag, msg ?: "")
}