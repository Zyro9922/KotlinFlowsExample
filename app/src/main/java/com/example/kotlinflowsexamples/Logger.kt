package com.example.kotlinflowsexamples

import android.widget.TextView
import java.lang.ref.WeakReference

object Logger {
    private var logTextView: WeakReference<TextView>? = null

    // Set the TextView to log messages
    fun setLogTextView(textView: TextView) {
        logTextView = WeakReference(textView)
    }

    // Log a message to the TextView
    fun printToTextView(message: String) {
        logTextView?.get()?.let {
            it.append("$message\n")
        }
    }
}
