package com.example.stuplan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stuplan.R
import com.example.stuplan.model.TaskModel

class TaskAdapter(
    private val taskList : ArrayList<TaskModel>
    , viewClickListener : TaskRvClickListener) : RecyclerView.Adapter<TaskViewHolder>() {

    private val listener: TaskRvClickListener = viewClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tasks_rv_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList.get(position)
        holder.assignmentNameTv.text = task.taskName
        holder.courseNameTvTaskRV.text = task.taskName
        holder.dueDateTv.text = task.duedate

        holder.completedCheckbox.isChecked = task.completed==1

        holder.completedCheckbox.setOnClickListener {
            val checked : Boolean = holder.completedCheckbox.isChecked

            if (checked){
                task.completed=1
                listener.onCompletedClick(task)
            } else{
                task.completed=0
                listener.onCompletedClick(task)
            }

        }




    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}

interface TaskRvClickListener{
    fun onCompletedClick(task: TaskModel)

}

class TaskViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val assignmentNameTv = itemView.findViewById<TextView>(R.id.tv_assignment_name_rv_task_item)
    val courseNameTvTaskRV = itemView.findViewById<TextView>(R.id.tv_course_name_rv_task_item)
    val dueDateTv = itemView.findViewById<TextView>(R.id.tv_task_deadline_rv_task_item)
    val completedCheckbox = itemView.findViewById<CheckBox>(R.id.task_completed_checkbox)

}
