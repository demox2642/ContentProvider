package com.skilbox.contentprovider.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.skilbox.contentprovider.R
import com.skilbox.contentprovider.data.Contact
import com.skilbox.networking.data.network.adapter.inflate

class ContactDelegateAdapter(val onItemClick: (position: Int) -> Unit) :
    AbsListItemAdapterDelegate<Contact, Contact, ContactDelegateAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ContactViewHolder {
        return ContactViewHolder(parent.inflate(R.layout.contact_view_for_list), onItemClick)
    }

    class ContactViewHolder(view: View, onItemClick: (position: Int) -> Unit) : BaseHolder(view, onItemClick) {

        fun bind(contact: Contact) {
            bindMainInfo(
                id = contact.id.toString(),
                contact_name = contact.name,
                phone = contact.phones.toString(),
                photo = contact.photo?.toUri()
            )
        }

        override val containerView: View
            get() = itemView
    }

    override fun isForViewType(
        item: Contact,
        items: MutableList<Contact>,
        position: Int
    ): Boolean {
        return true
    }

    override fun onBindViewHolder(
        item: Contact,
        holder: ContactViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}
