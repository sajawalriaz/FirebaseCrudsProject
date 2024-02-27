package com.firebase.crud.operation.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.crud.operation.viewmodels.SignUpResult
import com.firebase.crud.operation.viewmodels.SignUpViewModel
import com.firebase.crud.operations.R
import com.firebase.crud.operations.databinding.FragmentSignUpBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    val viewModel: SignUpViewModel by viewModels()
    var email: String = ""
    var password: String = ""
    var confirmPassword: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // Assuming you have a ViewModel associated with this fragment

        viewModel.signUpResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is SignUpResult.Success -> {
                    // Navigate to LoginActivity on successful sign-up
                    findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                }

                is SignUpResult.Failure -> {
                    // Show error message using showToast method
                    val error = result.error
                    showToast(error)
                }
            }
        }

        // Set click listener for "already logged" text
        binding.alreadyLogged.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }



        binding.button.setOnClickListener {
            email = binding.emailEt.text.toString()
            password = binding.passET.text.toString()
            confirmPassword = binding.confirmPassEt.text.toString()
            viewModel.signUp(email, password, confirmPassword)
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}