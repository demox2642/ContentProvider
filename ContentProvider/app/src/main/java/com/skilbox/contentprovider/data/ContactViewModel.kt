package com.skilbox.contentprovider.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val contactRepository = ContactRepository(application)

    private val callMutableLiveData = SingleLiveEvent<String>() // MutableLiveData<String>()  //
    private val contactsMutableLiveData = MutableLiveData<List<Contact>>()

//    fun callSingleLiveEvent(): SingleLiveEvent<String> {
//        return callMutableLiveData
//    }

    val callLiveData: LiveData<String>
        get() = callMutableLiveData

    val contactLiveData: LiveData<List<Contact>>
        get() = contactsMutableLiveData

    fun loadList() {
        viewModelScope.launch {
            try {
                contactsMutableLiveData.postValue(contactRepository.getAllContacts())
            } catch (t: Throwable) {
                Log.e("ContactVM", "ERROR contact list: $t")
                contactsMutableLiveData.postValue(emptyList())
            }
        }
    }

    fun saveContact(name: String, phone: String, email: String?) {
        viewModelScope.launch {
            try {
                contactRepository.saveContact(name, phone, email)
                contactsMutableLiveData.postValue(contactRepository.getAllContacts())
            } catch (t: Throwable) {
                Log.e("ContactVM", "ERROR saveContact : $t")
            }
        }
    }

    fun deleteContact(contact_id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.deleteContact(contact_id)
        }
    }
}
