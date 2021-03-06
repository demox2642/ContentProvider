package com.skilbox.contentprovider

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        contact.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_contactFragment)
        }

        shared_file.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_fileShareFragment)
        }
    }
}
