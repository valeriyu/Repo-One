package com.valeriyu.a01_basicsyntax

fun main() {
    var firstName: String = "Валерий"
    var lastName: String = "Гридин"
    var height: Int = 176
    var weight: Float = 80.4F
    var isChild: Boolean = checIsChild(height, weight)

    var info: String = "Имя: $firstName, Фамилия: $lastName, Рост: $height см. Вес: $weight кг. Это ${if(isChild) "Ребенок" else "Взрослый"}"
    println(info)

    height = 149
    isChild = checIsChild(height, weight)

    info = "Имя: $firstName, Фамилия: $lastName, Рост: $height см. Вес: $weight кг. Это ${if(isChild) "Ребенок" else "Взрослый"}"
    println(info)
}

fun checIsChild(height: Int, weight: Float) = (height < 150) || (weight < 40)