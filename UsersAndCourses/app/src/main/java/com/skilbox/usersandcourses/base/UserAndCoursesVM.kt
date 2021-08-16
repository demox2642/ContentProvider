package com.skilbox.usersandcourses.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserAndCoursesVM(application: Application): AndroidViewModel(application) {
    private val repository = Repository(application)
    private val userMutableLiveData = MutableLiveData <List<Users>>()
    private val serverErrorLiveData = MutableLiveData<String>()

    val userViewModel: LiveData<List<Users>>
        get() = userMutableLiveData

    val serverError: LiveData<String>
        get() = serverErrorLiveData

//    fun addUser(id:Long,name:String,age:Int){
//        viewModelScope.launch(Dispatchers.IO){
//            repository.addUser(id,name,age)
//        }
//
//
//    }

    fun getUsersList(){
        viewModelScope.launch {
            try {
                userMutableLiveData.postValue(repository.getUsersList())
            }catch (t:Throwable){
                Log.e("ContactVM","ERROR contact list: $t")
                userMutableLiveData.postValue(emptyList())
            }
        }
    }

     fun saveUser(name: String, age: Int) {
         viewModelScope.launch(Dispatchers.IO) {
             repository.saveUser(name, age)
         }
     }

}