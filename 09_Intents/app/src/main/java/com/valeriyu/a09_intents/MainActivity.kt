package com.valeriyu.a09_intents

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {


    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun navigationOnClick() {
        finish()
    }

    fun _dial() {
        var phoneNumber = textViewNumber.text.toString()

        //val dialIntent = Intent(Intent.ACTION_DIAL).apply {
        //    data = Uri.parse("tel:$phoneNumber")
        //}
        if (Patterns.PHONE.matcher(phoneNumber).matches()) {
            //val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            val dialIntent = Intent(Intent.ACTION_DIAL)
            if (dialIntent.resolveActivity(packageManager) != null) {
                //startActivity(dialIntent)
                dialIntent.setData(Uri.parse("tel:$phoneNumber"));
                startActivityForResult(dialIntent, 666)
            }
        } else {
            toast("Неправильный номер !!!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)

        //main_toolbar.setNavigationOnClickListener {
        //    navigationOnClick()
        //}

        buttonDial.setOnClickListener {
            _dial()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 666) {
            if (resultCode == RESULT_OK) {
                toast("RESULT_OK")
            } else {
                toast("RESULT_CANCELED")
                //toast("Звонилка вернула ==> $resultCode")
            }
        }
    }
}

