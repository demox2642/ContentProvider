package com.skilbox.contentprovider

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.skilbox.contentprovider.data.ContactViewModel
import kotlinx.android.synthetic.main.fragment_add_contact.*
import kotlinx.android.synthetic.main.fragment_detail_contact.*
import java.util.regex.Pattern

class AddContact : Fragment(R.layout.fragment_add_contact) {

    private val viewModel: ContactViewModel by navGraphViewModels<ContactViewModel>(R.id.Contact) {
        defaultViewModelProviderFactory
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        save_button.setOnClickListener {
            viewModel.saveContact(
                name_edit_text.text.toString(), phone_number_edit_text.text.toString(),
                mailValidate(email_edit_text.text.toString())
            )
            findNavController().popBackStack()
        }

        cancel_button.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    private fun mailValidate(mail: String?): String? {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(mail)
        return if (matcher.matches()) { mail } else { null }
    }
}
