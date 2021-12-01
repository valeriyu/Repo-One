package com.valeriyu.lists_1

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class ChoiseToolsFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var ToolNames = emptyArray<CharSequence>()
        arguments?.let {
            ToolNames = it.getCharSequenceArray(ARG_PARAM1) as Array<CharSequence>
        }

        //return super.onCreateDialog(savedInstanceState)
        return activity?.let {
            //val selectedItems = ArrayList<Int>() // Where we track the selected items
            var selectedItem: Int = -1
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите для добавления в список")
                .setSingleChoiceItems(
                    ToolNames, -1
                ) { _, item ->
                    selectedItem = item
                }
                .setPositiveButton(
                    "OK"
                ) { dialog, _  ->
                    if (selectedItem >=0 )(parentFragment as FragmentOnClickListner).buttonOnClickListener(selectedItem)
                    dialog.dismiss()
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
         const val ARG_PARAM1 = "toolNames"

        @JvmStatic
        fun newInstance(toolNames:Array<CharSequence>) =
            ChoiseToolsFragment().apply {
                arguments = Bundle().apply {
                    putCharSequenceArray(ARG_PARAM1, toolNames)
                }
            }
    }
}