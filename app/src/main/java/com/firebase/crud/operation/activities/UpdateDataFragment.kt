package com.firebase.crud.operation.activities

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.crud.operation.models.User
import com.firebase.crud.operation.viewmodels.DashboardViewModel
import com.firebase.crud.operations.R
import com.firebase.crud.operations.databinding.FragmentUpdateDataBinding
import com.firebase.crud.operations.databinding.FragmentViewDataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class UpdateDataFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentUpdateDataBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateDataBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpdateDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getSerializable("user") as User?
        if (user != null) {
            Log.d("Debug", "User: $user")
            if (user != null) {
                Log.d("Debug", "User: $user")
                binding.name.setText(user.name)
                binding.cnic.setText(user.cnic)
                binding.fname.setText(user.fatherName)
                binding.id.setText(user.id)
            }
        }
        binding.uploadData.setOnClickListener {
            updateUserInFirebase(user)
            findNavController().popBackStack()

        }
    }
    fun String.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }
    private fun updateUserInFirebase(user: User?) {
        if (user != null) {
            val newName = binding.name.text.toString()
            val newCnic = binding.cnic.text.toString()
            val newFatherName = binding.fname.text.toString()
            val newId = binding.id.text.toString()

            val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("usersData")
            val userReference = databaseReference.child(user.time)
            user.name = newName
            user.cnic = newCnic
            user.fatherName = newFatherName
            user.id = newId

            val userMap: Map<String, Any> = user.toMap()

            userReference.updateChildren(userMap)
                .addOnSuccessListener {
                    // User data updated successfully

                }
                .addOnFailureListener { exception ->

                    Log.e("TAG", "Error updating user data: $exception")
                }
        } else {
            Log.e("TAG", "User is null")
        }
    }
}