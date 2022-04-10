package com.example.stuplan.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stuplan.R
import com.example.stuplan.model.CourseModel

class CourseAdapter(
    private val courseList:ArrayList<CourseModel>
    , viewClickListener: CourseRVClickListener) : RecyclerView.Adapter<CourseViewHolder>() {

    private val listener: CourseRVClickListener = viewClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.courses_rv_item,parent,false))
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course =courseList.get(position)
        holder.courseNameTv.text = course.courseName

        holder.deleteIv.setOnClickListener {
            listener.onDeleteClick(course)
        }

        holder.editIv.setOnClickListener {
            listener.onEditClick(course)
        }
    }

    override fun getItemCount(): Int = courseList.size
}

interface CourseRVClickListener {
    fun onEditClick(course: CourseModel)
    fun onDeleteClick(course : CourseModel)
}

class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val courseNameTv = itemView.findViewById<TextView>(R.id.course_name_course_rv_item)
    val editIv = itemView.findViewById<ImageView>(R.id.iv_edit_course_rv_item)
    val deleteIv = itemView.findViewById<ImageView>(R.id.iv_delete_course_rv_item)



}
