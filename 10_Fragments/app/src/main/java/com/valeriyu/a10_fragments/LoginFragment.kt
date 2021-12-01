package com.valeriyu.a10_fragments

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*

const val LOGIN = "user@test.ru"
const val PASSWORD = "1"
const val FORM_STATE = "FORM_STATE"
const val tag = "a10_fragments_inside"


class LoginFragment : Fragment(R.layout.fragment_login) {

    var status: FormState = FormState()

    companion object {

        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("FORM_STATE", status)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            status = savedInstanceState.getParcelable<FormState>("FORM_STATE") ?: FormState()
        }
        updErrMsg()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        editTextEmailAddress.setText(LOGIN)
        editTextPassword.setText(PASSWORD)
        checkBlank()

        button.setOnClickListener {
            loginOnClick()
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

        (view as ViewGroup)
            .children
            .mapNotNull { it as? EditText }
            .forEach { editText ->
                editText.addTextChangedListener(object : TextWatcher {
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

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }
                })
            }
    }

    private fun toast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
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
        textViewAgrement.isEnabled = isEnable
    }

    fun loginOnClick() {

        val continerDynamic =
            layoutInflater.inflate(R.layout.layout_dynamic, null, false)

        val emailAddress = editTextEmailAddress.text.toString()
        var emailAddressOK = if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) true
        else false

        if (!emailAddressOK) {
            status = FormState(false, "Неправильный формат Электронной почты  !!!")
            updErrMsg()
            return
        }

        if (!editTextEmailAddress.text.isBlank() && !editTextPassword.text.isBlank() && checkBox.isChecked && emailAddressOK) {

            //setIsEnable(false)
            containerLogin.addView(continerDynamic)

            Handler().postDelayed({
                setIsEnable(true)
                containerLogin.removeView(continerDynamic)

                toast("Авторизация прошла успешно !!!")

                (activity as? FragmentOpenListner)?.OpenFragment(MainFragment.newInstance(), "MAIN")

            }, 2000)

            status = FormState()
            updErrMsg()

            //return
        } else {
            //toast("Неправильный логин или пароль !!!")
            status = FormState(false, "Неправильный логин или пароль !!!")
            updErrMsg()
        }

    }

    fun updErrMsg() {
        textViewMessage.text = status.message
    }

}