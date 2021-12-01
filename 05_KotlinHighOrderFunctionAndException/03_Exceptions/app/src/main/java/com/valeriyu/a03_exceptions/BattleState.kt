//package com.valeriyu.module4
package com.valeriyu.a03_exceptions

sealed class BattleState(var res:String){
    object Progress : BattleState(res = "")
    object Draw : BattleState("Ничья !!!")
    object victoryTeamA : BattleState("Команда A одержала победу !!!")
    object victoryTeamB : BattleState("Команда B одержала победу !!!")
}