package com.sdmd.assignment3.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.RESULT_CANCELED
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.sdmd.assignment3.R
import com.sdmd.assignment3.viewmodel.InputActivityViewModel

const val ContactInputFragmentTAG: String = "ContactInputFragment"

class ContactInputFragment : Fragment() {
    private lateinit var returnButton: Button
    private lateinit var saveButton: Button
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var addressEditText: TextInputEditText
    private lateinit var suburbEditText: TextInputEditText
    private val inputActivityViewModel: InputActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact_input, container, false)
        Log.i(ContactInputFragmentTAG, "ContactInputFragment created")
        init(view)
        return view
    }

    // Initialise activity view
    private fun init(view: View) {
        returnButton = view.findViewById(R.id.inputReturnButton)
        saveButton = view.findViewById(R.id.inputSaveButton)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        addressEditText = view.findViewById(R.id.addressEditText)
        suburbEditText = view.findViewById(R.id.suburbEditText)
        update()
        // Anonymous function that moves to the previous page of input activity
        val previousPage: (() -> (Unit)) = {
            Log.i(PersonalInputFragmentTAG, "Change to PersonalInputFragment")
            inputActivityViewModel.setPreviousPage()
        }

        // Anonymous function that returns the profile details to Main Activity
        val saveProfile: (() -> (Unit)) = {
            if(validateInput()) {
                Log.i(PersonalInputFragmentTAG, "User clicks Save - Save Successfully")
                inputActivityViewModel.setContactDetails(phoneEditText.text.toString(), addressEditText.text.toString(), suburbEditText.text.toString())
                Log.i(PersonalInputFragmentTAG, "Set contact details ${inputActivityViewModel.currentProfile.value}")

                requireActivity().intent.putExtra("Profile", inputActivityViewModel.currentProfile.value)
                requireActivity().setResult(RESULT_OK, requireActivity().intent)
                requireActivity().finish()
            }
        }

        // Handles both normal click and long click events
        returnButton.setOnClickListener { previousPage.invoke() }
        returnButton.setOnLongClickListener { previousPage.invoke(); true }

        saveButton.setOnClickListener { saveProfile.invoke() }
        saveButton.setOnLongClickListener { saveProfile.invoke(); true }
    }

    // Update edit text fields if the profile has been entered before
    private fun update() {
        inputActivityViewModel.currentProfile.value?.let {
            phoneEditText.setText(it.phone)
            addressEditText.setText(it.address)
            suburbEditText.setText(it.suburb)
        }
    }

    // Validate user input before saving profile
    private fun validateInput() : Boolean {
        var isCorrected = true

        // Check if the phone field is filled
        if(phoneEditText.text.toString().trim().isEmpty()) {
            phoneEditText.error = getString(R.string.phone_empty)
            isCorrected = false
            Log.d(PersonalInputFragmentTAG, "Phone Input: ${phoneEditText.text.toString()} - Error: The field is empty")
        } else {
            phoneEditText.error = null
            Log.d(PersonalInputFragmentTAG, "Phone Input: ${phoneEditText.text.toString()} - No Error")
        }

        return isCorrected
    }
}