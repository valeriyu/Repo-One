package com.skillbox.multithreading

import androidx.fragment.app.Fragment
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class DeadlockFragment : Fragment(R.layout.fragment_console) {

    var executor = Executors.newFixedThreadPool(2)

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdownNow()
    }

    override fun onResume() {
        super.onResume()
        start()
    }

    fun start() {
        var a = 0
        var b = 1
        var barrier = CyclicBarrier(2)

        fun operation1() {
            activity.println("  firstThread Start a = $a")
            (0..49).forEach {
                synchronized(a) {
                    barrier.await()
                    synchronized(b) {
                        a = a + b
                        activity.println(" firstThread => a = $a")
                        //Thread.sleep(100)
                    }
                }
            }
            activity.println("  firstThread end a = $a")
        }

        fun operation2() {
            activity.println("  secondThread Start a = $a")
            (0..49).forEach {
                synchronized(b) {
                    barrier.await()
                    synchronized(a) {
                        a = a + b
                        activity.println(" secondThread => a = $a")
                        //Thread.sleep(100)
                    }
                }
            }
            activity.println("  secondThread end a = $a")
        }


        Executors.newFixedThreadPool(1)
            .submit(
                {
                    executor.submit({ operation1() })
                    executor.submit({ operation2() })
                    executor.shutdown()
                    executor.awaitTermination(5, TimeUnit.SECONDS)
                    activity.println("\n  Deadlock разорван по таймауту - 5 сек.\n")
                    if (!executor.isShutdown) {
                        executor.shutdownNow()
                    }
                })
    }
}


/* fun print(text: String) {
     activity?.runOnUiThread {
         consoleTextView.setText(consoleTextView.text.toString() + text)
     }
 }

 fun println(text: String) {
     activity?.runOnUiThread {
         consoleTextView.setText(consoleTextView.text.toString() + text + "\n")
     }
 }

 fun clr() {
     activity?.runOnUiThread {
         consoleTextView.setText("")
     }
 }*/


/*  private var i = 0
  private val lock1 = Any()
  private val lock2 = Any()


  override fun onResume() {
      super.onResume()

      val friend1 = Person("Вася")
      val friend2 = Person("Петя")

      val thread1 = Thread {
          Log.d("Deadlock", "Start1 i = ${i}")

          (0..1000000).forEach {
              //synchronized(lock1) {
                  synchronized(this) {
                      i++
                  }
              //}
          }
          Log.d("Deadlock", "End1 i = ${i}")
      }

      val thread2 = Thread {
          Log.d("Deadlock", "Start2 i = ${i}")
          (0..1000000).forEach {
              //synchronized(lock2) {
                  synchronized(this) {
                      i++
                  }
              //}
          }

          Log.d("Deadlock", "End2 i = ${i}")
      }

      thread1.start()
      thread2.start()

      thread1.join()
      thread2.join()

      Log.d("Deadlock", "END i = ${i}")
  }*/

/*override fun onResume() {
    super.onResume()

    val friend1 = Person("Вася")
    val friend2 = Person("Петя")

    val thread1 = Thread {
        Log.d("Deadlock", "Start1")

        (0..1000000).forEach {
            synchronized(lock1) {
                synchronized(lock2) {
                    i++
                }
            }
        }
        Log.d("Deadlock", "End1")
    }

    val thread2 = Thread {
        Log.d("Deadlock", "Start2")
        (0..1000000).forEach {
            synchronized(lock2) {
                synchronized(lock1) {
                    i++
                }
            }
        }

        Log.d("Deadlock", "End2")
    }

    thread1.start()
    thread2.start()
}*/


/*   data class Person(
       val name: String
   ) {

       fun throwBallTo(friend: Person) {
           synchronized(this) {
               Log.d(
                   "Person",
                   "$name бросает мяч ${friend.name} на потоке ${Thread.currentThread().name}"
               )
               Thread.sleep(500)
           }
           friend.throwBallTo(this)
       }

   }
}*/