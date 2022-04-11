package com.example.stuplan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stuplan.R
import com.example.stuplan.model.CourseModel
import com.example.stuplan.model.TaskModel

class CompletedTaskAdapter(
    private val completedTasks: ArrayList<TaskModel>,
    private val courseList: ArrayList<CourseModel>
)
    : RecyclerView.Adapter<CompletedTaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedTaskViewHolder {
        return CompletedTaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.completed_tasks_rv_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CompletedTaskViewHolder, position: Int) {
        val completedTask = completedTasks.get(position)
        holder.titleTV.text=completedTask.taskName

        val courseHashmap=HashMap<Long, String>()
        for (i in courseList){
            courseHashmap.put(i.id!!, i.courseName)
        }
        holder.courseTV.text = courseHashmap.getValue(completedTask.courseId)

        val dateArray = completedTask.duedate.split(",")
        holder.dateTV.text=dateArray[1]+", "+ dateArray[3]
    }

    override fun getItemCount(): Int {
        return completedTasks.size
    }
}

class CompletedTaskViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val titleTV = itemView.findViewById<TextView>(R.id.tv_hw_name_rv_completed_item)
    val courseTV = itemView.findViewById<TextView>(R.id.tv_tv_sub_name_rv_completed_item)
    val dateTV = itemView.findViewById<TextView>(R.id.tv_date_rv_completed_item)
}
