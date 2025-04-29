package com.example.ecommerceapp.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentLoginWithEmailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginWithEmailFragment : Fragment() {

    private var _binding: FragmentLoginWithEmailBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginWithEmailBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            userLogin()
        }

        binding.txvForgetPassword.setOnClickListener {
            val action = LoginWithEmailFragmentDirections.actionLoginWithEmailFragmentToForgetPasswordFragment()
            findNavController().navigate(action)
        }

    }

    private fun userLogin() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

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

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                withContext(Dispatchers.Main) {
                    checkLoggedInState()
                }
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


    private fun checkLoggedInState() {
        if(auth.currentUser == null) {
            Toast.makeText(
                requireContext(),
                "Don't have an account, please SignUp first",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Successfully LoggedIn",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}