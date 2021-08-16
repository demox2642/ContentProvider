package com.skilbox.usersandcourses.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserAndCursesVM(application: Application) : AndroidViewModel(application) {
    private val userAndCursesRepository = UserAndCursesRepository(application)

    private val coursesMutableLiveData = MutableLiveData<List<Course>>()
    private val courseMD = MutableLiveData<Course>()
    private val loadingMD = MutableLiveData<Boolean>()

    val loadingLD: LiveData<Boolean>
        get() = loadingMD

    val courseLiveDataForFragment: LiveData<List<Course>>
        get() = coursesMutableLiveData

    val courseLD: LiveData<Course>
        get() = courseMD

    fun loadCourseList() {
        viewModelScope.launch {
            try {
                loadingMD.postValue(true)
                val courseList = userAndCursesRepository.getAllCourses()
                Log.e("CourseVM", "Course list: $courseList")
                coursesMutableLiveData.postValue(courseList)
            } catch (t: Throwable) {
                Log.e("CourseVM", "ERROR contact list: $t")
                coursesMutableLiveData.postValue(emptyList())
            } finally {
                loadingMD.postValue(false)
            }
        }
    }

    fun addCourse(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingMD.postValue(true)
                userAndCursesRepository.saveCourse(title)
                Log.e("addCourse", "addCourse: $title")
            } catch (t: Throwable) {
                Log.e("addCourse", "ERROR addCourse: $t")
            } finally {
                loadingMD.postValue(false)
            }
        }
        loadCourseList()
    }

    fun updateCourse(idCourse: Long, title: String) {
        viewModelScope.launch {
            try {
                loadingMD.postValue(true)
                userAndCursesRepository.updeteCourse(idCourse, title)
            } catch (t: Throwable) {
                Log.e("updateCourse", "ERROR addCourse: $t")
            } finally {
                loadingMD.postValue(false)
            }
        }
    }

    fun deletecourse(idCourse: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingMD.postValue(true)
                userAndCursesRepository.deleteCourse(idCourse)
                Log.e("deletecourse", "idCourse= $idCourse")
                loadCourseList()
            } catch (t: Throwable) {
                Log.e("deletecourse", "ERROR addCourse: $t")
            } finally {
                loadingMD.postValue(false)
            }
        }
    }

    fun cleanCourseList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingMD.postValue(true)
                userAndCursesRepository.deleteAllCourses()
            } catch (t: Throwable) {
                Log.e("cleanCourseList", "ERROR addCourse: $t")
            } finally {
                loadingMD.postValue(false)
            }
        }
    }

    fun getCourse(idCourse: Long) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                loadingMD.postValue(true)
                val course = userAndCursesRepository.getCoursesById(idCourse)
                courseMD.postValue(course)
                Log.e("getCourse", "course= $course")
            } catch (t: Throwable) {
            } finally {
                loadingMD.postValue(false)
            }
        }
    }
}
