package com.example.ecommerceapp.presentation.ui.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerceapp.databinding.FragmentPasswordResetBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PasswordResetBottomSheet(
    private val onOkClick: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentPasswordResetBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordResetBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMessage.text = "We will send a link to your email for resetting password"

        binding.btnOk.setOnClickListener {
            dismiss()
            onOkClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}