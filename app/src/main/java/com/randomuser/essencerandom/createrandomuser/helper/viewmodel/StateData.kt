package com.randomuser.essencerandom.createrandomuser.helper.viewmodel

/**
 * LiveData wrapper to easily handle network issues and display the corresponding state through the
 * Activity.
 */
class StateData<T>(val status: DataStatus,
                   val data: T?,
                   val errorMessage: String?) {
    companion object {
        fun <T> success(data: T?): StateData<T> {
            return StateData(DataStatus.SUCCESS, data, null)
        }

        fun <T> error(errorMessage: String, data: T?): StateData<T> {
            return StateData(DataStatus.ERROR, data, errorMessage)
        }
    }
}