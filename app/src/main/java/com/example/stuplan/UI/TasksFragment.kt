package com.example.stuplan.UI

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stuplan.R
import com.example.stuplan.adapters.TaskAdapter
import com.example.stuplan.adapters.TaskRvClickListener
import com.example.stuplan.databinding.FragmentTasksBinding
import com.example.stuplan.model.CourseModel
import com.example.stuplan.model.TaskModel
import com.example.stuplan.sqlite.DatabaseHelper
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TasksFragment : Fragment() {

    private lateinit var binding : FragmentTasksBinding
    private lateinit var dbHelper : DatabaseHelper
    var taskList = ArrayList<TaskModel>()
    var courseList = java.util.ArrayList<CourseModel>()
    private lateinit var taskToAddInDatabase : TaskModel
    private lateinit var selectedObject : CourseModel
    var dateAndTimeString = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        courseList = dbHelper.getAllCourse()
        taskList = dbHelper.getAllTask()

        if (taskList.isNullOrEmpty()){
            binding.tvNoTask.visibility=View.VISIBLE
        }

        binding.toolbarCourses.inflateMenu(R.menu.course_frag_menu)

        binding.toolbarCourses.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_course_menu_item -> {
                    addTask()
                    true
                }
                /*R.id.search_course_menu_item -> {
                    true
                }*/
                else -> false
            }
        }

        binding.rvTask.also {
            it.layoutManager= LinearLayoutManager(requireContext()
                , LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter= TaskAdapter(taskList,courseList, object : TaskRvClickListener{
                override fun onCompletedClick(task: TaskModel) {
                    println("task is : $task")
                    val count =dbHelper.setTaskCompleted(task)
                    println("count : $count")
                }

            })
        }


    }

    private fun addTask() {
        val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialog)
            .create()
        val dialogView = layoutInflater.inflate(R.layout.add_task_ui,null)
        builder.setView(dialogView)
        builder.setCanceledOnTouchOutside(false)

        val taskNotesEt = dialogView.findViewById<EditText>(R.id.et_notes_of_task_add_task)
        val taskNameEt = dialogView.findViewById<EditText>(R.id.et_name_of_task_add_task)
        val dateAndTimeTv = dialogView.findViewById<TextView>(R.id.et_due_date_of_task_add_task)
        val addButon = dialogView.findViewById<Button>(R.id.btn_add_add_task)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_cancel_add_task)
        val courseSpinner = dialogView.findViewById<Spinner>(R.id.spinner_course_add_task)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        dateAndTimeTv.setOnClickListener {
            val dialog = DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(datePicker: DatePicker?, y: Int, m: Int, d: Int) {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.YEAR, y)
                    cal.set(Calendar.MONTH,m)
                    cal.set(Calendar.DAY_OF_MONTH,d)

                    dateAndTimeString = DateFormat.getDateInstance(DateFormat.FULL).format(cal.time)

                    val timePicker = TimePickerDialog(
                        // pass the Context
                        requireContext(),
                        object : TimePickerDialog.OnTimeSetListener{
                            override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {

                                val formattedTime: String = when {
                                    hourOfDay == 0 -> {
                                        if (minute < 10) {
                                            "${hourOfDay + 12}:0${minute} am"
                                        } else {
                                            "${hourOfDay + 12}:${minute} am"
                                        }
                                    }
                                    hourOfDay > 12 -> {
                                        if (minute < 10) {
                                            "${hourOfDay - 12}:0${minute} pm"
                                        } else {
                                            "${hourOfDay - 12}:${minute} pm"
                                        }
                                    }
                                    hourOfDay == 12 -> {
                                        if (minute < 10) {
                                            "${hourOfDay}:0${minute} pm"
                                        } else {
                                            "${hourOfDay}:${minute} pm"
                                        }
                                    }
                                    else -> {
                                        if (minute < 10) {
                                            "${hourOfDay}:${minute} am"
                                        } else {
                                            "${hourOfDay}:${minute} am"
                                        }
                                    }
                                }
                                dateAndTimeString = dateAndTimeString +", " +formattedTime
                                dateAndTimeTv.text = dateAndTimeString
                            } },
                        hour,
                        minute,
                        false
                    )

                    // then after building the timepicker
                    // dialog show the dialog to user
                    timePicker.show()
                }
            }, year, month, day)
            dialog.show()
        }

        val adapter = ArrayAdapter<CourseModel>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            courseList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        courseSpinner.setAdapter(adapter)

        courseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // You can define your actions as you want

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                selectedObject = courseSpinner.selectedItem as CourseModel

            }
        }
        addButon.setOnClickListener {
            val taskName = taskNameEt.text.toString()
            val taskCourseId = selectedObject.id
            val taskNote = taskNotesEt.text.toString()

            if (taskName.isEmpty()){
                Toast.makeText(requireContext(), "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (dateAndTimeString.isEmpty()){
                Toast.makeText(requireContext(), "Select a valid due date", Toast.LENGTH_SHORT).show()
            } else{
                val task = taskCourseId?.let { it1 ->
                    TaskModel(null, taskName, taskNote= taskNote, duedate = dateAndTimeString, courseId = it1)
                }

                val status = task?.let { it1 -> dbHelper.insertTask(it1) }
                if (status!! >-1){
                    binding.tvNoTask.visibility=View.GONE
                    task.id=status
                    task?.let { it1 -> taskList.add(it1) }
                    binding.rvTask.adapter?.notifyDataSetChanged()
                }

            }

            builder.dismiss()
        }

        cancelButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()




    }

}