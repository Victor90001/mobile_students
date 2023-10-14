package com.example.userslist.models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.userslist.data.Student
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Objects

class ActivityMainModel: ViewModel() {
    private fun getDate(date: String): Date {
        var resDate: Date
        try {
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            resDate = formatter.parse(date)
            Log.e("resDate",resDate.toString())
            return resDate
        } catch (e: Exception){
            Log.e("resDate",e.toString())
            return SimpleDateFormat("dd.MM.yyyy").parse("01.01.1900")
        }
    }
    private var studentBank = arrayListOf(
        Student(1, "Мошняков","Виктор","Русланович","4ИТ","Мужчина","13.06.2002"),
        Student(2, "Иванова","Мария","Ивановна","3ИТ","Женщина","13.05.2002"),
        Student(3, "Долматов","Жульон","Гошанович","4ММ","Мужчина","13.02.2002")
    )
    var curId = 1

    val curStudent: ArrayList<String>
        get()= arrayListOf(
            studentBank[curId].sName,
            studentBank[curId].fName,
            studentBank[curId].patronymic,
            studentBank[curId].groupName,
            studentBank[curId].sex,
            studentBank[curId].birthDate
        )

    fun showNext(){
        curId = (curId + 1) % studentBank.size
    }
    fun showPrev(){
        curId = (studentBank.size + curId - 1) % studentBank.size
    }
    fun editStudent(id: Int,s: String, f: String, p: String, g: String, sex: String, b: String){
        val st = studentBank[id]
        st.sName = s
        st.fName = f
        st.patronymic = p
        st.groupName = g
        st.sex = sex
        st.patronymic = p
    }
    fun addStudent(s: String, f: String, p: String, g: String, sex: String, b: String){
        studentBank.add(Student(studentBank.size,s,f,p,g,sex,b))
    }
    fun delStudent(id: Int):Boolean{
        studentBank.removeAt(id)
        if(studentBank.size == 0){
            return false
        }
        return true
    }
    fun studentBankIsEmpty():Boolean{
        return studentBank.isEmpty()
    }
    override fun toString():String{
        val stud = studentBank[curId]
        return "ФИО: ${stud.sName} ${stud.fName} ${stud.patronymic}\n" +
                "Группа: ${stud.groupName}\n" +
                "Пол: ${stud.sex}\n" +
                "Дата рождения: ${stud.birthDate}\n"
    }
}