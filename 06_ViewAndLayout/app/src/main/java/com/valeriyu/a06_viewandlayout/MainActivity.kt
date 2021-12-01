package com.valeriyu.a06_viewandlayout

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ScrollView = findViewById<ScrollView>(R.id.ScrollView)
        val textView = findViewById<TextView>(R.id.textViewAgrement)
        val container = findViewById<LinearLayout>(R.id.container)
        val email = findViewById<EditText>(R.id.editTextEmailAddress)
        val password = findViewById<EditText>(R.id.editTextPassword)
        val chbox = findViewById<CheckBox>(R.id.checkBox)
        val button = findViewById<Button>(R.id.button)

        textView.setMovementMethod(ScrollingMovementMethod())

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (email.text.isNotBlank() && password.text.isNotBlank()) chbox.isEnabled = true
                else chbox.isEnabled = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (email.text.isNotBlank() && password.text.isNotBlank()) chbox.isEnabled = true
                else chbox.isEnabled = false

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        button.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onClick(v: View?) {
                /*             var textViewToAdd = TextView(this@MainActivity).apply {
                                  text = "Просто текст !!!"
                                  LinearLayout.LayoutParams(
                                      LinearLayout.LayoutParams.MATCH_PARENT,
                                      LinearLayout.LayoutParams.WRAP_CONTENT
                                  ).apply {
                                      gravity = Gravity.CENTER
                                  }
                              }
                              container.addView(textViewToAdd)*/

                var progressBarToAdd = ProgressBar(this@MainActivity).apply {
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER
                    }
                }
                chbox.isEnabled = false
                email.isEnabled = false
                password.isEnabled = false
                button.isEnabled = false

                container.removeView(chbox)
                container.removeView(button)

                container.addView(progressBarToAdd)
                Handler().postDelayed({

                    container.addView(chbox)
                    container.addView(button)

                    chbox.isEnabled = true
                    email.isEnabled = true
                    password.isEnabled = true
                    button.isEnabled = true

                    progressBarToAdd.visibility = View.GONE

                    Toast.makeText(
                        this@MainActivity,
                        "Авторизация прошла успешно !!!",
                        Toast.LENGTH_LONG
                    ).show()
                }, 2000)
            }
        })
        chbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                button.isEnabled = isChecked
            }
        })
    }
}
