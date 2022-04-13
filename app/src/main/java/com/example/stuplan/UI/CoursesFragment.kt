package com.example.stuplan.UI

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stuplan.R
import com.example.stuplan.adapters.CourseAdapter
import com.example.stuplan.adapters.CourseRVClickListener
import com.example.stuplan.databinding.FragmentCoursesBinding
import com.example.stuplan.model.CourseModel
import com.example.stuplan.sqlite.DatabaseHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.ArrayList


class CoursesFragment : Fragment() {

    private lateinit var binding: FragmentCoursesBinding
    var courseList = ArrayList<CourseModel>()
    private lateinit var dbHelper : DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
        courseList = dbHelper.getAllCourse()

        if (courseList.isNullOrEmpty()){
            binding.tvNoCourse.visibility=View.VISIBLE
        }else{
            binding.tvNoCourse.visibility=View.GONE
        }

        binding.toolbarCourses.inflateMenu(R.menu.course_frag_menu)

        binding.toolbarCourses.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_course_menu_item -> {
                    addCourse()
                    true
                }
                /*R.id.search_course_menu_item -> {
                    true
                }*/
                else -> false
            }
        }

        binding.rvCourse.also {
            it.layoutManager= LinearLayoutManager(requireContext()
                , LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter =
                CourseAdapter(courseList,object : CourseRVClickListener{
                    override fun onEditClick(course: CourseModel) {
                        updateClicked(course)

                    }

                    override fun onDeleteClick(course: CourseModel) {
                        deleteClicked(course)
                    }

                })
        }


    }

    private fun deleteClicked(course: CourseModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Are you sure you want to delete this note?")
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    dbHelper.deleteCourse(course)
                    courseList.remove(course)
                    binding.rvCourse.adapter?.notifyDataSetChanged()
                }

            })
            .setNegativeButton("No", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }

            }).show()
    }

    private fun updateClicked(course: CourseModel) {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_course,null)
        val updateCourseET= dialogView.findViewById<EditText>(R.id.et_update_course)
        updateCourseET.setText(course.courseName)

        val  updateButton = dialogView.findViewById<Button>(R.id.btn_update_update_course)
        val  cancelButton = dialogView.findViewById<Button>(R.id.btn_cancel_update_course)

        builder.setView(dialogView)

        updateButton.setOnClickListener {
            courseList.remove(course)
            val updatedCourseName = updateCourseET.text.toString()


            if (updatedCourseName.isNullOrEmpty()){
                Toast.makeText(requireContext(),
                    "Course name cannot be empty",Toast.LENGTH_SHORT).show()
            } else{
                val updateCourse = CourseModel(course.id,updatedCourseName )
                val status=dbHelper.updateCourse(updateCourse)

                if (status >-1){
                    //success
                    Toast.makeText(requireContext(), "Course updated successfully."
                        , Toast.LENGTH_SHORT).show()
                    courseList.add(updateCourse)
                    binding.rvCourse.adapter?.notifyDataSetChanged()
                    builder.dismiss()

                } else{
                    //failure
                    Toast.makeText(requireContext(), "Course is not updated.", Toast.LENGTH_SHORT).show()
                    builder.dismiss()

                }
            }

        }

        cancelButton.setOnClickListener {
            builder.dismiss()
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()

    }


    private fun addCourse() {
        val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialog)
            .create()
        val dialogView = layoutInflater.inflate(R.layout.add_course_ui,null)
        builder.setView(dialogView)


        val addCourseEditText = dialogView.findViewById<EditText>(R.id.et_add_course)
        val btnAddCourse = dialogView.findViewById<Button>(R.id.btn_add_add_course)
        val btnCancelCourse = dialogView.findViewById<Button>(R.id.btn_cancel_add_course)


        btnAddCourse.setOnClickListener {
            val courseNameFromET = addCourseEditText.text.toString()
            if (courseNameFromET.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Course name is empty.", Toast.LENGTH_SHORT).show()
            }else{
                val course = CourseModel(null, courseNameFromET)
                val status = dbHelper.insertCourse(course)

                if (status >-1){
                    //success
                    Toast.makeText(requireContext(), "Course added successfully."
                        , Toast.LENGTH_SHORT).show()

                    courseList.add(course)
                    binding.rvCourse.adapter?.notifyDataSetChanged()
                    binding.tvNoCourse.visibility=View.GONE
                    builder.dismiss()

                    addCourseEditText.text = null


                } else{
                    //failure
                    Toast.makeText(requireContext(), "There was some problems adding a note", Toast.LENGTH_SHORT).show()
                    builder.dismiss()

                }

            }
            builder.dismiss()
        }

        btnCancelCourse.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}