package com.mis.route.chatapp.ui.auth.fragments.register

import com.mis.route.chatapp.ui.database.User

sealed class RegisterViewEvents{

    data class NavigateToHome(val user:User):RegisterViewEvents()
}
