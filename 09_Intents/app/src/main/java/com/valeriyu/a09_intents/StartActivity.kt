package com.valeriyu.a09_intents

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.iterator
import kotlinx.android.synthetic.main.activity_start.*

//=====================================================
const val INFO_ENABLE = false
//=====================================================

const val LOGIN = "user@test.ru"
const val PASSWORD = "1"
const val FORM_STATE = "FORM_STATE"
const val tag = "_a09_intents_inside"

//  class StartActivity : AppCompatActivity(R.layout.activity_start) {
class StartActivity : AppCompatActivity() {


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

    //=============================================================================================

    //@SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_start)

        MyLogger.log("onCreate")

        editTextEmailAddress.setText(LOGIN)
        editTextPassword.setText(PASSWORD)
        checkBlank()

/*       buttonANR.setOnClickListener {
            clearFindViewByIdCache()
            Thread.sleep(12000)
        }*/

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT)

        textViewAgrement.setMovementMethod(ScrollingMovementMethod())

        updErrMsg()

        for (el in container) {

            if (el.id != editTextEmailAddress.id && el.id != editTextPassword.id) continue

            findViewById<EditText>(el.id).addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    status = FormState(true, "")
                    updErrMsg()
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


            CommonFunctions.openWebPage(
                this,
                packageManager,
                "https://yandex.ru/pogoda/moscow?via=moc"
            )

                finish()
        }


        button.setOnClickListener() {
            loginOnClick(it)
        }

        onInfoClick.setOnClickListener() {
            var am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            CommonFunctions.onInfoClick(am)
        }

        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                editTextEmailAddress.isEnabled = !isChecked
                editTextPassword.isEnabled = !isChecked
                button.isEnabled = isChecked
                status = FormState(true, "")
                updErrMsg()
            }
        })

        //onInfoClick.isVisible = false
        onInfoClick.isVisible = INFO_ENABLE

    }

//=============================================================================================

   /* private fun toast(text: String) {
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        CommonFunctions.toast(this, text)
    }*/

    fun checkBlank() {

        val emailAddress = editTextEmailAddress.text.toString()
        var emailAddressOK = false

//        if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
//            emailAddressOK = true
//        }

        if (emailAddress.matches(
                ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+").toRegex()
            )
        ) {
            emailAddressOK = true
        }


        if (emailAddressOK && editTextEmailAddress.text.isNotBlank() && editTextPassword.text.isNotBlank()) {
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

        // if (editTextEmailAddress.text.toString() == LOGIN && editTextPassword.text.toString() == PASSWORD && checkBox.isChecked) {
        if (editTextEmailAddress.text.isNotBlank() && editTextPassword.text.isNotBlank()  && checkBox.isChecked) {

            setIsEnable(false)
            container.addView(continerDynamic)

            Handler().postDelayed({
                setIsEnable(true)
                container.removeView(continerDynamic)
                CommonFunctions.toast(this, "Авторизация прошла успешно !!!")
            }, 2000)

            status = FormState()
            updErrMsg()

            val mainActivityIntent = Intent(
                this,
                MainActivity::class.java
            )

            startActivity(mainActivityIntent)
            finish()
            return
        }

        checkBox.isChecked = false
        status = FormState(false, "Неправильный логин или пароль !!!")
        updErrMsg()
    }

    fun updErrMsg() {
        textViewMessage.text = status.message
    }

}




