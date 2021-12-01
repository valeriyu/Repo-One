package com.valeriyu.a11_fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_main.*


class MainActivity : AppCompatActivity(), FragmentOnClickListner {

    private var fragmentState = FragmentState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainConainer, MainFragment.newInstance(), "MAIN")
                //.addToBackStack(null)
                .commit()
        }
    }

    override fun buttonOnClickListener(selector: String, param: BooleanArray?) {
        when (selector) {

            "APPLY" -> {

                if (fragmentState.badgePos != null) {
                    tabLayout1.getTabAt(fragmentState.badgePos!!)?.removeBadge()
                    fragmentState.badgePos = null
                }

                if (param != null) {
                    fragmentState.checkedItems = param
                }
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainConainer, MainFragment.newInstance(fragmentState), "MAIN")
                        .commit()
            }
        }
    }
}


