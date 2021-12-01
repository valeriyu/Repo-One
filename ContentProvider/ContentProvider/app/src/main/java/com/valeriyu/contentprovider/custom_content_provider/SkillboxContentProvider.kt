package com.valeriyu.contentprovider.custom_content_provider

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.squareup.moshi.Moshi
import com.valeriyu.contentprovider.BuildConfig


class SkillboxContentProvider : ContentProvider() {

    private lateinit var userPrefs: SharedPreferences
    private lateinit var coursesPrefs: SharedPreferences

    private val userAdapter = Moshi.Builder().build().adapter(User::class.java)
    private val courseAdapter = Moshi.Builder().build().adapter(Course::class.java)

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITIES, PATH_USERS, TYPE_USERS)
        addURI(AUTHORITIES, PATH_COURSES, TYPE_COURSES)
        addURI(AUTHORITIES, "$PATH_USERS/#", TYPE_USER_ID)
        addURI(AUTHORITIES, "$PATH_COURSES/#", TYPE_COURSE_ID)
    }

    override fun onCreate(): Boolean {
        userPrefs = context!!.getSharedPreferences("user_shared_prefs", Context.MODE_PRIVATE)
        coursesPrefs = context!!.getSharedPreferences("course_shared_prefs", Context.MODE_PRIVATE)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        values ?: return null
        return when (uriMatcher.match(uri)) {
            TYPE_USERS -> saveUser(values)
            TYPE_COURSES -> {
                val id = values.getAsLong(COLUMN_COURSE_ID) ?: return null
                val title = values.getAsString(COLUMN_COURSE_TITLE) ?: return null
                coursesPrefs
                    .edit()
                    .putString(id.toString(), courseAdapter.toJson(Course(id, title)))
                    .commit()
                Uri.parse("content://$AUTHORITIES/$PATH_COURSES/$id")
            }
            else -> null
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            TYPE_USER_ID -> deleteUser(uri)
            TYPE_COURSES -> {
                var count = coursesPrefs.all.size
                if (coursesPrefs
                        .edit()
                        .clear()
                        .commit()
                ) {
                    count
                } else {
                    0
                }
            }
            TYPE_COURSE_ID -> {
                val id = uri.lastPathSegment?.toLongOrNull()?.toString() ?: return 0
                coursesPrefs
                    .edit()
                    .remove(id)
                    .commit()
                1
            }
            else -> 0
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        values ?: return 0
        return when (uriMatcher.match(uri)) {
            TYPE_USER_ID -> updateUser(uri, values)
            TYPE_COURSE_ID -> {
                val id = uri.lastPathSegment?.toLongOrNull() ?: return 0
                //val id = values.getAsString(COLUMN_COURSE_ID) ?: return 0
                val title = values.getAsString(COLUMN_COURSE_TITLE) ?: return 0
                coursesPrefs
                    .edit()
                    .putString(id.toString(), courseAdapter.toJson(Course(id.toLong(), title)))
                    .commit()
                1
            }
            else -> 0
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            TYPE_USERS -> getAllUsersCursor()
            TYPE_COURSE_ID -> {
                val id = uri.lastPathSegment?.toLongOrNull()?.toString() ?: 0L
                val jsonString = coursesPrefs.getString(id.toString(), "")
                var course = courseAdapter.fromJson(jsonString.orEmpty())

                val title = course?.title.orEmpty()
                val cursor = MatrixCursor(arrayOf(COLUMN_COURSE_ID, COLUMN_COURSE_TITLE))
                cursor.newRow()
                    .add(id)
                    .add(title)
                cursor
            }
            TYPE_COURSES -> {
                val allCourses = coursesPrefs.all.mapNotNull {
                    val jsonString = it.value as String
                    courseAdapter.fromJson(jsonString)
                }

                val cursor = MatrixCursor(arrayOf(COLUMN_COURSE_ID, COLUMN_COURSE_TITLE))
                allCourses.forEach {
                    cursor.newRow()
                        .add(it.id)
                        .add(it.title)
                }
                cursor
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

//=========================================================================================

    private fun getAllUsersCursor(): Cursor {
        val allUsers = userPrefs.all.mapNotNull {
            val userJsonString = it.value as String
            userAdapter.fromJson(userJsonString)
        }

        val cursor = MatrixCursor(arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_AGE))
        allUsers.forEach {
            cursor.newRow()
                .add(it.id)
                .add(it.name)
                .add(it.age)
        }
        return cursor
    }

    private fun saveUser(contentValues: ContentValues): Uri? {
        val id = contentValues.getAsLong(COLUMN_USER_ID) ?: return null
        val name = contentValues.getAsString(COLUMN_USER_NAME) ?: return null
        val age = contentValues.getAsInteger(COLUMN_USER_AGE) ?: return null
        val user = User(id, name, age)
        userPrefs.edit()
            .putString(id.toString(), userAdapter.toJson(user))
            .commit()
        return Uri.parse("content://$AUTHORITIES/$PATH_USERS/$id")
    }

    private fun deleteUser(uri: Uri): Int {
        val userId = uri.lastPathSegment?.toLongOrNull()?.toString() ?: return 0
        return if (userPrefs.contains(userId)) {
            userPrefs.edit()
                .remove(userId)
                .commit()
            1
        } else {
            0
        }
    }

    private fun updateUser(uri: Uri, contentValues: ContentValues): Int {
        val userId = uri.lastPathSegment?.toLongOrNull()?.toString() ?: return 0
        return if (userPrefs.contains(userId)) {
            saveUser(contentValues)
            1
        } else {
            0
        }
    }

    companion object {
        private const val AUTHORITIES = "${BuildConfig.APPLICATION_ID}.courseses_content_provider"
        private const val PATH_USERS = "users"
        private const val PATH_COURSES = "courses"

        private const val TYPE_USERS = 1
        private const val TYPE_COURSES = 2
        private const val TYPE_USER_ID = 3
        private const val TYPE_COURSE_ID = 4

        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_NAME = "name"
        private const val COLUMN_USER_AGE = "age"

        private const val COLUMN_COURSE_ID = "id"
        private const val COLUMN_COURSE_TITLE = "title"

        const val TABLE_NAME = "courses"
        const val CONTENT_AUTHORITY = AUTHORITIES
        val CONTENT_AUTHORITY_URI = Uri.parse("content://$CONTENT_AUTHORITY")

        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
        const val CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    }
}