package com.sdmd.assignment3.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.sdmd.assignment3.R
import com.sdmd.assignment3.model.Profile
import com.sdmd.assignment3.viewmodel.MainActivityViewModel

const val MainActivityTAG: String = "MainActivity"

class MainActivity : AppCompatActivity(),
    ProfileAdapter.OnItemClickListener,
    ConfirmationDialogFragment.ConfirmationDialogListener {
    private lateinit var fabAddButton: FloatingActionButton
    private lateinit var categories: ChipGroup
    private lateinit var profilesList: RecyclerView

    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var list: MutableList<Profile>
    private var selectedProfile: Profile? = null

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
        profilesList = findViewById(R.id.itemList)

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
            Log.d(MainActivityTAG, "${findViewById<Chip>(checkedId[0]).text} category checked")
            mainActivityViewModel.selectCategory(findViewById<Chip>(checkedId[0]).text.toString())
        }
        initRecyclerView(mainActivityViewModel.profiles.value!!)
    }

    // Initialise the call for results profile creation intent
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Log.i(MainActivityTAG, "Receive intent result to MainActivity")
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    Log.i(MainActivityTAG, "Saving profile successfully")
                    val intent = result.data
                    // Handle the Intent
                    intent?.getParcelableExtra("Profile", Profile::class.java)?.let {
                        if(it.id == null) { // If the profile id is not assigned, it's a new profile
                            mainActivityViewModel.insertProfile(it) // Create new object to the database
                        }
                    }
                }

                Activity.RESULT_CANCELED -> {
                    Log.i(MainActivityTAG, "Cancel profile creation/modification")
                }
            }
        }

    // Initialise Recycler View
    private fun initRecyclerView(profiles: List<Profile>) {
        list = mutableListOf()
        list.addAll(profiles)

        profileAdapter = ProfileAdapter(list, this)
        profilesList.adapter = profileAdapter
        profilesList.layoutManager = LinearLayoutManager(this)
    }

    // Initialise activity viewmodel
    private fun initViewModel() {
        mainActivityViewModel.profiles.observe(this) {
            list.addAll(it)
            profileAdapter.updateList(it)
        }

        // Show list depends on category
        mainActivityViewModel.selectedCategory.observe(this) {
            val filteredList: MutableList<Profile> = mutableListOf()
            if(it == "All") {
                profileAdapter.updateList(list)
            } else {
                list.forEach { profile ->
                    if(profile.category == it){
                        filteredList.add(profile)
                    }
                }
                profileAdapter.updateList(filteredList)
            }
        }
    }

    // Overwrite behaviour for item in recycler view
    override fun itemDetail(item: Profile) { // Move to DetailActivity if the user clicks detail button
        selectedProfile = item
        Log.i(MainActivityTAG, "Create intent from MainActivity to DetailActivity")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("Profile", selectedProfile)
        startForResult.launch(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this))
    }

    override fun itemDelete(item: Profile) { // Shows delete confirmation dialog when the user clicks delete button
        selectedProfile = item
        Log.i(MainActivityTAG, "Delete Dialog pops up")
        // Call the confirmation dialog to ensure this is not an accident
        val dialog = ConfirmationDialogFragment("Delete confirmation", "Are you sure you want to delete this profile?")
        dialog.show(supportFragmentManager, "DeleteDialogFragment")
    }

    // Actions for dialog if users click Yes
    override fun onConfirmationDialogPositiveClick(dialog: DialogFragment) {
        Log.i(ContactInputFragmentTAG, "User clicks Yes - Profile ${selectedProfile.toString()} Deleted")
        selectedProfile?.let { mainActivityViewModel.deleteProfile(it) }
    }

    // Actions for dialog if users click No
    override fun onConfirmationDialogNegativeClick(dialog: DialogFragment) {
        Log.i(ContactInputFragmentTAG, "User clicks No - Action Cancelled")
        // Handle the Cancel action from cancel dialog
        Toast.makeText(this, "Action Cancelled", Toast.LENGTH_SHORT).show()
    }
}