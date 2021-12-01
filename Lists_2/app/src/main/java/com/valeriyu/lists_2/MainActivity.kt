package com.valeriyu.lists_2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import kotlinx.android.synthetic.main.fragment_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainActivityContainer, MainFragment.newInstance(), "MAIN")
                //.addToBackStack("MAIN")
                .commit()
        }
    }

    override fun onBackPressed() {

        var childFragmentManager =
            supportFragmentManager.findFragmentByTag("MAIN")?.childFragmentManager
/*
             var listFragment =
                 childFragmentManager?.findFragmentByTag(
                     "LIST"
                 )
       */
        // var mainFragment = supportFragmentManager.findFragmentByTag("MAIN")

        var listFrament =
            childFragmentManager?.fragments?.find { it.tag == "LIST" }


        if (listFrament != null) {
            childFragmentManager?.beginTransaction()
                ?.remove(listFrament)
                ?.commit()

            (mainFragmentContainer as ViewGroup)
                .children
                .mapNotNull { it as? Button }
                .forEach { button ->
                    button.visibility = View.VISIBLE
                }
        } else {
            // finish()
            super.onBackPressed()
        }
    }
}


