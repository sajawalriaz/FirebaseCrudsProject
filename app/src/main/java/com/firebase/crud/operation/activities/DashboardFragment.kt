package com.firebase.crud.operation.activities

import UserAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.firebase.crud.operation.models.User
import com.firebase.crud.operation.viewmodels.DashboardViewModel
import com.firebase.crud.operations.R
import com.firebase.crud.operations.databinding.FragmentDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class DashboardFragment : Fragment(), UserAdapter.OnEditClickListener,
    UserAdapter.OnDeleteClickListener, UserAdapter.OnViewClickListener {
    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up Firebase
        val database = FirebaseDatabase.getInstance()
        val databaseRefence = database.getReference("usersData")

        val adapter = UserAdapter()
        binding.itemsDataRecycler.adapter = adapter

        // Set edit and delete listeners for the adapter
        adapter.setOnEditClickListener(this)
        adapter.setOnDeleteClickListener(this)
        adapter.setOnViewClickListener(this)

        // Add ValueEventListener to fetch data from Firebase
        databaseRefence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()

                for (userSnapshot in snapshot.children) {
                    val name = userSnapshot.child("name").getValue(String::class.java) ?: ""
                    val id = userSnapshot.child("id").getValue(String::class.java) ?: ""
                    val cnic = userSnapshot.child("cnic").getValue(String::class.java) ?: ""
                    val fatherName =
                        userSnapshot.child("fatherName").getValue(String::class.java) ?: ""
                    val time =
                        userSnapshot.child("time").getValue(String::class.java) ?: ""

                    val user = User(name, id, cnic, fatherName,time)
                    userList.add(user)
                }

                adapter.setUserList(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })

        // Set click listener for the "Add Data" button
        binding.addData.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addDataFragment)
        }

        // Observe navigateToNextFragment LiveData
        viewModel.navigateToNextFragment.observe(viewLifecycleOwner, Observer { user ->
            // Navigate to the next fragment
            if (user != null) {
                navigateToNextFragment(user)
            }
            // Reset the navigation event after navigation
            viewModel.onNavigationComplete()
        })
    }


    private fun navigateToNextFragment(user: User) {
        val bundle = Bundle()
        bundle.putSerializable("user", user)
        findNavController().navigate(R.id.action_dashboardFragment_to_viewData, bundle)
    }

    override fun onEditClick(user: User) {
        val bundle = Bundle()
        bundle.putSerializable("user", user)
        findNavController().navigate(R.id.action_dashboardFragment_to_updateDataFragment, bundle)

    }

    override fun onViewClick(user: User) {
        val bundle = Bundle()
        bundle.putSerializable("user", user)
        findNavController().navigate(R.id.action_dashboardFragment_to_viewData, bundle)

    }

    override fun onDeleteClick(user: User) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Confirm Deletion")
        alertDialogBuilder.setMessage("Are you sure you want to delete this user information.?")

        alertDialogBuilder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            deleteConfirmed(user)
        }

        alertDialogBuilder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }

        alertDialogBuilder.show()
    }

    private fun deleteConfirmed(user: User) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("usersData")

        val query: Query = databaseReference.orderByChild("id").equalTo(user.id)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.ref.removeValue()
                        .addOnSuccessListener {
                            Log.d("TAG", "Node deleted successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("TAG", "Error deleting node: $exception")
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException())
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        findNavController().popBackStack()
    }
}
