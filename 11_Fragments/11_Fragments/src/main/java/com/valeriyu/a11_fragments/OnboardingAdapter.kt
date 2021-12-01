package com.valeriyu.a11_fragments

import android.util.Log
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(
    private val screens: List<OnboardingScreen>,
    activity: MainFragment): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        Log.d("viewPager", "OnboardingAdapter getItemCount")
        return screens.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.d("viewPager", "OnboardingAdapter createFragment $position")
        val screen: OnboardingScreen = screens[position]
        return OnboardingFragment.newInstance(
            textRes = screen.textRes,
            textTabRes = screen.textTabRes,
            drawableRes = screen.drawableRes
        )
    }
}