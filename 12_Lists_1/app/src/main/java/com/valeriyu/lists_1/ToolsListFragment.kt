package com.valeriyu.lists_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_tools_list.*

class ToolsListFragment : Fragment(R.layout.fragment_tools_list), FragmentOnClickListner {

    private var tools0 = listOf(
        Tools.ProflTool(
            name = "Дрель-шуруповерт DeWALT DCD 771 C2",
            pictLink = "https://cdn.vseinstrumenti.ru/images/goods/instrument/shurupoverty/698641/1200x800/9795260.jpg",
            description = "Аккумуляторная дрель-шуруповерт DeWalt DCD 771 C2 предназначена для работ с крепежными элементами и сверления отверстий. Двухскоростной редуктор упрощает ведение различных работ: первая скорость предназначена для заворачивания шурупов, вторая - для сверления.\n" +
                    "Муфта регулировки крутящего момента позволяет максимально точно настроить момент затяжки под конкретный материал, всего на выбор имеется 16 позиций.\n",
            weight = 1.65
        ),
        Tools.Tool(
            name = "Дрель-шуруповерт Black+Decker BDCDD186KB ",
            pictLink = "https://cdn.vseinstrumenti.ru/images/goods/instrument/shurupoverty/834015/1200x800/9799546.jpg",
            description = "Аккумуляторная дрель-шуруповерт Black&Decker BDCDD186KB оснащена двухскоростным редуктором и имеет 10 ступеней крутящего момента, что расширяет сферу применения. С помощью тумблера сверху на корпусе можно выбрать одну из двух рабочих скоростей. Благодаря быстрозажимному патрону, смена оснастки не занимает много времени. В зависимости от плотности материала можно выбрать один из десяти режимов крутящего момента.\n"
        ),
        Tools.ProflTool(
            name = "Дисковая пила Makita M5802 ",
            pictLink = "https://cdn.vseinstrumenti.ru/images/goods/instrument/pily/1162157/1200x800/10478630.jpg",
            description = "Дисковая пила Makita M5802 используется для быстрого и аккуратного пиления заготовок по прямой.\n" +
                    "Возможность подключения пылесоса поставляет содержать рабочее пространство в чистоте.\n" +
                    "Для удобного выполнения точных пропилов, инструмент оснащен параллельным упором.\n" +
                    "Легкий доступ к угольным щеткам предусматривает их самостоятельную замену при износе без обращения в сервисные центры.\n",
            weight = 5.17

        ),
        Tools.Tool(
            name = "Дисковая пила Bosch PKS 55 0.603.500.020 ",
            pictLink = "https://cdn.vseinstrumenti.ru/images/goods/instrument/pily/70281/1200x800/5510572.jpg",
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

    //private var toolsAdapter: ToolsAdapter? = null
    protected var toolsAdapter by AutoClearedValue<ToolsAdapter?>()
    //protected var choiseToolsFragmen by AutoClearedValue<ChoiseToolsFragment>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

 /*       if(savedInstanceState == null) {
            tools = emptyList<Tools>()
        }*/

        initList()

        addFab.setOnClickListener {
            var toolNames = emptyArray<CharSequence>()
            tools0.forEach {
                if( it is Tools.Tool) toolNames += it.name
                if( it is Tools.ProflTool) toolNames += it.name
            }
            toolNames += "Несуществующая ссылка"


            ChoiseToolsFragment.newInstance(toolNames)
                .show(childFragmentManager, "choiseToolsFragment")
        }
        toolsAdapter?.updateTools(tools)
        toolsAdapter?.notifyDataSetChanged()
    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        //oolsAdapter = null
    }*/

    private fun initList() {
        toolsAdapter = ToolsAdapter ({  position -> deleteItem(position) })

        with(toolsList) {
            adapter = toolsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        updStatus()
    }

    private fun deleteItem(position: Int) {
        tools = tools.filterIndexed { index, _ -> index != position }
        toolsAdapter?.updateTools(tools)
        toolsAdapter?.notifyItemRemoved(position)
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
    }

    override fun buttonOnClickListener(selector: Int) {
        if (selector > tools0.size - 1) {

            var newItem = tools0.random()

            when (newItem){
                is Tools.Tool -> newItem.pictLink = "https://127.0.0.1/9795260.jpg"
                is Tools.ProflTool -> newItem.pictLink = "https://127.0.0.1/9795260.jpg"
            }

            tools = listOf(newItem) + tools
            toolsAdapter?.updateTools(tools)
            toolsAdapter?.notifyItemInserted(0)
            toolsList.scrollToPosition(0)
            updStatus()
        } else {
            val newItem = tools0[selector]
            tools = listOf(newItem) + tools
            toolsAdapter?.updateTools(tools)
            toolsAdapter?.notifyItemInserted(0)
            toolsList.scrollToPosition(0)
            updStatus()
        }
    }

    companion object {
        @JvmStatic

        private var tools = emptyList<Tools>()

        fun newInstance() =
            ToolsListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
