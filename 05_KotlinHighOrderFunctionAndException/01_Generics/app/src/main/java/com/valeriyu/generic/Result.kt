package com.valeriyu.generic

sealed class Result<out T, R>{
    data class Success<T, R>(var param:T): Result<T, R>() {
        override fun toString(): String {
            return "Success"
        }
    }

    data class Errorval<T, R>(var param:R): Result<T, R>(){
        override fun toString(): String {
            return "Error"
        }
    }
}

fun getResult(testParam:Int):Result<Int, String>{
    if (testParam > 0){
        return  Result.Success(testParam)
    }
    return Result.Errorval("")
}