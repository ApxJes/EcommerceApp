package com.example.ecommerceapp.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.Constants.SIGN_IN_REQUEST_CODE
import com.example.ecommerceapp.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.btnLoginWithEmail.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToLoginWithEmailFragment()
            findNavController().navigate(action)
        }

        binding.txvSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupWithEmailFragment()
            findNavController().navigate(action)
        }

        binding.btnLoginWithGoogle.setOnClickListener {
            loginWithGoogle()
        }
    }

    private fun loginWithGoogle() {
        val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_clientId))
            .requestEmail()
            .build()

        val signInClient = GoogleSignIn.getClient(requireContext(), option)
        signInClient.signInIntent.also {
            startActivityForResult(it, SIGN_IN_REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)
                account?.let {
                    googleAuthForFirebase(it)
                }
            } catch (e: ApiException) {
                if(e.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                    Toast.makeText(requireContext(), "Sign-in cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Google sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}