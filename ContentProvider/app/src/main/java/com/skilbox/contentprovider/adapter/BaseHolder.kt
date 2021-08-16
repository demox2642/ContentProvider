package com.skilbox.contentprovider.adapter

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.contact_view_for_list.view.*

abstract class BaseHolder(
    view: View,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(view), LayoutContainer {

    init {
        view.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }

    protected fun bindMainInfo(
        id: String,
        contact_name: String,
        phone: String,
        photo: Uri?
    ) {

        itemView.user_pic.setImageURI(photo)
        itemView.contact_name.text = contact_name
        itemView.contact_phone.text = phone
    }
}
