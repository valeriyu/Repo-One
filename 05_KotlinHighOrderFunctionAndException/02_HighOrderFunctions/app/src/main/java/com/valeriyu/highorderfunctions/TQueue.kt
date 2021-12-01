package com.valeriyu.highorderfunctions

class TQueue<T>() {

    var  q:MutableList<T> = mutableListOf<T>()

    fun enqueue(item:T) {
        q.add(item)
    }

    fun dequeue():T? {
        if (q.size == 0) return null
        return q.removeAt(0)
    }


    fun filter(cbFun:() -> Unit):TQueue<T>{
        cbFun()

        var _TQueue = TQueue<T>()
        _TQueue.q.addAll(q)
        return _TQueue
    }

}