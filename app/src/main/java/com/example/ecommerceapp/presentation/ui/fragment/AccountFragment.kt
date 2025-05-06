package com.example.ecommerceapp.presentation.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var selectImageUri: Uri? = null

    private val resultContract = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri.let {
            selectImageUri = it
            binding.imvProfile.setImageURI(selectImageUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.imvProfile.setOnClickListener {
            resultContract.launch("image/*")
        }
        
        binding.imvSave.setOnClickListener {
            val userName = binding.edtName.text.toString()
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .setPhotoUri(selectImageUri)
                .build()

            auth.currentUser?.updateProfile(profileUpdate)
                ?.addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                        binding.txvName.text = userName
                        binding.txvEmail.text = auth.currentUser?.email
                    } else {
                        Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        auth.currentUser?.let { user ->
            binding.txvName.text = user.displayName
            binding.txvEmail.text = user.email
            user.photoUrl?.let {
                binding.imvProfile.setImageURI(it)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}