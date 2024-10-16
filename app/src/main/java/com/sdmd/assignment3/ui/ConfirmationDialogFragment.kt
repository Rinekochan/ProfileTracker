package com.sdmd.assignment3.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmationDialogFragment(private val title: String, private val content: String) : DialogFragment() {

    // Define an interface for callbacks to the detailActivity
    interface ConfirmationDialogListener {
        fun onConfirmationDialogPositiveClick(dialog: DialogFragment)
        fun onConfirmationDialogNegativeClick(dialog: DialogFragment)
    }

    private var listener: ConfirmationDialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Alert Dialog Builder class
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("Yes") { _, _ ->
                    listener?.onConfirmationDialogPositiveClick(this) // Notify activity of positive click
                }
                .setNegativeButton("No") { _, _ ->
                    listener?.onConfirmationDialogNegativeClick(this) // Notify activity of negative click
                }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Set the listener from the parent fragment
        try {
            listener = if (parentFragment != null) {
                parentFragment as ConfirmationDialogListener
            } else {
                context as ConfirmationDialogListener
            }
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ConfirmationDialogListener")
        }
    }
}