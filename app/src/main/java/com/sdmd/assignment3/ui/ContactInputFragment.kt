package com.sdmd.assignment3.ui

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import com.sdmd.assignment3.R
import com.sdmd.assignment3.viewmodel.InputActivityViewModel

const val ContactInputFragmentTAG: String = "ContactInputFragment"

class ContactInputFragment : Fragment(), ConfirmationDialogFragment.ConfirmationDialogListener {
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

    // Initialise fragment view
    private fun init(view: View) {
        returnButton = view.findViewById(R.id.inputReturnButton)
        saveButton = view.findViewById(R.id.inputSaveButton)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        addressEditText = view.findViewById(R.id.addressEditText)
        suburbEditText = view.findViewById(R.id.suburbEditText)
        update()
        // Anonymous function that moves to the previous page of input activity
        val previousPage: (() -> (Unit)) = {
            // Save current details before going back to previous input page
            inputActivityViewModel.setContactDetails(phoneEditText.text.toString(), addressEditText.text.toString(), suburbEditText.text.toString())
            Log.i(ContactInputFragmentTAG, "Set contact details ${inputActivityViewModel.currentProfile.value}")

            Log.i(ContactInputFragmentTAG, "Change to PersonalInputFragment")
            inputActivityViewModel.setPreviousPage()
        }

        // Anonymous function that returns the profile details to Main Activity
        val saveProfile: (() -> (Unit)) = {
            if(validateInput()) {
                Log.i(ContactInputFragmentTAG, "Save Dialog pops up")
                // Call the confirmation dialog to ensure this is not an accident
                val dialog = ConfirmationDialogFragment("Save confirmation", "Are you sure you want to save this profile?")
                dialog.show(childFragmentManager, "SaveDialogFragment")
            } else {
                Toast.makeText(requireActivity(), "There's error in your submitted details", Toast.LENGTH_SHORT).show()
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
            Log.d(ContactInputFragmentTAG, "Phone Input: ${phoneEditText.text.toString()} - Error: The field is empty")
        } else if (PhoneNumberUtils.isGlobalPhoneNumber(phoneEditText.text.toString().trim())) {
            phoneEditText.error = getString(R.string.phone_invalid)
            isCorrected = false
            Log.d(ContactInputFragmentTAG, "Phone Input: ${phoneEditText.text.toString()} - Error: Phone is in invalid format")
        } else {
            Log.d(ContactInputFragmentTAG, "Phone Input: ${phoneEditText.text.toString()} - No Error")
        }

        return isCorrected
    }

    // Actions for dialog if users click Yes
    override fun onConfirmationDialogPositiveClick(dialog: DialogFragment) {
        Log.i(ContactInputFragmentTAG, "User clicks Save - Save Successfully")
        inputActivityViewModel.setContactDetails(phoneEditText.text.toString(), addressEditText.text.toString(), suburbEditText.text.toString())
        Log.i(ContactInputFragmentTAG, "Set contact details ${inputActivityViewModel.currentProfile.value}")

        Toast.makeText(requireActivity(), "Saving details successfully", Toast.LENGTH_SHORT).show()
        // Returns the profile result
        requireActivity().intent.putExtra("Profile", inputActivityViewModel.currentProfile.value)
        requireActivity().setResult(RESULT_OK, requireActivity().intent)
        requireActivity().finish()
    }

    // Actions for dialog if users click No
    override fun onConfirmationDialogNegativeClick(dialog: DialogFragment) {
        Log.i(ContactInputFragmentTAG, "User clicks No - Action Cancelled")
        // Handle the Cancel action from cancel dialog
        Toast.makeText(requireActivity(), "Action Cancelled", Toast.LENGTH_SHORT).show()
    }
}