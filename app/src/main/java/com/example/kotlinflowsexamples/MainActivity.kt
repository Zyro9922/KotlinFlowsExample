package com.example.kotlinflowsexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "ZYRO"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logTextView: TextView = findViewById(R.id.logTextView)
        Logger.setLogTextView(logTextView)
        val logButton: Button = findViewById(R.id.logButton)

        lifecycleScope.launch {
            val result = SharedFlowExample()
            result.collect{
                Logger.printToTextView("Result 1 ${it}")
            }
        }

        lifecycleScope.launch {
            val result = SharedFlowExample()
            delay(10000)
            result.collect{
                Logger.printToTextView("Result 2 ${it}")
            }
        }
    }

    fun onButtonClick(view: View){
        Scenario2()
    }

    private fun Scenario1() {
        /**
         * Scenario: forEach will wait for the getUserNames to return the list.
         * Hence in this scenario all the logs will be printed after 5 seconds
         */
        lifecycleScope.launch(Dispatchers.Default){
            getUserNames().forEach{
                Logger.printToTextView(it)
            }
        }
    }

    private fun Scenario2() {
        /**
         * Scenario2: Using Kotlin Flows
         *
         * Unlike Scenario1, where the user names were logged only after all of them were
         * retrieved (causing a 5-second delay), Scenario2 demonstrates a more efficient approach.
         *
         * Scenario2 uses Flows, to emit user names as soon as they become available.
         */
        lifecycleScope.launch(Dispatchers.Default) {
            getUserNamesAsFlows().collect { userName ->
                Logger.printToTextView(userName)
            }
        }
    }

    private fun StateFlowExample(): Flow<Int> {
        val mutableSharedFlow = MutableStateFlow<Int>(8989)
        lifecycleScope.launch {
            val list = listOf<Int>(1,2,3,4,5)
            list.forEach{
                mutableSharedFlow.emit(it)
                delay(1000)
            }
        }
        return mutableSharedFlow
    }

    private fun SharedFlowExample(): Flow<Int> {
        val mutableSharedFlow = MutableSharedFlow<Int>()
        lifecycleScope.launch {
            val list = listOf<Int>(1,2,3,4,5)
            list.forEach{
                mutableSharedFlow.emit(it)
                delay(1000)
            }
        }
        return mutableSharedFlow
    }

    private suspend fun getUserNames() : List<String> {
        val list = mutableListOf<String>()
        list.add(getUser(1))
        list.add(getUser(2))
        list.add(getUser(3))
        list.add(getUser(4))
        list.add(getUser(5))
        return list
    }

    private suspend fun getUserNamesAsFlows(): Flow<String> {
        Logger.printToTextView("First line getUserNamesAsFlows")
        // Create a Flow of user names using asFlow
        return listOf(1, 2, 3, 4, 5)
            .asFlow()
            .map { getUser(it) }
    }
    private suspend fun getUser(id: Int) : String {
        Logger.printToTextView("getUser $id Current thread: " + Thread.currentThread().getName())
        delay(1000)
        return "User $id"
    }
}