package com.sdmd.assignment3.ui

import android.widget.LinearLayout
import com.sdmd.assignment3.R
import com.sdmd.assignment3.model.Profile
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

const val ProfileAdapterTAG = "ProfileAdapter"
// Adapter for managing a list of profiles in a RecyclerView
class ProfileAdapter(private var data: List<Profile>,
                     private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_layout, parent, false) as View // Inflate the product item layout
        return ViewHolder(view)
    }

    // Returns the total number of items in the data set
    override fun getItemCount() = data.size

    // Called to display the data at the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    // Update the adapter if the list has changed
    fun updateList(profiles: List<Profile>) {
        Log.d(ProfileAdapterTAG, "Update List $profiles")
        data = profiles
        notifyDataSetChanged()
    }

    // ViewHolder class for holding and binding item views
    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val categoryLayout: LinearLayout = v.findViewById(R.id.cardLabelLayout)
        private val category: TextView = v.findViewById(R.id.cardLabel)
        private val name: TextView = v.findViewById(R.id.cardTitle)
        private val dob: TextView = v.findViewById(R.id.cardDob)

        // Bind the product data to the view elements
        fun bind(profile: Profile) {
            category.text = profile.category
            name.text = profile.name
            dob.text = profile.birthday

            when(profile.category) {
                "Family" -> categoryLayout.setBackgroundResource(R.color.backgroundSecondary)
                "Friend" -> categoryLayout.setBackgroundResource(R.color.backgroundTertiary)
            }

            // Set a click listener for the row to handle item detail selection
            v.findViewById<Button>(R.id.detailButton).setOnClickListener {
                onItemClickListener.itemDetail(profile)
            }

            // Set a click listener for the delete button to handle item deletion
            v.findViewById<Button>(R.id.deleteButton).setOnClickListener {
                onItemClickListener.itemDelete(profile)
            }
        }
    }

    // Interface for handling item detail and delete events, the activity will overwrite this to define the behaviour
    interface OnItemClickListener {
        fun itemDetail(item: Profile)
        fun itemDelete(item: Profile)
    }
}