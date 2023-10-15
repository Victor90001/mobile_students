package com.example.userslist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModelProvider
import com.example.userslist.models.ActivityMainModel

const val BUTTON_ADD = "com.example.userlist.add_button"
const val BUTTON_EDIT = "com.example.userlist.edit_button"

class AddEditActivity : AppCompatActivity() {
    private lateinit var btnAccept: Button
    private lateinit var btnDecline: Button

    private lateinit var editSName: EditText
    private lateinit var editFName: EditText
    private lateinit var editPatronymic: EditText
    private lateinit var editGroup: EditText
    private lateinit var editSex: ToggleButton
    private lateinit var editBirth: EditText
    private lateinit var studentAdded: TextView
    private lateinit var menuETList: List<EditText>
    private var bankEmpty: Boolean? = null
    private var id:Int? =null

//    private val viewModel: ActivityMainModel by lazy{
//        var provider = ViewModelProvider(this)
//        provider[ActivityMainModel::class.java]
//    }

    override fun onBackPressed() {
        if (bankEmpty == true){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Нет студентов")
            builder.setMessage("Все студенты были удалены, добавте нового студента для работы с приложением")
            builder.setPositiveButton(android.R.string.ok){ _, _ ->
            }
            val alertBox = builder.create()
            alertBox.show()
        }else{
            super.onBackPressed()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        val button = intent?.getStringExtra("buttonType")
        bankEmpty = intent?.getBooleanExtra("bankIsEmpty",false)

        btnDecline = findViewById(R.id.btnDecline)
        btnAccept = findViewById(R.id.btnAccept)
        editSName = findViewById(R.id.editSName)
        editFName = findViewById(R.id.editFName)
        editPatronymic = findViewById(R.id.editPatronymic)
        editGroup = findViewById(R.id.editGroup)
        editSex = findViewById(R.id.toggleSex)
        editBirth = findViewById(R.id.editTextDate)
        studentAdded = findViewById(R.id.tvStudentAdded)
        menuETList = listOf(
            editSName,
            editFName,
            editPatronymic,
            editGroup,
            editBirth
        )
        if (bankEmpty == true){
            btnDecline.visibility = View.GONE
        }

        if (button == "Edit"){
            id = intent?.getIntExtra("id", Int.MIN_VALUE)
            val student = intent?.getStringArrayExtra("CurStudent")
            for(i in 0..4){
                menuETList[i].setText(student!![i])
            }
            if (student!![5] == "Женщина"){
                editSex.toggle()
            }
        }

        val fcls = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                studentAdded.visibility = View.INVISIBLE
            }
        }
        val cls = View.OnClickListener {
            studentAdded.visibility = View.INVISIBLE
        }
        editSex.setOnClickListener(cls)
        for(elem in menuETList){
            elem.onFocusChangeListener = fcls
        }
        btnAccept.setOnClickListener{
            returnAccept(id, button)
        }
//        btnDecline.setOnClickListener{
//            returnDecline()
//        }

    }
    private fun checkErrors():Boolean{
        var error = false
        if(editSName.text.isBlank()){
            editSName.error = "Введите фамилию"
            error = true
        }
        if(editFName.text.isBlank()) {
            editFName.error = "Введите имя"
            error = true
        }
        if(editGroup.text.isBlank()){
            editGroup.error = "Введите группу"
            error = true
        }
        if(editBirth.text.isBlank()){
            editBirth.error = "Введите дату рождения"
            error = true
        }
        return error
    }

    private fun returnAccept(id: Int?,btnType: String?){
        if (checkErrors())
            return
        val data=Intent()
        if (btnType == "Add"){
            data.apply {
                putExtra(BUTTON_ADD, true)
                putExtra(BUTTON_EDIT, false)
                putExtra("Student", arrayOf(
                    editSName.text.toString(),
                    editFName.text.toString(),
                    editPatronymic.text.toString(),
                    editGroup.text.toString(),
                    editSex.text.toString(),
                    editBirth.text.toString()
                ))
            }
            for(i in 0..4){
//                menuETList[i].text.clear()
                menuETList[i].clearFocus()
            }
//            if (editSex.text.toString() == "Женщина")
//                editSex.toggle()
            studentAdded.visibility = View.VISIBLE
            bankEmpty = false
        } else {
            data.apply {
                putExtra(BUTTON_ADD, false)
                putExtra(BUTTON_EDIT, true)
                putExtra("id", id)
                putExtra("Student", arrayOf(
                    editSName.text.toString(),
                    editFName.text.toString(),
                    editPatronymic.text.toString(),
                    editGroup.text.toString(),
                    editSex.text.toString(),
                    editBirth.text.toString()
                ))
            }
            studentAdded.text = "Информация о студенте была изменена"
            studentAdded.visibility = View.VISIBLE
        }
        setResult(Activity.RESULT_OK, data)
    }

//    private fun returnDecline(){
//        val data = Intent().apply {
//            putExtra(BUTTON_ADD, false)
//            putExtra(BUTTON_EDIT, false)
//        }
//        setResult(Activity.RESULT_CANCELED, data)
//        finish()
//    }

    companion object{
        fun newIntent(packageContext: Context, id: Int?,button: String, student: Array<String>?,bankEmpty: Boolean?): Intent{
            return Intent(packageContext, AddEditActivity::class.java).apply {
                putExtra("buttonType", button)
                putExtra("id", id)
                putExtra("CurStudent", student)
                putExtra("bankIsEmpty", bankEmpty)
            }
        }
    }
}