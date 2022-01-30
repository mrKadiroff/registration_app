package com.example.registration.db

import com.example.registration.models.RegisterModel

interface DatabaseService {

    fun insertRegister(registerModel: RegisterModel)

    fun getAllCamera(): List<RegisterModel>
}