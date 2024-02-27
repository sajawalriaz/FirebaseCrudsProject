package com.firebase.crud.operation.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.firebase.crud.operation.models.User
import com.firebase.crud.operation.viewmodels.AuthViewModel
import com.firebase.crud.operation.viewmodels.DashboardViewModel
import com.firebase.crud.operations.R
import com.firebase.crud.operations.databinding.FragmentLoginBinding
import com.firebase.crud.operations.databinding.FragmentViewDataBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ViewData : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentViewDataBinding? = null
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
        // Inflate the layout using data binding
        _binding = FragmentViewDataBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewData().apply {
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
            binding.texName.text=user.name
            binding.texCnic.text=user.cnic
            binding.texFname.text=user.fatherName
            binding.texId.text=user.id
        }

    }
}