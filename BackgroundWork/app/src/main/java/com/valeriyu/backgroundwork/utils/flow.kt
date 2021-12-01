package com.valeriyu.backgroundwork.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun EditText.textChangedFlow(): Flow<String> {
    return callbackFlow<String> {
        val textChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendBlocking(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        this@textChangedFlow.addTextChangedListener(textChangedListener)
        awaitClose {
            this@textChangedFlow.removeTextChangedListener(textChangedListener)
        }
    }
}

/*@ExperimentalCoroutinesApi
fun RadioGroup.onCheckedChangeFlow(): Flow<MovieType> {
    return callbackFlow {
        this@onCheckedChangeFlow.setOnCheckedChangeListener { group, checkedId ->
            val radioButton: RadioButton = group.findViewById(checkedId)
            sendBlocking(radioButton.text.toString())
        }
        awaitClose {
            this@onCheckedChangeFlow.setOnCheckedChangeListener(null)
        }
    }
        .map {
            MovieType.valueOf(it)
        }
}*/

@ExperimentalCoroutinesApi
fun CheckBox.checkedChangesFlow(): Flow<Boolean> {
    return callbackFlow {
        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            sendBlocking(isChecked)
        }
        setOnCheckedChangeListener(checkedChangeListener)
        awaitClose {
            setOnCheckedChangeListener(null)
        }
    }
}




