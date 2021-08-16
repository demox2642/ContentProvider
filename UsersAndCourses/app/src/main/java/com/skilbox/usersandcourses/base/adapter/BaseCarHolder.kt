package com.skilbox.usersandcourses.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.skilbox.usersandcourses.base.ClickItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.course_list_item.view.*

abstract class BaseHolder(
    view: View,

    private val onItemClick: (item: ClickItem) -> Unit,
    private var courseID: Long? = null,
    private var titleText: String? = null
) : RecyclerView.ViewHolder(view), LayoutContainer {

    init {

        view.setOnClickListener {

            onItemClick(
                ClickItem(
                    false,
                    courseID!!
                )

            )
        }

        view.setOnLongClickListener {
            onItemClick(ClickItem(true, courseID!!))
            true
        }
    }

    protected fun bindMainInfo(
        id: Long,
        title: String,

    ) {

        courseID = id

        itemView.course_id_text.text = id.toString()
        itemView.course_text_edit.text = title
    }
}
