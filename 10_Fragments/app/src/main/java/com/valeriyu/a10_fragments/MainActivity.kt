package com.valeriyu.a10_fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


const val TAG = "a10_fragmentsinside"

class MainActivity() : AppCompatActivity(), FragmentOpenListner {


    override fun onBackPressed() {

        //Log.d("_onViewStateRestored_", "onViewStateRestored ==> ${this.toString()} ${supportFragmentManager.fragments.lastOrNull()?.childFragmentManager?.fragments}}")

        var childFragmentManager =
            supportFragmentManager.findFragmentByTag("MAIN")?.childFragmentManager


        /*     var infoFrament =
                 childFragmentManager?.findFragmentByTag(
                     "INFO"
                 )

             var listFragment =
                 childFragmentManager?.findFragmentByTag(
                     "LIST"
                 )
       */

        var infoFrament =
            childFragmentManager?.fragments?.find { it.tag == "INFO" }

        var listFragment =
            childFragmentManager?.fragments?.find { it.tag == "LIST" }

        //var isLarge =
        //    (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE

        if ((resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            if (infoFrament != null && listFragment != null) {
                childFragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
                    ?.remove(infoFrament)
                    ?.show(listFragment)
                    ?.commit()
            } else {
                if (listFragment != null) {
                    childFragmentManager?.beginTransaction()
                        ?.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
                        ?.remove(listFragment)
                        //?.hide(listFragment)
                        ?.commit()
                }
                super.onBackPressed()
            }

        } else {
            if (infoFrament != null && listFragment != null) {
                //childFragmentManager?.popBackStackImmediate()

                childFragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
                    ?.remove(infoFrament)
                    ?.commit()
            } else {
                super.onBackPressed()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Log.d(TAG, "onCreate ==> ${this.toString()} ${currentFragment.toString()}")
        //Log.d(TAG, "onCreate ==> ${this.toString()} ${supportFragmentManager.fragments}")
        //Log.d(TAG, "onCreate ==> ${this.toString()} ${supportFragmentManager.fragments.lastOrNull()?.childFragmentManager?.fragments}")


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
//                .setCustomAnimations(
//                    R.animator.slide_in_left,
//                    R.animator.slide_in_right
//                )
                .replace(R.id.contMainActivity, LoginFragment.newInstance(), "LOGIN")
                .commitNow()
        }
    }

    override fun OpenFragment(fragment: Fragment, tag: String) {

        //var loginFragment = supportFragmentManager.findFragmentByTag("LOGIN")

        //if (fragment != null && loginFragment != null) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.animator.slide_in_left,
                R.animator.slide_in_right
            )
            //.remove(loginFragment)
            //.remove(LoginFragment())
            .replace(R.id.contMainActivity, fragment, "MAIN")
            .commit()
    }
}
