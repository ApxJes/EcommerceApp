package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentEditAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class EditAccountFragment : Fragment() {

    private var _binding: FragmentEditAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private var selectImageUri: Uri? = null

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                requireContext().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                selectImageUri = it
                binding.imvProfile.setImageURI(it)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditAccountBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
        requireActivity().contentResolver.persistedUriPermissions
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        checkLoginState()

        binding.imvBack.setOnClickListener {
            findNavController().navigate(
                R.id.accountFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.onBoardingFragment, true)
                    .build()
            )
        }

        binding.imvSave.setOnClickListener {
            updateProfile()
        }

        binding.imvProfile.setOnClickListener {
            imagePickerLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun updateProfile() {
        auth.currentUser?.let { user ->
            val userName = binding.edtName.text.toString()
            val builder = UserProfileChangeRequest.Builder()
                .setDisplayName(userName)

            selectImageUri?.let {
                builder.setPhotoUri(it)
            }

            val profileUpdate = builder.build()

            CoroutineScope(Dispatchers.IO).launch {
                try {

                    user.updateProfile(profileUpdate).await()
                    withContext(Dispatchers.Main) {
                        checkLoginState()
                        Toast.makeText(
                            requireContext(),
                            "Successfully updated user profile",
                            Toast.LENGTH_SHORT
                        ).show()
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
    }

    private fun checkLoginState() {
        val user = auth.currentUser
        user?.let {
            binding.txvName.text = user.displayName ?: ""
            binding.txvEmail.text = user.email ?: ""
            binding.edtName.setText(user.displayName ?: "")

            if (user.photoUrl != null) {
                try {
                    binding.imvProfile.setImageURI(user.photoUrl)
                } catch (e: SecurityException) {
                    binding.imvProfile.setImageResource(R.drawable.pp)
                }
            } else {
                binding.imvProfile.setImageResource(R.drawable.pp)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}