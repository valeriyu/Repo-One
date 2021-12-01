package com.skillbox.multithreading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_console.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

const val TAG = "Livelock"

/*
Thread-1: First Locked: true
Thread-1: wait for 2 sec
Thread-0: First Locked: false
Thread-0: wait for 1 sec
Thread-0: Second Locked: true
Thread-0: wait for 1 sec
Thread-1: Second Locked: false
Thread-1: wait for 2 sec
Thread-0: First Locked: false
Thread-0: wait for 1 sec
Thread-0: Second Locked: true
Thread-0: wait for 1 sec
Thread-1: First Locked: true
Thread-1: wait for 2 sec
Thread-0: First Locked: false
Thread-0: wait for 1 sec
Thread-0: Second Locked: true
Thread-0: wait for 1 sec
Thread-1: Second Locked: false
Thread-1: wait for 2 sec
Thread-0: First Locked: false
Thread-0: wait for 1 sec
 */

class LivelockFragment : Fragment(R.layout.fragment_console) {

    val first: Lock = ReentrantLock()
    val second: Lock = ReentrantLock()

    private val latch = CountDownLatch(2)


    var firstThread: Thread? = null
    var secondThread: Thread? = null

    var firstThreadName = ""
    var secondThreadName = ""

    fun thread(delay: Long): Thread {
        return Thread {
            //firstThreadName = Thread.currentThread().name
            latch.countDown()
            latch.await()
            Thread.sleep(delay)
            try {
                locker()
            }catch (e:Exception){
            }
        }
    }

    fun start() {
        activity.println("Если первым начинает работать 1-й поток - LiveLock не возникает.\n")


        Thread {
            firstThread = thread(0)
            firstThreadName = firstThread!!.name

            secondThread = thread(2)
            secondThreadName = secondThread!!.name

            firstThread!!.start()
            secondThread!!.start()

            firstThread!!.join()
            secondThread!!.join()


            activity.println("\nЕсли первым начинает работать 2-й поток - возникает LiveLock !!!\n")

            firstThread = thread(2)
            firstThreadName = firstThread!!.name

            secondThread = thread(0)
            secondThreadName = secondThread!!.name

            firstThread!!.start()
            secondThread!!.start()

        }.start()
    }


    fun log(text: String) {
        val name = Thread.currentThread().name

        var value = when (name) {
            firstThreadName -> 1
            secondThreadName -> 2
            else -> 0
        }

        activity.println("$name: $text")
        activity.println("$name: wait for $value sec")

        Thread.sleep((value * 1000).toLong())
    }

    fun locker() {
        var firstLocked = false
        var secondLocked = false

        while (!firstLocked || !secondLocked) {
            firstLocked = first.tryLock(200, TimeUnit.MILLISECONDS)
            log("First Locked: $firstLocked")
            secondLocked = second.tryLock(200, TimeUnit.MILLISECONDS)
            log("Second Locked: $secondLocked")
        }
        first.unlock()
        second.unlock()
    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            firstThread?.interrupt()
        } catch (e: Exception) {
        } finally {
            firstThread = null
        }

        try {
            secondThread?.interrupt()
        } catch (e: Exception) {
        } finally {
            secondThread = null
        }
    }

    override fun onResume() {
        super.onResume()
        start()
    }
}

/*
override fun onResume() {
    super.onResume()


           firstThread = Thread({ operation1() })
           firstThread!!.start()

           secondThread = Thread({ operation2() })
           secondThread!!.start()
}

    private fun operation1() {

        //Decrement count from each thread
        latch.countDown()
        //Waiting for count == 0. ie 2 threads are started
        latch.await()

        while (true) {
            lock1.lock()
            Log.d(
                "Livelock",
                "lock1 acquired, trying to acquire lock2. From thread = ${Thread.currentThread().name}"
            )
            Thread.sleep(500)
            if (lock2.tryLock()) {
                Log.d("Livelock", "lock2 acquired. From thread = ${Thread.currentThread().name}")
            } else {
                Log.d(
                    "Livelock",
                    "cannot acquire lock2, releasing lock1. From thread = ${Thread.currentThread().name}"
                )
                lock1.unlock()
                continue
            }
            Log.d("Livelock", "executing first work. From thread = ${Thread.currentThread().name}")
            break
        }
        lock2.unlock()
        lock1.unlock()
    }

    private fun operation2() {
        //Decrement count from each thread
        latch.countDown()
        //Waiting for count == 0. ie 2 threads are started
        latch.await()
        while (true) {
            lock2.lock()
            Log.d(
                "Livelock",
                "lock2 acquired, trying to acquire lock1. From thread = ${Thread.currentThread().name}"
            )
            Thread.sleep(500)
            if (lock1.tryLock()) {
                Log.d("Livelock", "lock1 acquired. From thread = ${Thread.currentThread().name}")
            } else {
                Log.d(
                    "Livelock",
                    "cannot acquire lock1, releasing lock2. From thread = ${Thread.currentThread().name}"
                )
                lock2.unlock()
                continue
            }
            Log.d("Livelock", "executing second work. From thread = ${Thread.currentThread().name}")
            break
        }
        lock1.unlock()
        lock2.unlock()
    }


    // Fixed livelock

    private fun __operation1() {
        //Decrement count from each thread
        latch.countDown()
        //Waiting for count == 0. ie 2 threads are started
        latch.await()

        while (true) {
            if (lock1.tryLock()) {
                Log.d(
                    "Livelock",
                    "lock1 acquired, trying to acquire lock2. From thread = ${Thread.currentThread().name}"
                )
                if (lock2.tryLock()) {
                    Log.d(
                        "Livelock",
                        "lock2 acquired. From thread = ${Thread.currentThread().name}"
                    )
                    Log.d(
                        "Livelock",
                        "executing work. From thread = ${Thread.currentThread().name}"
                    )
                    lock2.unlock()
                    lock1.unlock()
                    break
                } else {
                    Log.d(
                        "Livelock",
                        "cannot acquire lock2, releasing lock1. From thread = ${Thread.currentThread().name}"
                    )
                    lock1.unlock()
                    continue
                }
            } else {
                Log.d(
                    "Livelock",
                    "cannot acquire lock1. From thread = ${Thread.currentThread().name}"
                )
            }
        }
    }

    private fun __operation2() {
        operation1()
    }

}

 */