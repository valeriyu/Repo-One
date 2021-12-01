package com.valeriyu.lists_2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.valeriyu.lists_2.Linear.ToolsListFragment
import com.valeriyu.lists_2.Grid.ImageListFragment
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(R.layout.fragment_main) {

    private var buttons: List<Button> = emptyList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (mainFragmentContainer as ViewGroup)
            .children
            .mapNotNull { it as? Button }
            .forEach { button ->
                buttons += button
                // button.visibility = View.VISIBLE
                button.setOnClickListener {
                    clickListner(it as Button)
                }
            }
    }

    private fun clickListner(btn: Button) {

        when (btn) {
            btn1 -> {
                childFragmentManager.beginTransaction()
                    .replace(R.id.mainFragmentContainer, ToolsListFragment.newInstance(0), "LIST")
                    .commit()
            }

            btn2 -> {
                childFragmentManager.beginTransaction()
                    .replace(R.id.mainFragmentContainer, ToolsListFragment.newInstance(1), "LIST")
                    .commit()
            }

            btn3 -> {
                childFragmentManager.beginTransaction()
                    .replace(R.id.mainFragmentContainer, ImageListFragment.newInstance(0), "LIST")
                    .commit()
            }


            btn4 -> {
                childFragmentManager.beginTransaction()
                    .replace(R.id.mainFragmentContainer, ImageListFragment.newInstance(1), "LIST")
                    .commit()
            }
        }

        buttons.forEach {
            it.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}