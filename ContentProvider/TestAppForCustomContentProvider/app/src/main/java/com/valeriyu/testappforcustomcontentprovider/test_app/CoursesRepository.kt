package com.valeriyu.testappforcustomcontentprovider

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

var LOG_TAG = "CoursesRepository"

class CoursesRepository(private val context: Context) {

    val COURSES_URI: Uri =
        Uri.parse("content://com.valeriyu.contentprovider.courseses_content_provider/courses")

    fun getAllCourses(): List<Course> {
        val list = mutableListOf<Course>()

        var c = context.contentResolver.query(
            COURSES_URI,
            null,
            null,
            null,
            null
        )
        if (c == null) {
            error("Ресурс - $COURSES_URI Не найден !!!")
        } else {
            c.use {
                if (it.moveToFirst().not()) {
                    return@use emptyList<Course>()
                } else {
                    do {
                        list.add(
                            Course(
                                id = it.getLong(0),
                                title = it.getString(1).orEmpty()
                            )
                        )
                    } while (it.moveToNext())
                }
            }
            return list
        }
    }

    fun getCourseById(courseId: Long): Course? {
        var res: Course? = null
        var uri =
            Uri.parse("content://com.valeriyu.contentprovider.courseses_content_provider/courses/${courseId}")
        var c = context.contentResolver.query(
            uri,
            null,
            null,
            null,
            null,
            null
        )
        if (c == null) {
            error("Ресурс: $uri - Не найден !!!")
        } else {
            c.use {
                if (it.moveToFirst().not()) {
                    return@use null
                }
                res = Course(
                    id = it.getLong(0),
                    title = it.getString(1).orEmpty()
                )
            }
        }
        return res
    }

    fun addCourse(title: String) {
        val cv = ContentValues()
        var newUri: Uri?
        var newId = getAllCourses().map {
            it.id
        }.maxOrNull()?.plus(1) ?: 1

        cv.put("id", newId)
        cv.put("title", title)
        newUri = context.contentResolver.insert(COURSES_URI, cv)
        Log.d(LOG_TAG, "insert, result Uri : $newUri")
        if(newUri == null){
            error("Ресурс - $COURSES_URI Не найден !!!")
        }
    }

    suspend fun deleteCourse(courseId: Long) {
        withContext(Dispatchers.IO) {
            var uri =
                Uri.parse("content://com.valeriyu.contentprovider.courseses_content_provider/courses/${courseId}")
            context.contentResolver.delete(
                uri,
                null,
                null
            )
        }
    }

    suspend fun updateCourse(courseId: Long, title: String) {
        withContext(Dispatchers.IO) {
            val cv = ContentValues()
            cv.put("id", courseId)
            cv.put("title", title)
            var uri =
                Uri.parse("content://com.valeriyu.contentprovider.courseses_content_provider/courses/${courseId}")
            context.contentResolver.update(
                uri,
                cv,
                null,
                null
            )
        }
    }
}