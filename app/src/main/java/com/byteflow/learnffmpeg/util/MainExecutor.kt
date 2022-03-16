package com.robertlevonyan.demo.camerax.utils

import android.os.Handler
import android.os.Looper
import com.byteflow.learnffmpeg.util.ThreadExecutor

class MainExecutor : ThreadExecutor(Handler(Looper.getMainLooper())) {
    override fun execute(runnable: Runnable) {
        handler.post(runnable)
    }
}
