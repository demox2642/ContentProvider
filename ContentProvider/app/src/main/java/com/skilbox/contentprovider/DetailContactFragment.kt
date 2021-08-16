package com.skilbox.contentprovider

import android.os.Bundle
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.skilbox.contentprovider.data.ContactViewModel
import kotlinx.android.synthetic.main.fragment_detail_contact.*

class DetailContactFragment : Fragment(R.layout.fragment_detail_contact) {

    private val viewModel: ContactViewModel by navGraphViewModels<ContactViewModel>(R.id.Contact) {
        defaultViewModelProviderFactory
    }
    private val args: DetailContactFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initContact()
        val contact = viewModel.contactLiveData.value!![args.contactId]
        delete_contact_button.setOnClickListener {
            viewModel.deleteContact(contact_id = contact.id)
            findNavController().popBackStack()
        }
    }

    private fun initContact() {
        val contact = viewModel.contactLiveData.value!![args.contactId]
        val filtered = "[]"
        ContactName.text = contact.name
        phoneNumber.text = contact.phones.toString().replace(",", "\n").filterNot { filtered.indexOf(it)> -1 }
        email.text = contact.email

        upload_profile_picture_image_view.setImageURI(contact.photo?.toUri())
    }
}
