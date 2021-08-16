package com.skilbox.contentprovider.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.skilbox.contentprovider.data.Contact

class ContactAdapter(onItemClick: (position: Int) -> Unit) : AsyncListDifferDelegationAdapter<Contact>(ContactDiffUtilCallBack()) {
    init {
        delegatesManager.addDelegate(ContactDelegateAdapter(onItemClick))
    }

    class ContactDiffUtilCallBack : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}
