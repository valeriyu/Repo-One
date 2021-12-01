package com.valeriyu.testappforcustomcontentprovider

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class CoursesViewModel(application: Application) : AndroidViewModel(application) {


    private val coursesRepository = CoursesRepository(application)

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable)
    }

    private val coroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default + errorHandler)

    private val errorLiveEvent = SingleLiveEventK<String>("")
    val errorLiveData: SingleLiveEventK<String>
        get() = errorLiveEvent

    /*  private var currСourseLiveData = MutableLiveData<Course>()
      val currСourse: LiveData<Course>
          get() = currСourseLiveData*/

    private var currСourseLiveData = SingleLiveEventK<Course?>(null)
    val currСourse: SingleLiveEventK<Course?>
        get() = currСourseLiveData

    private var coursesListLiveData = MutableLiveData<List<Course>>()
    val coursesList: LiveData<List<Course>>
        get() = coursesListLiveData

    /* private val toastLiveEvent = SingleLiveEventK<String>("")
     val error: SingleLiveEventK<String>
         get() = toastLiveEvent*/

    private val isLoadingLiveData = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    private fun onError(throwable: Throwable) {
        errorLiveEvent.postValue(throwable.message)
        isLoadingLiveData.postValue(false)
        currСourseLiveData.postValue(null)
        coursesListLiveData.postValue(emptyList())
    }

    fun getAllCourses() {
        coroutineScope.launch {
            isLoadingLiveData.postValue(true)
            var newList = coursesRepository.getAllCourses()
            coursesListLiveData.postValue(newList)
            isLoadingLiveData.postValue(false)
        }
    }

    fun getCourseById(couseId: Long) {
        if (couseId == 0L) {
            currСourseLiveData.postValue(null)
            return
        }
        coroutineScope.launch {
            isLoadingLiveData.postValue(true)
            var course = coursesRepository.getCourseById(couseId)
            currСourseLiveData.postValue(course)
            isLoadingLiveData.postValue(false)
        }
    }

    fun addCourse(title: String) {
        coroutineScope.launch {
            isLoadingLiveData.postValue(true)
            coursesRepository.addCourse(title)
            getAllCourses()
            isLoadingLiveData.postValue(false)
        }
    }

    fun deleteCourse(couseId: Long) {
        coroutineScope.launch {
            coursesRepository.deleteCourse((couseId))
            getAllCourses()
            isLoadingLiveData.postValue(false)
        }
    }

    fun updateCourse(couseId: Long, title: String) {
        coroutineScope.launch {
            isLoadingLiveData.postValue(true)
            coursesRepository.updateCourse(couseId, title)
            getAllCourses()
            isLoadingLiveData.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.coroutineContext.cancelChildren()
    }
}