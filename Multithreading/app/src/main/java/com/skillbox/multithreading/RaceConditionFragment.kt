package com.skillbox.multithreading

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_race_condition.*

class RaceConditionFragment : Fragment(R.layout.fragment_race_condition) {

    private var value: Int = 0
    private var threadCount: Int = 100
    private var incrementCount: Long = 1000000
    private var startTime :Long = 0
    private var requestTime:Long = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnSync.setOnClickListener {
            syncMultithreadingIIncrement()
        }
        btnNoSync.setOnClickListener {
            noSyncMultithreadingIIncrement()
        }

        threadCountTextView.setText(threadCount.toString())
        incrementCountTextView.setText(incrementCount.toString())

    }

    @SuppressLint("SetTextI18n")
    fun setResult() {
        if (value != 0) {
            resultTextView.text = "Ожидаемое значение: ${threadCount * incrementCount}\n" +
                    "Реальное значение: ${value}\n" +
                    "Время выполнения: ${requestTime}"
        } else {
            resultTextView.text = ""
        }
    }

    private fun syncMultithreadingIIncrement() {
        resultTextView.text = ""
        value = 0
        startTime = System.currentTimeMillis()

        threadCount = threadCountTextView.text.toString().toInt()
        incrementCount = incrementCountTextView.text.toString().toLong()

        (0..threadCount - 1).map {
            Thread {
                synchronized(this) {
                    for (i in 0..incrementCount - 1) {
                        value++
                    }
                }
            }.apply {
                start()
            }
        }
            .map { it.join() }

        requestTime = System.currentTimeMillis() - startTime
        setResult()
    }

    private fun noSyncMultithreadingIIncrement() {
        resultTextView.text = ""
        value = 0
        startTime = System.currentTimeMillis()

        threadCount = threadCountTextView.text.toString().toInt()
        incrementCount = incrementCountTextView.text.toString().toLong()

        (0..threadCount - 1).map {
            Thread {
                for (i in 0..incrementCount - 1) {
                    value++
                }
            }.apply {
                start()
            }
        }
            .map { it.join() }

        requestTime = System.currentTimeMillis() - startTime
        setResult()
    }
}