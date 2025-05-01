package com.example.ecommerceapp.presentation.ui.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerceapp.databinding.FragmentLoginSuccessfulBottomDialogSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LoginSuccessfulBottomSheet(
    private val onClick: () -> Unit
): BottomSheetDialogFragment() {

    private var _binding: FragmentLoginSuccessfulBottomDialogSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginSuccessfulBottomDialogSheetBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txvLoginSuccessfulMessage.text = "Login Successful"
        binding.btnDone.setOnClickListener {
            dismiss()
            onClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}