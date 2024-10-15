package com.sdmd.assignment3.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.RESULT_CANCELED
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.sdmd.assignment3.R
import com.sdmd.assignment3.viewmodel.InputActivityViewModel

const val PersonalInputFragmentTAG: String = "PersonalInputFragment"

class PersonalInputFragment : Fragment() {
    private lateinit var cancelButton: Button
    private lateinit var nextButton: Button
    private lateinit var fullNameEditText: TextInputEditText
    private lateinit var dateOfBirthEditText: TextInputEditText
    private lateinit var genderEditText: TextInputEditText
    private lateinit var categoryInputGroup: ChipGroup

    private val inputActivityViewModel: InputActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_personal_input, container, false)
        Log.i(PersonalInputFragmentTAG, "PersonalInputFragment created")
        init(view)
        return view
    }

    // Initialise activity view
    private fun init(view: View) {
        cancelButton = view.findViewById(R.id.inputCancelButton)
        nextButton = view.findViewById(R.id.inputNextButton)
        fullNameEditText = view.findViewById(R.id.fullNameEditText)
        dateOfBirthEditText = view.findViewById(R.id.dobEditText)
        genderEditText = view.findViewById(R.id.genderEditText)
        categoryInputGroup = view.findViewById(R.id.filterInputLabelGroup)
        update()

        // Anonymous function that cancel this input activity
        val cancelInput: (() -> (Unit)) = {
            Toast.makeText(requireActivity(), "Input Cancelled", Toast.LENGTH_SHORT).show()
            requireActivity().setResult(RESULT_CANCELED, requireActivity().intent) // Users cancel so nothing happens
            requireActivity().finish()
        }

        // Anonymous function that moves to the next page of input activity
        val nextPage: (() -> (Unit)) = {
            if(validateInput()) {
                Log.i(PersonalInputFragmentTAG, "Change to ContactInputFragment")

                val currentCategory = view.findViewById<Chip>(categoryInputGroup.checkedChipId).text.toString()
                inputActivityViewModel.setPersonalDetails(fullNameEditText.text.toString(), dateOfBirthEditText.text.toString(), genderEditText.text.toString(), currentCategory)
                Log.i(PersonalInputFragmentTAG, "Set personal details ${inputActivityViewModel.currentProfile.value}")

                inputActivityViewModel.setNextPage()
            }
        }

        // Handles both normal click and long click events
        cancelButton.setOnClickListener { cancelInput.invoke() }
        cancelButton.setOnLongClickListener { cancelInput.invoke(); true }

        nextButton.setOnClickListener { nextPage.invoke() }
        nextButton.setOnLongClickListener { nextPage.invoke(); true }
    }

    // Update edit text fields if the profile has been entered before
    private fun update() {
        inputActivityViewModel.currentProfile.value?.let {
            fullNameEditText.setText(it.name)
            dateOfBirthEditText.setText(it.birthday)
            genderEditText.setText(it.category)

            when(it.category) {
                "Family" -> categoryInputGroup.check(R.id.familyInputChip)
                "Friend" -> categoryInputGroup.check(R.id.friendInputChip)
            }
        }
    }

    // Validate user input before moving to next fragment
    private fun validateInput() : Boolean {
        var isCorrected = true

        // Check if the full name field is filled
        if(fullNameEditText.text.toString().trim().isEmpty()) {
            fullNameEditText.error = getString(R.string.full_name_empty)
            isCorrected = false
            Log.d(PersonalInputFragmentTAG, "Full Name Input: ${fullNameEditText.text.toString()} - Error: The field is empty")
        } else {
            fullNameEditText.error = null
            Log.d(PersonalInputFragmentTAG, "Full Name Input: ${fullNameEditText.text.toString()} - No Error")
        }

        return isCorrected
    }
}