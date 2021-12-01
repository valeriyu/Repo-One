package com.valeriyu.viewmodelandnavigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToolsViewModel:ViewModel() {

    private val repository = ToolsRepository()

    private val toolsLiveData = MutableLiveData<List<Tools>>(
        repository.generateToolsList()
    )

    private val showToastLiveData = SingleLiveEventK<Unit>()

    val tools: LiveData<List<Tools>>
        get() = toolsLiveData

    val showToast: SingleLiveEventK<Unit>
        get() = showToastLiveData


    fun addItem() {
        //val updatedList = repository.addItem((toolsLiveData.value.orEmpty()))
        toolsLiveData.postValue(repository.addItem((toolsLiveData.value.orEmpty())))
        // showToastLiveData.postValue(Unit)
    }


    fun loadMore(totalItemsCount: Int) {
        toolsLiveData.postValue(repository.loadMore(toolsLiveData.value.orEmpty(), totalItemsCount))
    }


    fun generateToolsList() {
        repository.generateToolsList()
    }

    fun deleteItem(position: Int) {
        val updatedList = repository.deleteItem(toolsLiveData.value.orEmpty(), position)
        toolsLiveData.postValue(updatedList)
        showToastLiveData.postValue(Unit)
    }


    fun getToolsList():List<Tools>{
        return toolsLiveData.value.orEmpty()
    }
}