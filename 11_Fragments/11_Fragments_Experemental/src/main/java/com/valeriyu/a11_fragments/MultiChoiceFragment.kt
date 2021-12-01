package com.valeriyu.a11_fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

private const val ARG_PARAM1 = "checkedItems"

class MultiChoiceFragment : DialogFragment() {

    private var checkedItems: BooleanArray? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBooleanArray(ARG_PARAM1, checkedItems)
    }

    companion object {

        fun newInstance(checkedItems: BooleanArray?) =
            MultiChoiceFragment().apply {
                arguments = Bundle().apply {
                    putBooleanArray(ARG_PARAM1, checkedItems)
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        arguments?.let {
            checkedItems = it.getBooleanArray(ARG_PARAM1)

        }

        var tagNames = emptyArray<CharSequence>()

        if (tagNames.isEmpty()) {
            ArticleTag.values().forEach {
                tagNames += it.name
            }
        }

        return activity?.let {

            var handler = Handler()
            handler
                .postDelayed({
                    activity.toast("Выбор закрылся по таймауту однако !!!")
                    dialog?.dismiss()
                }, 10000)


            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите что нибудь")
                .setMultiChoiceItems(tagNames, checkedItems) { _, which, isChecked ->
                    handler.removeCallbacksAndMessages(null)
                    checkedItems!!.set(which, isChecked)
                }
                .setPositiveButton(
                    "Применить"
                ) { dialog, _ ->
                    handler.removeCallbacksAndMessages(null)
                    //(parentFragment as FragmentOnClickListner).buttonOnClickListener(
                    (activity as FragmentOnClickListner).buttonOnClickListener(
                        "APPLY",
                        checkedItems
                    )
                    dialog.dismiss()
                }

                .setNegativeButton("Отмена") { dialog, _ ->
                    handler.removeCallbacksAndMessages(null)
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}