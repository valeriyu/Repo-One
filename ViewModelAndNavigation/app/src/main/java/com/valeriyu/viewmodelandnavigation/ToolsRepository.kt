package com.valeriyu.viewmodelandnavigation

class ToolsRepository {

    // private var tools = emptyList<Tools>()

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

    fun addItem(tools: List<Tools>): List<Tools> {
        var tmpList =  tools
        tmpList += tools0.random()
        return tmpList
    }

    fun loadMore(tools: List<Tools>, totalItemsCount: Int): List<Tools> {
        var tmpList =  tools
        if (totalItemsCount >= MAX_ITEMS_COUNT) {
            return tools
        }

        for (i in 0..9) {
            if (tools.size >= MAX_ITEMS_COUNT) {
                break
            }
            tmpList += tools0.random()
        }
        return tmpList
    }

    fun generateToolsList(): List<Tools> {
        var tools = emptyList<Tools>()
        for (i in 0..9) {
            tools += tools0.random()
        }
        return tools
    }

    fun deleteItem(tools: List<Tools>, position: Int): List<Tools> {
        return tools.filterIndexed { index, _ -> index != position }
    }

}