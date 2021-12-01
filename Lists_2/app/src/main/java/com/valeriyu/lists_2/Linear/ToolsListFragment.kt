package com.valeriyu.lists_2.Linear

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valeriyu.lists_2.EndlessRecyclerViewScrollListener
import com.valeriyu.lists_2.R
import com.valeriyu.lists_2.adapters.ToolsAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.fragment_tools_list.*

private const val ARG_PARAM1 = "selector"
const val MAX_ITEMS_COUNT = 50

class ToolsListFragment() :
    Fragment(R.layout.fragment_tools_list) {

    private var selector: Int? = null

    private var toolsAdapter: ToolsAdapter? = null
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private var tools0 = listOf(
        Tools.ProflTool(
            // id = 1,
            name = "Дрель-шуруповерт DeWALT DCD 771 C2",
            pictLink = "https://cdn.vseinstrumenti.ru/images/goods/instrument/shurupoverty/698641/1200x800/52002189.jpg",
            description = "Аккумуляторная дрель-шуруповерт DeWalt DCD 771 C2 предназначена для работ с крепежными элементами и сверления отверстий. Двухскоростной редуктор упрощает ведение различных работ: первая скорость предназначена для заворачивания шурупов, вторая - для сверления.\n" +
                    "Муфта регулировки крутящего момента позволяет максимально точно настроить момент затяжки под конкретный материал, всего на выбор имеется 16 позиций.\n",
            weight = 1.65
        ),
        Tools.Tool(
            // id = 2,
            name = "Дрель-шуруповерт Black+Decker BDCDD186KB ",
            pictLink = "https://cdn.vseinstrumenti.ru/images/goods/instrument/shurupoverty/834015/1200x800/51660875.jpg",
            description = "Аккумуляторная дрель-шуруповерт Black&Decker BDCDD186KB оснащена двухскоростным редуктором и имеет 10 ступеней крутящего момента, что расширяет сферу применения. С помощью тумблера сверху на корпусе можно выбрать одну из двух рабочих скоростей. Благодаря быстрозажимному патрону, смена оснастки не занимает много времени. В зависимости от плотности материала можно выбрать один из десяти режимов крутящего момента.\n"
        ),
        Tools.ProflTool(
            // id = 3,
            name = "Дисковая пила Makita M5802 ",
            pictLink = "https://cdn.vseinstrumenti.ru/images/goods/instrument/pily/1162157/1200x800/51954785.jpg",
            description = "Дисковая пила Makita M5802 используется для быстрого и аккуратного пиления заготовок по прямой.\n" +
                    "Возможность подключения пылесоса поставляет содержать рабочее пространство в чистоте.\n" +
                    "Для удобного выполнения точных пропилов, инструмент оснащен параллельным упором.\n" +
                    "Легкий доступ к угольным щеткам предусматривает их самостоятельную замену при износе без обращения в сервисные центры.\n",
            weight = 5.17

        ),
        Tools.Tool(
            // id = 4,
            name = "Дисковая пила Bosch PKS 55 0.603.500.020 ",
            pictLink = "https://cdn.vseinstrumenti.ru/images/goods/instrument/pily/70281/1200x800/51069242.jpg",
            description = "Дисковая пила Bosch PKS 55 0.603.500.020 предназначена для обработки древесины, фанеры, ДСП, пластиков." +
                    " Пила применяется для выполнения продольных и поперечных прямых пропилов с углом наклона до 45°, имеет глубину пропила 55 мм под углом 90°  и 38 мм под углом 45°." +
                    " Система CutControl обеспечивает точность при проведении произвольных распилов.\n"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tools_list, container, false)
    }


    private fun initScrollListener() {
        scrollListener = object :
            EndlessRecyclerViewScrollListener(toolsList.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore(page, totalItemsCount, view)
            }

            override fun chLastVisable(lastVisibleItemPosition: Int, totalItemsCount: Int) {
                textViewCounts.text =
                    "  Всего элементов: ${totalItemsCount}"
                textViewLastVis.text = "Последний видимый элем. : ${lastVisibleItemPosition}"
            }
        }
        toolsList.addOnScrollListener(scrollListener)
    }


    private fun enableScrollListener() {

        scrollListener.resetState()

        Handler().postDelayed({
            toolsList.addOnScrollListener(scrollListener)
        }, 100)
    }

    private fun disableScrollListener() {
        toolsList.removeOnScrollListener(scrollListener)
    }


    private fun loadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

        // return

        if (totalItemsCount >= MAX_ITEMS_COUNT) {
            return
        }

        /*   if (noScrollFlag) {
               noScrollFlag = !noScrollFlag
               return
           }*/

        for (i in 0..9) {

            if (tools.size >= MAX_ITEMS_COUNT) {
                break
            }
            tools += tools0.random()
/*
            tools += listOf(
                Tools.Separator(
                )
            )*/
        }

        toolsAdapter!!.items = tools
        updStatus()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            selector = it.getInt(ARG_PARAM1)
        }
        selector?.let { initList(it) }

        addFab.setOnClickListener {
            addFabOnClick()
        }


        Handler()
            .postDelayed(Runnable {
                initScrollListener()
            }, 1000)

        updStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tools = emptyList<Tools>()
        toolsAdapter = null
    }

    private fun initList(sel: Int) {
        toolsAdapter = ToolsAdapter({ position -> deleteItem(position) })

        with(toolsList) {
            adapter = toolsAdapter

            when (sel) {
                0 -> {
                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
                1 -> {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    itemAnimator = ScaleInAnimator()
                }
            }

            // itemAnimator = ScaleInAnimator()
            setHasFixedSize(true)
        }

        for (i in 0..9) {
            tools += tools0.random()
        }

        toolsAdapter?.items = tools
        // toolsList.scrollToPosition(tools.size - 1)
        toolsList.scrollToPosition(1)
        updStatus()
    }

    private fun deleteItem(position: Int) {

        disableScrollListener()

        tools = tools.filterIndexed { index, _ -> index != position }

        //toolsAdapter?.items = emptyList<Tools>()
        //scrollListener.resetState()
        toolsAdapter?.items = tools

        enableScrollListener()
        updStatus()
    }

/*private fun addItem() {
    val newItem = tools0.random()
    tools = listOf(newItem) + tools
    toolsAdapter?.updateTools(tools)
    toolsAdapter?.notifyItemInserted(0)
    toolsList.scrollToPosition(0)
    updStatus()
}*/

    fun updStatus() {

        statusTextView.isVisible = tools.isEmpty()
        val handler = Handler()
        handler.postDelayed(Runnable {
            textViewCounts.text =
                "  Всего элементов: ${toolsList.adapter?.getItemCount()}"
            textViewLastVis.text =
                "Последний видимый элем. : ${(toolsList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()}"
        }, 100)
    }

    fun addFabOnClick() {

        if (toolsAdapter!!.items.size >= MAX_ITEMS_COUNT) {
            return
        }

        disableScrollListener()

        tools = toolsAdapter!!.items

        //toolsAdapter?.items = emptyList<Tools>()
        //scrollListener.resetState()

        tools += tools0.random()
        toolsAdapter!!.items = tools
        toolsList.scrollToPosition(4)

        enableScrollListener()
        updStatus()
    }

    companion object {
        @JvmStatic

        private var tools = emptyList<Tools>()

        fun newInstance(sel: Int = 0) =
            ToolsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, sel)
                }
            }
    }
}

