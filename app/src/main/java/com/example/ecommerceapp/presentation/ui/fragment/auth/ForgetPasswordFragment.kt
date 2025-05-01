package com.example.ecommerceapp.presentation.ui.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.databinding.FragmentForgetPasswordBinding
import com.example.ecommerceapp.presentation.ui.fragment.auth.PasswordResetBottomSheet
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetPasswordBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        forgetPassword()
    }

    private fun forgetPassword(){
        binding.btnContinue.setOnClickListener {
            val email = binding.edtForgetEmail.text.toString()

            if(email.isNotEmpty()) {
                sendResetEmail(email)
            } else {
                binding.edtForgetEmail.error = "Email is require!"
                binding.edtForgetEmail.requestFocus()
            }
        }
    }

    private fun sendResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                showSuccessBottomDialog()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Invalid email address",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showSuccessBottomDialog() {
        val bottomSheet = PasswordResetBottomSheet {
            val action = ForgetPasswordFragmentDirections
                .actionForgetPasswordFragmentToLoginWithEmailFragment()
            findNavController().navigate(action)
        }

        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}