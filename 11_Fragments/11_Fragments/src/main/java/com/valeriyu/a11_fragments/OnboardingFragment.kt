package com.valeriyu.a11_fragments

import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnboardingFragment: Fragment(R.layout.fragment_onboarding) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("viewPager", "OnboardingFragment oncreate = ${resources.getString(requireArguments().getInt(
            KEY_TEXT))}")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //requireView().setBackgroundResource(requireArguments().getInt(KEY_TAB_TEXT))
        textView.setText(requireArguments().getInt(KEY_TEXT))
        imageView.setImageResource(requireArguments().getInt(KEY_IMAGE))

        buttonGenerateEvent.setOnClickListener(){
            (parentFragment as FragmentOnClickListner).buttonOnClickListener("BADGE")
        }
    }


    companion object {

        private const val KEY_TEXT = "text"
        private const val KEY_TAB_TEXT = "textTab"
        private const val KEY_IMAGE = "image"

        fun newInstance(
            @StringRes textRes: Int,
            @StringRes textTabRes: Int,
            @DrawableRes drawableRes: Int
        ): OnboardingFragment {
            return OnboardingFragment().withArguments {
                putInt(KEY_TEXT, textRes)
                putInt(KEY_TAB_TEXT, textTabRes)
                putInt(KEY_IMAGE, drawableRes)
            }
        }
    }

}