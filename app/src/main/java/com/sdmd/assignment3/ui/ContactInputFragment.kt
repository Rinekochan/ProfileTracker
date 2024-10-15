package com.sdmd.assignment3.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sdmd.assignment3.R
import com.sdmd.assignment3.viewmodel.InputActivityViewModel

class ContactInputFragment : Fragment() {
    private val inputActivityViewModel: InputActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact_input, container, false)

        return view
    }
}