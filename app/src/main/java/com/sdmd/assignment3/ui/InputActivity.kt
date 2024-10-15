package com.sdmd.assignment3.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.sdmd.assignment3.R
import com.sdmd.assignment3.viewmodel.InputActivityViewModel

const val InputActivityTAG: String = "InputActivity"

class InputActivity : AppCompatActivity() {
    private val inputActivityViewModel: InputActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
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

    private fun init() {
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
                val myFragment = PersonalInputFragment()
                fragmentManager.beginTransaction().replace(R.id.inputFragmentLayout, myFragment)
                    .commit()
            }

            1 -> { // If the user is focusing on contact input page, show ContactInputFragment
                Log.d(InputActivityTAG, "Open ContactInputFragment")
                val myFragment = ContactInputFragment()
                fragmentManager.beginTransaction().replace(R.id.inputFragmentLayout, myFragment)
                    .commit()
            }
        }
    }
}