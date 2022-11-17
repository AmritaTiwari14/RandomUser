package com.randomuser.essencerandom.createrandomuser.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.randomuser.essencerandom.createrandomuser.helper.viewmodel.DataStatus
import com.randomuser.essencerandom.createrandomuser.helper.viewmodel.StateData
import com.randomuser.essencerandom.createrandomuser.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val userRepository = HomeRepository()
    private var page: Int = 1
    var isNetworkAvailable = MutableLiveData<Boolean>()
    var stateDataUsers = MutableLiveData<StateData<List<User>>>()

    fun loadUsers() {
        GlobalScope.launch {
            val isConnected: Boolean = isNetworkAvailable.value ?: false
            val stateData: StateData<List<User>> = userRepository.retrieveUsers(isConnected, page)
            if (stateData.status == DataStatus.SUCCESS) {
                page += 1
            }
            stateDataUsers.postValue(stateData)
        }
    }
}