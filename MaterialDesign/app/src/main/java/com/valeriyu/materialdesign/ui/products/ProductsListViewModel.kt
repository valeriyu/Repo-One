package com.valeriyu.materialdesign.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valeriyu.materialdesign.data.Product
import com.valeriyu.materialdesign.data.ProductsRepository
import com.valeriyu.notifications.SingleLiveEventK

class ProductsListViewModel : ViewModel() {
    private val repository = ProductsRepository()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val errorLiveEvent = SingleLiveEventK<String>("")
    val errorLiveData: SingleLiveEventK<String>
        get() = errorLiveEvent

    fun getProductsList(){
        errorLiveEvent.postValue("Соединение с сервером отсутствует, показаны сохранённые объекты.")
        _products.postValue(repository.productsList)
    }

}