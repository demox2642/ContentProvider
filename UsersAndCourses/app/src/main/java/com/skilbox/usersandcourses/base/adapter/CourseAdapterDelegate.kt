package com.skilbox.usersandcourses.base.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.skilbox.usersandcourses.R
import com.skilbox.usersandcourses.base.ClickItem
import com.skilbox.usersandcourses.base.Course
import com.skilbox.usersandcourses.base.inflate

class CourseAdapterDelegate(
    val onItemClick: (item: ClickItem) -> Unit
) : AbsListItemAdapterDelegate<Course, Course, CourseAdapterDelegate.CourseHolder>() {

    class CourseHolder(
        view: View,
        onItemClick: (item: ClickItem) -> Unit
    ) : BaseHolder(view, onItemClick) {

        fun bind(courseId: Long, title: String) {
            bindMainInfo(id = courseId, title = title)
        }

        override val containerView: View
            get() = itemView
    }

    override fun isForViewType(item: Course, items: MutableList<Course>, position: Int): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): CourseHolder {
        return CourseHolder(parent.inflate(R.layout.course_list_item), onItemClick)
    }

    override fun onBindViewHolder(
        item: Course,
        holder: CourseHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item.id, item.title)
    }
}
