package com.orange.callhistory

import kotlinx.coroutines.*
import org.junit.Test

class CoroutineTest {

    // how to assert code on coroutine ?


    @Test
    fun test1() {
        GlobalScope.launch {
            // launch new coroutine in background and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
        println("Hello,") // main thread continues while coroutine is delayed
        Thread.sleep(2000L)
    }

    @Test
    fun test2() = runBlocking {
        GlobalScope.launch {
            // launch new coroutine in background and continue
            delay(1000L)
            println("World!")
        }
        println("Hello,") // main thread continues here immediately
        // but this expression blocks the main thread
        delay(2000L)  // ... while we delay for 2 seconds to keep JVM alive
    }

    @Test
    fun test3() = runBlocking {
        val job = GlobalScope.launch { printWorld() }
        println("Hello,")
        job.join() // wait until child coroutine completes
    }

    @Test
    fun test4() = runBlocking {
        launch { printWorld() }
        println("Hello,")
    }

    @Test
    fun test5() = runBlocking {
        launch {
            delay(200L)
            println("Task from runBlocking")
        }

        coroutineScope {
            // Creates a new coroutine scope
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // This line will be printed before nested launch
        }

        println("Coroutine scope is over") // This line is not printed until nested launch completes
    }

    @Test
    fun test6() = runBlocking {
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(490L) // just quit after delay
    }


    private suspend fun printWorld() {
        delay(1000L)
        println("World!")
    }
}
