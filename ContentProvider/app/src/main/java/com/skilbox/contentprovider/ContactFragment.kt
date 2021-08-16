package com.skilbox.contentprovider

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.solver.widgets.Helper
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.skilbox.contentprovider.adapter.ContactAdapter
import com.skilbox.contentprovider.data.ContactViewModel
import kotlinx.android.synthetic.main.fragment_contact_list.*
import permissions.dispatcher.ktx.constructPermissionsRequest

class ContactFragment : Fragment(R.layout.fragment_contact_list) {

    private val viewModel: ContactViewModel by navGraphViewModels<ContactViewModel>(R.id.Contact) {
        defaultViewModelProviderFactory
    }
    private var contactAdapter: ContactAdapter by autoCleared()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initToolbar()
        initList()

        addContactbutton.setOnClickListener {
            findNavController().navigate(R.id.action_contactFragment_to_addContact)
        }
        bindViewModel()

        Handler(Looper.getMainLooper()).post {

            constructPermissionsRequest(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.CALL_PHONE,
                onShowRationale = { permissionRequest ->
                    Log.e(Helper::class.java.simpleName, "PD: onShowRationale")
                    onContactPermissionShowRationale(permissionRequest)
                },
                onPermissionDenied = ::onContactPermissionDenied,
                onNeverAskAgain = ::onContactPermissionNeverAskAgain,
                requiresPermission = { viewModel.loadList() }
            )

                .launch()
        }
    }

    private fun bindViewModel() {
        viewModel.contactLiveData.observe(viewLifecycleOwner) { contactAdapter?.items = it }
        viewModel.callLiveData.observe(viewLifecycleOwner, ::callToPhone)
    }

    private fun initList() {
        contactAdapter = ContactAdapter { ClickItem -> detailContact(ClickItem) }
        with(contact_list) {
            adapter = contactAdapter
            setHasFixedSize(true)
        }
    }

    private fun detailContact(contact_id: Int) {

        val args = ContactFragmentDirections.actionContactFragmentToDetailContactFragment(contact_id)
        Log.e("ToDetail", "$args")
        findNavController().navigate(args)
    }

    private fun initToolbar() {
        // toolbar.setTitle("Список контактов")
    }

    private fun callToPhone(phone: String) {
        Intent(Intent.ACTION_DIAL)
            .apply { data = Uri.parse("tel:$phone") }
            .also { startActivity(it) }
    }

    private fun onContactPermissionDenied() {
        toast("Доступ к чтению контактов запрещен")
    }

    private fun onContactPermissionShowRationale(request: permissions.dispatcher.PermissionRequest) {
        request.proceed()
    }

    private fun onContactPermissionNeverAskAgain() {
        toast("Разрешите доступ к чтению контактов")
    }

    private fun toast(massage: String) {
        Toast.makeText(requireContext(), massage, Toast.LENGTH_SHORT).show()
    }
}
