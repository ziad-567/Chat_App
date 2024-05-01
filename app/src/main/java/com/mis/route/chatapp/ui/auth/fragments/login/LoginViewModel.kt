package com.mis.route.chatapp.ui.auth.fragments.login

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mis.route.chatapp.ui.ViewMessage
import com.mis.route.chatapp.ui.base.BaseViewModel
import com.mis.route.chatapp.ui.database.MyDatabase
import com.mis.route.chatapp.ui.database.User

class LoginViewModel : BaseViewModel() {

    val emailLiveData = MutableLiveData<String>()
    val emailError = MutableLiveData<String?>()
    val passwordLiveData = MutableLiveData<String>()
    val passowrdError = MutableLiveData<String?>()
    val isLoading = MutableLiveData(false)
    val authService = Firebase.auth
    val events = MutableLiveData<LoginViewEvent>()

    fun login(){
        if (isLoading.value==true)return
        if (!validateInput())return
        isLoading.value = true
        authService.signInWithEmailAndPassword(
            emailLiveData.value!!
            ,passwordLiveData.value!!
        ).addOnCompleteListener{ task->
            if (task.isSuccessful){
                val user = task.result.user
               getUserFromDataBase(user!!.uid)
            }else{
                isLoading.value =false
                viewMessage.value = ViewMessage(
                    message = task.exception?.localizedMessage?:"something went wrong"
                )
            }
        }

    }

    private fun getUserFromDataBase(uid: String) {
        MyDatabase.getUserFromDB(uid){task->
            val user = task.result.toObject(User::class.java)
            if (task .isSuccessful && user!=null){
                events.postValue(LoginViewEvent.NavigateToHome(user))
            } else{
                viewMessage.postValue(
                    ViewMessage(
                        message =task.exception?.localizedMessage?:"",
                        posActionName = "ok",
                ),
                )
            }
        }

    }

    fun onGotoRegisterClick(){
        events.postValue(LoginViewEvent.NavigateToRegister)
    }


    fun validateInput():Boolean{
        var isValid =true
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

        return isValid



    }

}