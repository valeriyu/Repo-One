package com.valeriyu.a08_activitylifecycle

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
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*

const val LOGIN = "test@localhost"
const val PASSWORD = "123"
const val FORM_STATE = "FORM_STATE"
const val tag = "_a08_activitylifecycle_inside"

class MainActivity : AppCompatActivity() {

    var status: FormState = FormState()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //MyLogger.log("onSaveInstanceState $status")
        outState.putParcelable("FORM_STATE", status)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        status = savedInstanceState.getParcelable<FormState>("FORM_STATE") ?: FormState()
        //MyLogger.log("onRestoreInstanceState  $status")
        updErrMsg()
    }

    override fun onStart() {
        super.onStart()
        MyLogger.log("onStart")
    }

    override fun onRestart() {
        super.onRestart()
        MyLogger.log("onRestart")
    }

    override fun onResume() {
        super.onResume()
        MyLogger.log("onResume")
    }

    override fun onPause() {
        super.onPause()
        MyLogger.log("onPause")
    }

    override fun onStop() {
        super.onStop()
        MyLogger.log("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        MyLogger.log("onDestroy")
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        MyLogger.log("onTopResumedActivityChanged $isTopResumedActivity")
    }

    //@SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyLogger.log("onCreate")

        buttonANR.setOnClickListener {
            clearFindViewByIdCache()
            Thread.sleep(12000)
        }

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT)

        textViewAgrement.setMovementMethod(ScrollingMovementMethod())

        updErrMsg()

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

        buttonANR.setOnClickListener {
            Thread.sleep(15000)
        }

        button.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onClick(v: View?) {

                loginOnClick(v)
            }

        })
        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                button.isEnabled = isChecked
            }
        })

    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun checkBlank() {
        if (editTextEmailAddress.text.isNotBlank() && editTextPassword.text.isNotBlank()) {
            checkBox.isEnabled = true
            button.isEnabled = checkBox.isChecked
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
    }

    fun loginOnClick(v: View?) {

        val continerDynamic =
            layoutInflater.inflate(R.layout.layout_dynamic, container, false)

        if (editTextEmailAddress.text.toString() == LOGIN && editTextPassword.text.toString() == PASSWORD && checkBox.isChecked) {

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

            status = FormState()
            updErrMsg()
            return
        }

        if (!editTextEmailAddress.text.toString().contains("@")) {
            status = FormState(false, "Неправильный адрес электронной почты !!!")
            updErrMsg()
        } else {
            status = FormState(false, "Неправильный логин или пароль !!!")
            updErrMsg()
        }
    }

    fun updErrMsg() {
        textViewMessage.text = status.message
    }
}




