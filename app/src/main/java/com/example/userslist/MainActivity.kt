package com.example.userslist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.userslist.models.ActivityMainModel
import com.example.userslist.AddEditActivity

private const val KEY_INDEX = "com.example.userlist.index"

class MainActivity : AppCompatActivity() {

    private lateinit var btnNext: ImageButton
    private lateinit var btnPrev: ImageButton
    private lateinit var btnAdd: ImageButton
    private lateinit var btnDelete: ImageButton
    private lateinit var btnEdit: ImageButton

    private lateinit var tvSecondName: TextView

    private val viewModel: ActivityMainModel by lazy {
        var provider = ViewModelProvider(this)
        provider[ActivityMainModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.curId = savedInstanceState?.getInt(KEY_INDEX)?:0

        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrev)
        btnAdd = findViewById(R.id.btnAdd)
        btnDelete = findViewById(R.id.btnDelete)
        btnEdit = findViewById(R.id.btnEdit)

        tvSecondName = findViewById(R.id.tvSecondName)

        updateInfo()

        btnNext.setOnClickListener{
            viewModel.showNext()
            updateInfo()
        }
        btnPrev.setOnClickListener{
            viewModel.showPrev()
            updateInfo()
        }

        val resultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data: Intent? = result.data
                if (data?.getBooleanExtra(BUTTON_ADD, false) == true){
                    val std = data.getStringArrayExtra("Student")
                    viewModel.addStudent(std!![0],std[1],std[2],std[3],std[4],std[5])
                    updateInfo()
                }
                else if (data?.getBooleanExtra(BUTTON_EDIT, false) == true){
                    val std = data.getStringArrayExtra("Student")
                    val stdId = data.getIntExtra("id",Int.MIN_VALUE)
                    if (stdId!=Int.MIN_VALUE)
                        viewModel.editStudent(stdId,std!![0],std[1],std[2],std[3],std[4],std[5])
                    updateInfo()
                }
            }
        }
        btnAdd.setOnClickListener{
            val intent = AddEditActivity.newIntent(
                this@MainActivity,
                button="Add",
                student = null,
                id = null,
                bankEmpty = viewModel.studentBankIsEmpty()
            )
            resultLauncher.launch(intent)
        }
        btnEdit.setOnClickListener{
            val intent = AddEditActivity.newIntent(
                this@MainActivity,
                viewModel.curId,
                "Edit",
                arrayOf(
                    viewModel.curStudent[0],
                    viewModel.curStudent[1],
                    viewModel.curStudent[2],
                    viewModel.curStudent[3],
                    viewModel.curStudent[5],
                    viewModel.curStudent[4]
                ),
                viewModel.studentBankIsEmpty()
            )
            resultLauncher.launch(intent)
        }
        btnDelete.setOnClickListener{
            if(viewModel.delStudent(viewModel.curId))
                updateInfo()
            else{
                btnAdd.callOnClick()
            }
        }
    }

    private fun updateInfo(){
        tvSecondName.text = viewModel.toString()
    }
}