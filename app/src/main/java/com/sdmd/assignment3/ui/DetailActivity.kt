package com.sdmd.assignment3.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sdmd.assignment3.R
import com.sdmd.assignment3.model.Profile

const val DetailActivityTAG = "DetailActivity"

class DetailActivity : AppCompatActivity() {
    private lateinit var selectedProfile: Profile
    private lateinit var categoryLayout: LinearLayout
    private lateinit var category: TextView
    private lateinit var profileName: TextView
    private lateinit var gender: TextView
    private lateinit var dob: TextView
    private lateinit var phone: TextView
    private lateinit var address: TextView
    private lateinit var returnButton: Button
    private lateinit var modifyButton: Button

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
        setContentView(R.layout.activity_detail)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }
        init()
    }

    // Initialise the receiving profile intent and activity view
    private fun init() {
        initIntent()
        initView()
    }

    // Initialise the receiving profile intent
    private fun initIntent() {
        selectedProfile = intent.getParcelableExtra("Profile", Profile::class.java)!!
        Log.i(DetailActivityTAG, "Receive profile with $selectedProfile")
    }

    // Initialise activity view
    private fun initView() {
        categoryLayout = findViewById(R.id.profileLabelLayout)
        category = findViewById(R.id.profileLabel)
        profileName = findViewById(R.id.profileName)
        gender = findViewById(R.id.genderFieldContent)
        dob = findViewById(R.id.dobFieldContent)
        phone = findViewById(R.id.phoneFieldContent)
        address = findViewById(R.id.addressFieldContent)
        returnButton = findViewById(R.id.detailReturnButton)
        modifyButton = findViewById(R.id.detailModifyButton)

        // Populate the fields with the selected profile
        selectedProfile.let{
            category.text = it.category
            profileName.text = it.name
            gender.text = it.gender
            dob.text = it.birthday
            phone.text = it.phone
            if(it.suburb != "") {
                address.text = "${it.address}, ${it.suburb}"
            } else {
                address.text = it.address
            }

            when(it.category) {
                "Family" -> categoryLayout.setBackgroundResource(R.color.backgroundSecondary)
                "Friend" -> categoryLayout.setBackgroundResource(R.color.backgroundTertiary)
            }
        }

        // Anonymous function that returns the user to the main activity
        val returnToMainActivity: (() -> (Unit)) = {
            setResult(RESULT_CANCELED, intent) // Users cancel so nothing happens
            finish()
        }

        // Anonymous function that moves to the input activity to modify profile
        val modifyProfile: (() -> (Unit)) = {
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra("Profile", selectedProfile)
            startForResult.launch(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this))
        }

        // Handles both normal click and long click events
        returnButton.setOnClickListener { returnToMainActivity.invoke() }
        returnButton.setOnLongClickListener { returnToMainActivity.invoke(); true }

        modifyButton.setOnClickListener { modifyProfile.invoke() }
        modifyButton.setOnLongClickListener { modifyProfile.invoke(); true }
    }

    // Initialise the call for results profile creation intent
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Log.i(DetailActivityTAG, "Receive intent result to DetailActivity")
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    Log.i(DetailActivityTAG, "Modify profile successfully")
                    val intent = result.data
                    // Handle the Intent
                    intent?.getParcelableExtra("Profile", Profile::class.java)?.let {
                        intent.putExtra("Profile", it)
                        setResult(RESULT_OK, intent) // The profile is modified, send the user back to main activity immediately
                        finish()
                    }
                }
                Activity.RESULT_CANCELED -> {
                    Log.i(DetailActivityTAG, "Cancel profile modification")
                }
            }
        }
}