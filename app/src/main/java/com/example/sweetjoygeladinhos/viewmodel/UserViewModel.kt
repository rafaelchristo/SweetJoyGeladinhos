package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sweetjoygeladinhos.model.UserType

class UserViewModel : ViewModel() {
    var userType: UserType = UserType.CLIENT
}