package com.sdmd.assignment3.ui

import android.os.Bundle
import android.transition.Explode
import android.util.Log
import android.view.Window
import android.transition.Fade
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.chip.Chip
import com.sdmd.assignment3.R
import com.sdmd.assignment3.viewmodel.InputActivityViewModel

const val InputActivityTAG: String = "InputActivity"

class InputActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var personalDisplay: Chip
    private lateinit var contactDisplay: Chip
    private val inputActivityViewModel: InputActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set set the transition to be shown when the user enters this activity
            enterTransition = Explode()
            // set the transition to be shown when the user leaves this activity
            exitTransition = Explode()
            exitTransition.duration = 1000
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.input)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        init()
    }

    // Initialise activity view
    private fun init() {
        progressBar = findViewById(R.id.progressBar)
        personalDisplay =  findViewById(R.id.personalProgressTitle)
        contactDisplay = findViewById(R.id.contactProgressTitle)

        personalDisplay.setChipBackgroundColorResource(R.color.backgroundSecondary)
        personalDisplay.setTextColor(getColor(R.color.textPrimary))
        // Observes and change the fragment whenever the currentPage changes
        inputActivityViewModel.currentPage.observe(this) {
            Log.i(InputActivityTAG, "Focused fragment changes. Do fragment switching")
            selectFragment(supportFragmentManager, inputActivityViewModel.currentPage.value!!)
        }
    }

    // Select fragment based on currentPage
    private fun selectFragment(fragmentManager: FragmentManager, currentPage: Int) {
        Log.i(InputActivityTAG, "SelectFragment called")
        when (currentPage) {  // If the user is focusing on personal input page, show PersonalInputFragment
            0 -> {
                Log.d(InputActivityTAG, "Open PersonalInputFragment")
                progressBar.progress = 0
                contactDisplay.setChipBackgroundColorResource(R.color.textPrimary)
                contactDisplay.setTextColor(getColor(R.color.textSecondary))

                val myFragment = PersonalInputFragment()
                fragmentManager.beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                    .replace(R.id.inputFragmentLayout, myFragment).commit()
            }

            1 -> { // If the user is focusing on contact input page, show ContactInputFragment
                Log.d(InputActivityTAG, "Open ContactInputFragment")
                progressBar.progress = 1
                contactDisplay.setChipBackgroundColorResource(R.color.backgroundSecondary)
                contactDisplay.setTextColor(getColor(R.color.textPrimary))

                val myFragment = ContactInputFragment()
                fragmentManager.beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                    .replace(R.id.inputFragmentLayout, myFragment).commit()
            }
        }
    }
}