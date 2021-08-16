package com.skilbox.usersandcourses.base


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val context: Context
) {

    private val USER:Uri="content://com.skilbox.contentprovider.provider/courses".toUri()

    suspend fun getUsersList():List<Users> = withContext(Dispatchers.IO){
        context.contentResolver.query(
            CONTENT_USER_URI,
                arrayOf("id","name","age"),
            null,
            null,
            null
        )?.use {
            getUsersForCursor(it)
        }.orEmpty()
    }

    private fun getUsersForCursor(cursor: Cursor):List<Users> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list= mutableListOf<Users>()
        do {
            val idIndex=cursor.getColumnIndex("id")
            val id=cursor.getLong(idIndex)

            val nameIndex=cursor.getColumnIndex("name")
            val name=cursor.getString(nameIndex)

            val ageIndex=cursor.getColumnIndex("age")
            val age=cursor.getInt(ageIndex)

            list.add(
                    Users(id,name,age)
            )
        }while (cursor.moveToNext())
        Log.e("RepositoryUsers","$list")
        return list
    }

    suspend fun saveUser(name: String, age:Int)= withContext(Dispatchers.IO){
        Log.e("saveContact", "name: $name ")

        val userId = saveRawUser()
        saveUserName(userId, name,age)
      //  saveUserAge(userId, age)
    }



    private fun saveRawUser(): Long {
        val uri=context.contentResolver.insert(
            CONTENT_USER_URI,
                ContentValues()
        )
        return uri?.lastPathSegment?.toLongOrNull()?: error("cannot save user")
    }

    private fun saveUserName(userId: Long, name: String,age: Int) {
        val contentValues=ContentValues().apply {
            put("id",userId)
            put("name",name)
            put("age",age)
        }
        context.contentResolver.insert(CONTENT_USER_URI,contentValues)
    }


//    suspend fun getAllCourses():List<Course>{
//        return NetworkRetrofit.userAndCoursesAPI.getCoursesList()
//    }
//
//    suspend fun addUser(id:Long,name:String,age:Int){
//        NetworkRetrofit.userAndCoursesAPI.addUser(id,name,age)    }

    companion object{
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

}

