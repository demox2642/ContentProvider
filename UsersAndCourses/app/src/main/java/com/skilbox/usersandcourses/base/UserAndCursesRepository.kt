package com.skilbox.usersandcourses.base

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class UserAndCursesRepository(
    private val context: Context
) {

    companion object {
        private const val AVTORITES = "com.skilbox.contentprovider"

        private const val PROVIDER_NAME = ".provaider.SkillboxContentProvider"
        private const val URL_USER = "content://$AVTORITES/$PROVIDER_NAME/users"
        private val CONTENT_USER_URI = Uri.parse(URL_USER)

        private const val URL_COURSES = "content://$AVTORITES/$PROVIDER_NAME/courses"
        private val CONTENT_COURSES_URI = Uri.parse(URL_COURSES)

        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "name"
        const val COLUMN_USER_AGE = "age"

        const val COLUMN_COURSES_ID = "id"
        const val COLUMN_COURSES_TITLE = "title"
    }

    suspend fun getCoursesById(corses_id: Long): Course = withContext(Dispatchers.IO) {
        Log.e("getCoursesById", "$corses_id")
        val uri = ContentUris.withAppendedId(CONTENT_COURSES_URI, corses_id)
        context.contentResolver.query(
            uri,
            null,
            "$COLUMN_COURSES_ID= ?",
            arrayOf(arrayOf(corses_id).toString()),
            null
        )?.use {
            getCoursesFromCursorById(cursor = it)
        }!!
    }

    private fun getCoursesFromCursorById(cursor: Cursor): Course {
        if (cursor.moveToFirst().not()) null
        var list: Course
        do {
            val titleIndex = cursor.getColumnIndex(COLUMN_COURSES_TITLE)
            val title = cursor.getString(titleIndex).orEmpty()

            val idIndex = cursor.getColumnIndex(COLUMN_COURSES_ID)
            val id = cursor.getLong(idIndex)

            list =
                Course(
                    id = id,
                    title = title
                )
        } while (cursor.moveToNext())
        Log.e("getContactsFromCursor", "LIST: $list")
        return list
    }

    suspend fun getAllCourses(): List<Course> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            CONTENT_COURSES_URI,
            arrayOf(
                COLUMN_COURSES_ID,
                COLUMN_COURSES_TITLE
            ),
            null,
            null,
            COLUMN_COURSES_ID
        )?.use {
            getCoursesFromCursor(cursor = it)
        }.orEmpty()
    }

    private fun getCoursesFromCursor(cursor: Cursor): List<Course> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<Course>()
        do {
            val titleIndex = cursor.getColumnIndex(COLUMN_COURSES_TITLE)
            val title = cursor.getString(titleIndex).orEmpty()

            val idIndex = cursor.getColumnIndex(COLUMN_COURSES_ID)
            val id = cursor.getLong(idIndex)

            list.add(
                Course(
                    id = id,
                    title = title
                )
            )
        } while (cursor.moveToNext())
        Log.e("getContactsFromCursor", "LIST: $list")
        return list
    }

    suspend fun saveCourse(title: String) = withContext(Dispatchers.IO) {
        Log.e("saveCourse", "title: $title")

        val courseId = saveRawCourse()
        saveCourseValue(courseId, title)
    }

    private fun saveRawCourse(): Long {

        return Random.nextLong(0, 100000)
    }

    fun deleteCourse(corses_id: Long) {
        Log.e("deleteCourse", "corses_id = $corses_id")

        val uri = ContentUris.withAppendedId(CONTENT_COURSES_URI, corses_id)

        context.contentResolver.delete(
            uri,
            "$COLUMN_COURSES_ID = ?",
            arrayOf(corses_id.toString())
        )
    }

    fun deleteAllCourses() {

        context.contentResolver.delete(
            CONTENT_COURSES_URI,
            null,
            null
        )
    }

    fun updeteCourse(courseId: Long, title: String) {

        val contentValues = ContentValues().apply {
            put(COLUMN_COURSES_TITLE, title)
        }

        val uri = ContentUris.withAppendedId(CONTENT_COURSES_URI, courseId)

        Log.e("updeteCourse", "uri=$uri")
        context.contentResolver.update(
            uri,
            contentValues,
            "$COLUMN_COURSES_ID= ?",
            arrayOf(courseId.toString())
        )
    }

    private fun saveCourseValue(courseId: Long, title: String) {
        Log.e("Repository", "courseId=$courseId  title=$title ")
        val contentValues = ContentValues().apply {
            put(COLUMN_COURSES_ID, courseId)
            put(COLUMN_COURSES_TITLE, title)
        }
        context.contentResolver.insert(CONTENT_COURSES_URI, contentValues)
    }

    suspend fun getAllUsers(): List<Users> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            CONTENT_USER_URI,
            arrayOf(
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_AGE
            ),
            null,
            null,
            null
        )?.use {
            getUsersFromCursor(cursor = it)
        }.orEmpty()
    }

    private fun getUsersFromCursor(cursor: Cursor): List<Users> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<Users>()
        do {
            val nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME)
            val name = cursor.getString(nameIndex).orEmpty()

            val idIndex = cursor.getColumnIndex(COLUMN_USER_ID)
            val id = cursor.getLong(idIndex)

            val ageIndex = cursor.getColumnIndex(COLUMN_USER_AGE)
            val age = cursor.getInt(ageIndex)

            list.add(
                Users(
                    id = id,
                    name = name,
                    age = age
                )
            )
        } while (cursor.moveToNext())
        Log.e("getUsersFromCursor", "LIST: $list")
        return list
    }

    suspend fun saveUser(name: String, age: Int) = withContext(Dispatchers.IO) {
        Log.e("saveContact", "name: $name  phone:$age")

        val userId = saveRawUser()
        saveUserValue(userId, name, age)
    }

    private fun saveUserValue(userId: Long, name: String, age: Int) {
        Log.e("Repository", "userId=$userId  name=$name  age=$age")
        val contentValues = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_USER_NAME, name)
            put(COLUMN_USER_AGE, age)
        }
        context.contentResolver.insert(CONTENT_USER_URI, contentValues)
    }

    private fun saveRawUser(): Long {

        return Random.nextLong()
    }
}
