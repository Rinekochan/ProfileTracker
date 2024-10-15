package com.sdmd.assignment3.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.util.Log
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sdmd.assignment3.R
import com.sdmd.assignment3.viewmodel.MainActivityViewModel

const val MainActivityTAG: String = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var fabAddButton: FloatingActionButton
    private lateinit var categories: ChipGroup
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

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
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }
        init()
    }

    // Initialise activity view and viewmodel
    private fun init() {
        initView()
        initViewModel()
    }

    // Initialise activity view
    private fun initView() {
        fabAddButton = findViewById(R.id.addFabButton)
        categories = findViewById(R.id.filterGroup)

        // Anonymous function handles intent creation to InputActivity
        val addNewItem: (() -> (Unit)) = {
            Log.i(MainActivityTAG, "Create intent from MainActivity to InputActivity")
            val intent = Intent(this, InputActivity::class.java)
            startForResult.launch(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this))
        }

        // Handles both normal click and long click events
        fabAddButton.setOnClickListener { addNewItem.invoke() }
        fabAddButton.setOnLongClickListener { addNewItem.invoke(); true }

        // Update view model if any category is checked
        categories.setOnCheckedStateChangeListener { _, checkedId ->
            Log.d(MainActivityTAG, "${checkedId[0]} checked")
            mainActivityViewModel.selectCategory(findViewById<Chip>(checkedId[0]).text.toString())
        }
    }

    // Initialise the call for results intent
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Log.i(MainActivityTAG, "Receive intent result to MainActivity")
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    Log.i(MainActivityTAG, "Saving profile successfully")
                    val intent = result.data
                    // Handle the Intent
                    intent?.getParcelableExtra("", InputActivity::class.java)?.let {
//                    mainActivityViewModel.create(it) // Create new object to the database
                    }
                }
                Activity.RESULT_CANCELED -> {
                    Log.i(MainActivityTAG, "Cancel profile creation/modification")
                }
            }
        }

    // Initialise activity viewmodel
    private fun initViewModel() {

    }

}