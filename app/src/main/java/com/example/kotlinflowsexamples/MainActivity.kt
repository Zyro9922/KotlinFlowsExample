package com.example.kotlinflowsexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "ZYRO"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                Log.d(LOG_TAG, it)
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
                Log.d(LOG_TAG, userName)
            }
        }
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
        // Create a Flow of user names using asFlow
        return listOf(1, 2, 3, 4, 5)
            .asFlow()
            .map { getUser(it) }
    }
    private suspend fun getUser(id: Int) : String {
        Log.d(LOG_TAG, "getUser $id Current thread: " + Thread.currentThread().getName())
        delay(1000)
        return "User $id"
    }
}