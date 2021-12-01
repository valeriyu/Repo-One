package com.valeriyu.a01_constraintlayout

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT)

        textViewAgrement.setMovementMethod(ScrollingMovementMethod())

        for (el in container) {

            if (el.id != editTextEmailAddress.id && el.id != editTextPassword.id) continue

            findViewById<EditText>(el.id).addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    checkBlank()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
/*
        editTextEmailAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
*/
        button.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onClick(v: View?) {

                val continerDynamic =
                    layoutInflater.inflate(R.layout.layout_dynamic, container, false)

                setIsEnable(false)
                container.addView(continerDynamic)

                Handler().postDelayed({
                    setIsEnable(true)

                    container.removeView(continerDynamic)

                    Toast.makeText(
                        this@MainActivity,
                        "Авторизация прошла успешно !!!",
                        Toast.LENGTH_LONG
                    ).show()
                }, 2000)
            }
        })
        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                button.isEnabled = isChecked
            }
        })

    }

    fun checkBlank() {
        if (editTextEmailAddress.text.isNotBlank() && editTextPassword.text.isNotBlank()) {
            checkBox.isEnabled = true
            button.isEnabled = true
        } else {
            checkBox.isEnabled = false
            button.isEnabled = false
        }
    }

    fun setIsEnable(isEnable: Boolean) {
        for (el in container) {
            if (el.id == textViewAgrement.id) continue
            el.isEnabled = isEnable
        }

        fun afterTextChanged() {
        }

    }
}



