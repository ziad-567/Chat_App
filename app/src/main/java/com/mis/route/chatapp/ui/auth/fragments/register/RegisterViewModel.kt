package com.mis.route.chatapp.ui.auth.fragments.register

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mis.route.chatapp.ui.ViewMessage
import com.mis.route.chatapp.ui.base.BaseViewModel
import com.mis.route.chatapp.ui.database.MyDatabase
import com.mis.route.chatapp.ui.database.User

class RegisterViewModel :BaseViewModel() {

    val usernameLiveData = MutableLiveData<String>()
    val userNameError = MutableLiveData<String?>()
    val emailLiveData = MutableLiveData<String>()
    val emailError = MutableLiveData<String?>()
    val passwordLiveData = MutableLiveData<String>()
    val passowrdError = MutableLiveData<String?>()
    val passwordConfirmError = MutableLiveData<String?>()
    val passwordConfirmLiveData = MutableLiveData<String>()
    val isRegistring = MutableLiveData(false)
    val authService = Firebase.auth
    val events = MutableLiveData<RegisterViewEvents>()


    fun register() {
        if (isRegistring.value == true) return
        if (!validateInput()) return
        isRegistring.value = true
        authService.createUserWithEmailAndPassword(
            emailLiveData.value!!, passwordLiveData.value!!
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                registerUserInDB(user!!.uid)
            } else {
                viewMessage.value = ViewMessage(
                    message = task.exception?.localizedMessage ?: "something went wrong"
                )
            }
        }

    }

    private fun registerUserInDB(uid: String) {
        val user = User(
            uid,
            usernameLiveData.value!!,
            emailLiveData.value!!,
        )

        MyDatabase.createUser(user) { task ->
            isRegistring.value = false

            if (task.isSuccessful) {
                events.postValue(
                    RegisterViewEvents.NavigateToHome(user),
                )
            } else {
                viewMessage.value = ViewMessage(
                    message = task.exception?.localizedMessage ?: "",
                    posActionName = "ok"
                )
            }
        }

    }

    fun validateInput():Boolean{
        var isValid =true

        if(usernameLiveData.value.isNullOrBlank()){
          userNameError.value ="please enter user Name"
            isValid =false
        }
        else{
            userNameError.value =null
        }

        if(emailLiveData.value.isNullOrBlank()){
          emailError.value ="please enter email"
            isValid =false
        }
        else{
            emailError.value =null
        }

        if(passwordLiveData.value.isNullOrBlank()){
          passowrdError.value ="please enter password"
            isValid =false
        }
        else if(passwordLiveData.value!!.length<6){
            passowrdError.value = "password must be least 6 chars "
            isValid =false
        }
        else{
            passowrdError.value =null
        }


        if(passwordConfirmLiveData.value.isNullOrBlank()){
          passwordConfirmError.value ="please enter password confirmation"
            isValid =false
        }
        else if(passwordLiveData.value!! != passwordConfirmLiveData.value!!){
            passwordConfirmError.value = "password dosent match "
            isValid =false
        }
        else{
            passwordConfirmError.value =null
        }
        return isValid



    }

}