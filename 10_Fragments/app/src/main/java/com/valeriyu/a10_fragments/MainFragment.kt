package com.valeriyu.a10_fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class MainFragment : Fragment(R.layout.fragment_main), FragmentOpenListner {

    var isLarge: Boolean = false

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            var infoFragment =
                childFragmentManager.fragments.find { it.tag == "INFO" }
            var listFragment =
                childFragmentManager.fragments.find { it.tag == "LIST" }

            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

                if (infoFragment != null && listFragment != null) {
                    childFragmentManager.beginTransaction()
                        .hide(listFragment)
                        .show(infoFragment)
                        .commit()
                }

            } else {
                if (infoFragment != null && listFragment != null) {
                    childFragmentManager.beginTransaction()
                        .show(listFragment)
                        .show(infoFragment)
                        .commit()
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null && resources.configuration.smallestScreenWidthDp < 600) {

            OpenFragment(ListFragment.newInstance(), "LIST")

/*            childFragmentManager.beginTransaction()
                //.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
                .replace(R.id.listContainer, ListFragment(), "LIST")
                .addToBackStack(null)
                .commit()*/
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }

        isLarge =
            (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun OpenFragment(fragment: Fragment, tag: String) {


        if (tag == "LIST") {
            childFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
                .replace(R.id.listContainer, fragment, tag)
                .addToBackStack(null)
                .commit()
        } else {

            /*              if (isLarge) {
                              OpenFragmentLarge(fragment, tag)
                          } else {
                              OpenFragmentNotLarge(fragment, tag)
                          }*/

            val listFragment = childFragmentManager.findFragmentByTag("LIST")
            val infoFragment = childFragmentManager.findFragmentByTag("INFO")

            if (listFragment == null) {
                return
            }

            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                childFragmentManager.beginTransaction()
                    .hide(listFragment)
                    .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_in_right
                    )
                    //.hide(listFragment)
                    .replace(R.id.infoContainer, fragment, tag)
                    .addToBackStack(null)
                    .commit()
            } else {

                if (infoFragment != null) {
                    childFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.animator.slide_in_left,
                            R.animator.slide_in_right
                        )
                        .remove(infoFragment)
                        .replace(R.id.infoContainer, fragment, tag)
                        .addToBackStack(null)
                        .commit()
                } else {
                    childFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.animator.slide_in_left,
                            R.animator.slide_in_right
                        )
                        .replace(R.id.infoContainer, fragment, tag)
                        .addToBackStack(null)
                        .commit()
                }
            }
            Log.d("_a10_fragmentsinside_)", "${childFragmentManager.fragments}")
        }

    }

/*
    private fun OpenFragmentLarge(fragment: Fragment, tag: String) {

        var listFragment = childFragmentManager.findFragmentByTag("LIST")
        //var listFragment = childFragmentManager.findFragmentById(R.id.staticListFragment)
        var infoFragment = childFragmentManager.findFragmentByTag("INFO")

        if (listFragment == null) {
            return
        }

        if ((resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)) {

            childFragmentManager.beginTransaction()
                .hide(listFragment)
                .setCustomAnimations(
                    R.animator.slide_in_left,
                    R.animator.slide_in_right
                )
                //.hide(listFragment)
                .replace(R.id.infoContainer, fragment, tag)
                .addToBackStack(null)
                .commit()

        } else {
            if (infoFragment != null) {
                childFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_in_right
                    )
                    .remove(infoFragment)
                    .replace(R.id.infoContainer, fragment, tag)
                    .addToBackStack(null)
                    .commit()
            } else {
                childFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_in_right
                    )
                    .replace(R.id.infoContainer, fragment, tag)
                    .addToBackStack(null)
                    .commit()
            }
        }
        Log.d("_a10_fragmentsinside_)", "${childFragmentManager.fragments}")
    }

    private fun OpenFragmentNotLarge(fragment: Fragment, tag: String) {

        val listFragment = childFragmentManager.findFragmentByTag("LIST")
        val infoFragment = childFragmentManager.findFragmentByTag("INFO")

        if (listFragment == null) {
            return
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.animator.slide_in_left,
                    R.animator.slide_in_right
                )
                .hide(listFragment)
                .replace(R.id.infoContainer, fragment, tag)
                .addToBackStack(null)
                .commit()
        } else {

            if (infoFragment != null) {
                childFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_in_right
                    )
                    .remove(infoFragment)
                    .replace(R.id.infoContainer, fragment, tag)
                    .addToBackStack(null)
                    .commit()
            } else {
                childFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_in_right
                    )
                    .replace(R.id.infoContainer, fragment, tag)
                    .addToBackStack(null)
                    .commit()
            }
        }
        Log.d("_a10_fragmentsinside_)", "${childFragmentManager.fragments}")
    }

 */
}










