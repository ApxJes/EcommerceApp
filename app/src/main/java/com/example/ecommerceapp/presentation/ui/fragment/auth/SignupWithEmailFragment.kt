package com.example.ecommerceapp.presentation.ui.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.databinding.FragmentSignupWithEmailBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignupWithEmailFragment : Fragment() {

    private var _binding: FragmentSignupWithEmailBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupWithEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            registerUser()
        }

        binding.txvLogin.setOnClickListener {
            val action = SignupWithEmailFragmentDirections.actionSignupWithEmailFragmentToLoginWithEmailFragment()
            findNavController().navigate(action)
        }
    }

    private fun registerUser() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPasswrd.text.toString().trim()

        if (email.isEmpty()) {
            binding.edtEmail.error = "Email is required!"
            binding.edtEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.edtPassword.error = "Password can't be empty!"
            binding.edtPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            binding.edtPassword.error = "Password is too short!"
            binding.edtPassword.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.edtConfirmPasswrd.error = "Confirm password can't be empty!"
            binding.edtConfirmPasswrd.requestFocus()
            return
        }

        if (password != confirmPassword) {
            binding.edtConfirmPasswrd.error = "Passwords do not match!"
            binding.edtConfirmPasswrd.requestFocus()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                showSignupSuccessfulBottomDialog()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showSignupSuccessfulBottomDialog(){
        val bottomSheet = SignupSuccessfulBottomSheet {
            val action =
                SignupWithEmailFragmentDirections.actionSignupWithEmailFragmentToLoginWithEmailFragment()

            findNavController().navigate(action)
        }

        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}