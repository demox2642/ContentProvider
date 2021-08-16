package com.skilbox.contentprovider.provaider

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.skilbox.contentprovider.BuildConfig
import com.skilbox.contentprovider.provaider.data.Course
import com.skilbox.contentprovider.provaider.data.Users
import com.squareup.moshi.Moshi

class SkillboxContentProvider : ContentProvider() {
    private lateinit var userPreferences: SharedPreferences
    private lateinit var coursesPreferences: SharedPreferences

    private val userAdapter = Moshi.Builder().build().adapter(Users::class.java)
    private val courseAdapter = Moshi.Builder().build().adapter(Course::class.java)

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITEST, "$PROVIDER_PATH/$PATH_USERS", TYPE_USER)
        addURI(AUTHORITEST, "$PROVIDER_PATH/$PATH_COURSES", TYPE_COURSES)
        addURI(AUTHORITEST, "$PROVIDER_PATH/$PATH_COURSES" + "/#", TYPE_COURSES_ID)
    }

    override fun onCreate(): Boolean {
        userPreferences = context!!.getSharedPreferences("user_sp", Context.MODE_PRIVATE)
        coursesPreferences = context!!.getSharedPreferences("courses_sp", Context.MODE_PRIVATE)

        Log.e("ContentProvider", "deployment DB is completed")
        return true
    }

    override fun query(
        uri: Uri,
        progection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            TYPE_USER -> getAllUsersCursor()
            TYPE_COURSES -> getAllCoursesCursor()
            TYPE_USER_ID -> getUserCursor()
            TYPE_COURSES_ID -> {
                Log.e("QweryCOURSE_ID", "I'am hear")

                getCourseByID(uri)
            }
            else -> null
        }
    }

    private fun getCourseByID(uri: Uri): Cursor {
        val courseId = uri.lastPathSegment?.toLongOrNull().toString().toLong()
        val course = coursesPreferences.all.mapNotNull {
            val courseJsonString = it.value as String
            courseAdapter.fromJson(courseJsonString)
        }.find { it.id == courseId }

        val cursor = MatrixCursor(arrayOf(COLUMN_COURSES_ID, COLUMN_COURSES_TITLE))

        listOf(course).forEach {
            cursor.newRow()
                .add(it!!.id)
                .add(it!!.title)
        }
        return cursor
    }

    private fun getAllUsersCursor(): Cursor {
        val allUsers = userPreferences.all.mapNotNull {
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

    private fun getAllCoursesCursor(): Cursor {
        val allCourses = coursesPreferences.all.mapNotNull {
            val courseJsonString = it.value as String
            courseAdapter.fromJson(courseJsonString)
        }

        val cursor = MatrixCursor(arrayOf(COLUMN_COURSES_ID, COLUMN_COURSES_TITLE))
        allCourses.forEach {
            cursor.newRow()
                .add(it.id)
                .add(it.title)
        }
        return cursor
    }

    private fun getUserCursor(): Cursor {
        val allUsers = userPreferences.all.mapNotNull {
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

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        p1 ?: return null
        Log.e("I see uri", "$p0  and cv=$p1")
        return when (uriMatcher.match(p0)) {

            TYPE_USER -> saveUserValues(p1)
            TYPE_COURSES -> saveCoursesValue(p1)
            else -> null
        }
    }

    private fun saveCoursesValue(contentValues: ContentValues): Uri? {
        val id = contentValues.getAsLong(COLUMN_COURSES_ID) ?: return null
        val title = contentValues.getAsString(COLUMN_COURSES_TITLE) ?: return null

        val course = Course(id, title)
        coursesPreferences.edit()
            .putString(id.toString(), courseAdapter.toJson(course))
            .apply()
        return Uri.parse("content://$AUTHORITEST/$PATH_COURSES/$id")
    }

    private fun saveUserValues(contentValues: ContentValues): Uri? {
        val id = contentValues.getAsLong(COLUMN_USER_ID) ?: return null
        val name = contentValues.getAsString(COLUMN_USER_NAME) ?: return null
        val age = contentValues.getAsInteger(COLUMN_USER_AGE) ?: return null
        val user = Users(id, name, age)
        userPreferences.edit()
            .putString(id.toString(), userAdapter.toJson(user))
            .apply()
        return Uri.parse("content://$AUTHORITEST/$PATH_USERS/$id")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.e("deleteFun", "p0=$uri|| p1=$selection|| p2=$selectionArgs\n ${uriMatcher.match(uri)}")

        return when (uriMatcher.match(uri)) {
            TYPE_USER_ID -> deleteUser(uri)
            TYPE_COURSES -> cleanCourse()
            TYPE_COURSES_ID -> deleteCourses(uri)
            else -> 0
        }
    }

    fun cleanCourse(): Int {
        coursesPreferences.edit()
            .clear()
            .apply()
        return 1
    }

    private fun deleteUser(uri: Uri): Int {
        val userId = uri.lastPathSegment?.toLongOrNull().toString() ?: return 0
        return if (userPreferences.contains(userId)) {
            userPreferences.edit()
                .remove(userId)
                .apply()
            1
        } else {
            0
        }
    }

    private fun deleteCourses(uri: Uri): Int {
        Log.e("deleteCourses1", "uri:$uri")
        val courseId = uri.lastPathSegment?.toLongOrNull().toString() ?: return 0

        val userId = uri.lastPathSegment?.toLongOrNull().toString() ?: return 0
        Log.e("deleteCourses2", "${coursesPreferences.contains(courseId)}")
        return if (coursesPreferences.contains(userId)) {
            coursesPreferences.edit()
                .remove(userId)
                .apply()
            return 1
        } else {
            0
        }
    }

    override fun update(uri: Uri, cv: ContentValues?, p2: String?, p3: Array<out String>?): Int {

        cv ?: return 0
        return when (uriMatcher.match(uri)) {
            TYPE_USER_ID -> updateUser(uri, cv)
            TYPE_COURSES_ID -> updateCourses(uri, cv)

            else -> 0
        }
    }

    private fun updateCourses(uri: Uri, contentValues: ContentValues): Int {
        val courseId = uri.lastPathSegment?.toLongOrNull().toString() ?: return 0
        val title = contentValues.getAsString(COLUMN_COURSES_TITLE) ?: return 0

        val contentValues = ContentValues().apply {
            put(COLUMN_COURSES_ID, courseId)
            put(COLUMN_COURSES_TITLE, title)
        }
        return if (coursesPreferences.contains(courseId)) {
            saveCoursesValue(contentValues)
            1
        } else {
            0
        }
    }

    private fun updateUser(uri: Uri, contentValues: ContentValues): Int {
        val userId = uri.lastPathSegment?.toLongOrNull().toString() ?: return 0
        return if (userPreferences.contains(userId)) {
            saveUserValues(contentValues) // но это не точно!!!!
            1
        } else {
            0
        }
    }

    companion object {
        const val AUTHORITEST = "${BuildConfig.APPLICATION_ID}"
        const val PROVIDER_PATH = ".provaider.SkillboxContentProvider"
        const val PATH_USERS = "users"
        const val PATH_COURSES = "courses"

        private const val TYPE_USER = 1
        private const val TYPE_COURSES = 2
        private const val TYPE_USER_ID = 3
        private const val TYPE_COURSES_ID = 4

        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "name"
        const val COLUMN_USER_AGE = "age"

        const val COLUMN_COURSES_ID = "id"
        const val COLUMN_COURSES_TITLE = "title"
    }
}
