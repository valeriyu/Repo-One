package com.valeriyu.a11_fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.random.Random

const val FRAGMENT_STATE = "fragmentState"

class MainFragment : Fragment(), FragmentOnClickListner {

    private var selScreens = emptyList<OnboardingScreen>()

    private var fragmentState = FragmentState()

    private val screens: List<OnboardingScreen> = listOf(
        OnboardingScreen(
            textRes = R.string.onboarding_text_1,
            textTabRes = R.string.tab_text_1,
            drawableRes = R.drawable.onboarding_dravable_1,
            tags = listOf(ArticleTag.Болгарки, ArticleTag.Электоинструмент)
        ),
        OnboardingScreen(
            textRes = R.string.onboarding_text_2,
            textTabRes = R.string.tab_text_2,
            drawableRes = R.drawable.onboarding_dravable_2,
            tags = listOf(ArticleTag.Пилы, ArticleTag.Электоинструмент)
        ),
        OnboardingScreen(
            textRes = R.string.onboarding_text_3,
            textTabRes = R.string.tab_text_3,
            drawableRes = R.drawable.onboarding_dravable_3,
            tags = listOf(ArticleTag.Пилы, ArticleTag.Бензопилы)
        ),
        OnboardingScreen(
            textRes = R.string.onboarding_text_4,
            textTabRes = R.string.tab_text_4,
            drawableRes = R.drawable.onboarding_dravable_4,
            tags = listOf(ArticleTag.Шуруповерты, ArticleTag.Аккамуляторный)
        ),
        OnboardingScreen(
            textRes = R.string.onboarding_text_5,
            textTabRes = R.string.tab_text_5,
            drawableRes = R.drawable.onboarding_dravable_5,
            tags = listOf(ArticleTag.Лобзики, ArticleTag.Электоинструмент)
        )
    )

    companion object {

        fun newInstance(fragmentState: FragmentState = FragmentState()) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(FRAGMENT_STATE, fragmentState)
                }
            }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {

            fragmentState = savedInstanceState.getParcelable<FragmentState>(FRAGMENT_STATE)!!

            setFilter()
            setContent()

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(FRAGMENT_STATE, fragmentState)
    }


    private fun setFilter() {
        var tagNames = emptyArray<CharSequence>()
        ArticleTag.values().forEach {
            tagNames += it.name
        }


        val selectedItems = emptyList<ArticleTag>().toMutableList()
        for (i in tagNames.indices) {
            if (fragmentState.checkedItems[i]) {
                selectedItems += ArticleTag.values()[i]
            }
        }

        selScreens = screens.filter { it.tags?.any { it in selectedItems } == true }

    }


    private fun setContent() {

        var adapter = OnboardingAdapter(selScreens, this)

        viewPager.adapter = adapter


        viewPager.offscreenPageLimit = 1

        //viewPager.setCurrentItem(Random.nextInt(0, screens.count()), false)
        viewPager.setCurrentItem(0, false)

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        tabLayout1.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(tabLayout1, viewPager) { tab, position ->
            tab.text = resources.getString(selScreens[position].textTabRes)
        }.attach()


        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        viewPager.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        if (fragmentState.badgePos != null) {

            tabLayout1.getTabAt(fragmentState.badgePos!!)?.orCreateBadge?.apply {
                number = fragmentState.badgeNumber
            }
        }

        dots_indicator.setViewPager2(viewPager)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (selScreens.isEmpty()) selScreens = screens

        if (fragmentState.checkedItems.isEmpty()) {
            for (i in ArticleTag.values().indices) {
                fragmentState.checkedItems += true
            }
        }

        setFilter()
        setContent()

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (fragmentState.badgePos == position) {
                        tabLayout1.getTabAt(fragmentState.badgePos!!)?.removeBadge()
                        fragmentState.badgePos = null
                    }
                }
            })

        toolbar.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.action_filter -> {
                    MultiChoiceFragment.newInstance(
                        fragmentState.checkedItems
                    )
                        .show(childFragmentManager, "multiChoiceFragment")

                    true
                }
                else -> false
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            fragmentState = it.getParcelable<FragmentState>(FRAGMENT_STATE)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun buttonOnClickListener(selector: String, param: BooleanArray?) {
        when (selector) {
            "BADGE" -> {
                if (fragmentState.badgePos == null) {
                    fragmentState.badgePos = Random.nextInt(0, selScreens.count())
                    fragmentState.badgeNumber = 1

                    tabLayout1.getTabAt(fragmentState.badgePos!!)?.orCreateBadge?.apply {
                        number = fragmentState.badgeNumber
                        badgeGravity = BadgeDrawable.TOP_END
                        activity.toast("Бейджик установлен на страницу № ${fragmentState.badgePos!! + 1}")
                    }

                } else {
                    tabLayout1.getTabAt(fragmentState.badgePos!!)?.orCreateBadge?.apply {
                        fragmentState.badgeNumber = fragmentState.badgeNumber + 1
                        number = fragmentState.badgeNumber
                        activity.toast("Счетчик бейджика увеличен на 1 ==>  ${number}")
                    }
                }
            }
        }
    }
}

