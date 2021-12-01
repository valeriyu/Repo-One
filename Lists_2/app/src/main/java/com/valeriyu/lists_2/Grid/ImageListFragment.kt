package com.valeriyu.lists_2.Grid

import android.os.Bundle
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.valeriyu.lists_2.R
import com.valeriyu.lists_2.adapters.ImageAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.fragment_image_list.*

private const val ARG_PARAM1 = "selector"

class ImageListFragment : Fragment(R.layout.fragment_image_list) {

    private var selector: Int? = null
    private var imageAdapter: ImageAdapter? = null
    private var images = emptyList<MyImages>()


    private val urls = listOf(
        "https://krivoe-zerkalo.ru/images/thumbnails/images/2018_1/IVmheKXegrY-fit-1200x788.jpg",
        "https://i0.wp.com/dronomania.ru/wp-content/uploads/2016/12/%D0%94%D1%80%D0%BE%D0%BD%D1%8B-USA.png",
        "https://avatars.mds.yandex.net/get-zen_doc/52716/pub_59f0d0057ddde894b72cd06f_59f0db244bf1610270f47a98/scale_1200",
        "https://s3-eu-west-1.amazonaws.com/files.surfory.com/uploads/2015/3/30/54dce0da1f395de3098b463f/55195dc61f395d0c0e8b4614.jpg",
        "https://anband.spb.ru/images/200/DSC100222318.jpg",
        "https://www.tripsoul.ru/Destinations/IMG_Australia-Oceania/Australia/Melbourne/Melbourne_02.jpg",
        "https://mykaleidoscope.ru/uploads/posts/2020-01/1579933117_30-p-shokoladnie-torti-71.jpg",
        "https://i.pinimg.com/736x/02/cc/77/02cc771415ccb6f9825f88288ccd78bc--owl.jpg",
        "https://cdn.pixabay.com/photo/2019/10/02/07/50/rhino-4520317_1280.jpg"
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            selector = it.getInt(ARG_PARAM1)
        }
        selector?.let { initList(it) }
    }

    private fun initList(selector: Int) = with(imageList) {

        urls.forEach{
            images +=  MyImages.Image(
                imageUrl = it
            )
        }

        imageAdapter = ImageAdapter()
        imageAdapter?.items =
            (images.shuffled() + images.shuffled() + images.shuffled() + images.shuffled())
        adapter = imageAdapter
        itemAnimator = ScaleInAnimator()

        setHasFixedSize(true)

        when (selector) {
            0 -> {
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
            1 -> {
                layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                addItemDecoration(ItemOffsetDecoration(requireContext()))
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        imageAdapter = null
    }


    companion object {

        @JvmStatic
        fun newInstance(sel: Int) =
            ImageListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, sel)
                }
            }
    }
}