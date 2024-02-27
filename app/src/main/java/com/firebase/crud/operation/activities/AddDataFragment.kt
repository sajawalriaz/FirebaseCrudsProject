package com.firebase.crud.operation.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.crud.operation.viewmodels.DashboardViewModel
import com.firebase.crud.operations.databinding.FragmentAddDataBinding
import com.google.firebase.database.FirebaseDatabase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddDataFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAddDataBinding
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
        binding = FragmentAddDataBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up Firebase
        val database = FirebaseDatabase.getInstance()
        val databaseRefrence = database.getReference("usersData")

        binding.uploadData.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val id = binding.id.text.toString().trim()
            val cnic = binding.cnic.text.toString().trim()
            val fatherName = binding.fname.text.toString().trim()
            val time = System.currentTimeMillis().toString()

            // Call ViewModel function to upload data to Firebase
            viewModel.uploadData(name, id, cnic, fatherName,time)
            findNavController().popBackStack()

        }


        viewModel.userData.observe(viewLifecycleOwner) { userData ->
            // Upload data to Firebase
            userData?.let {
                databaseRefrence.child(it.time).setValue(it)
            }
        }
}
}