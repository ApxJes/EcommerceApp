package com.example.ecommerceapp.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentSignupSuccessfulBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SignupSuccessfulBottomSheet(
    private val onDoneClick: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentSignupSuccessfulBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupSuccessfulBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txvSignUpSuccessfulMessage.text = "Signup Successful"
        binding.btnDone.setOnClickListener {
            dismiss()
            onDoneClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}