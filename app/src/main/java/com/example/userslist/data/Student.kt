package com.example.userslist.data

import androidx.annotation.StringRes
import java.util.Date

data class Student(
    val id: Int,
    var sName: String,
    var fName: String,
    var patronymic: String,
    var groupName: String,
    var sex: String,
    var birthDate: String
    )
