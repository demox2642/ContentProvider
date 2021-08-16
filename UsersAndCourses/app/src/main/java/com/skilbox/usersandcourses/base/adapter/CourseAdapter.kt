package com.skilbox.usersandcourses.base.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.skilbox.usersandcourses.base.ClickItem
import com.skilbox.usersandcourses.base.Course

class CourseAdapter(
    private val onItemClick: (item: ClickItem) -> Unit
) : AsyncListDifferDelegationAdapter<Course>(CourseDiffUtilCallBack()) {

    init {
        delegatesManager
            .addDelegate(CourseAdapterDelegate(onItemClick))
    }

    class CourseDiffUtilCallBack : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}
