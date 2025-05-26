package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerceapp.databinding.FragmentAboutMeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutMeFragment : Fragment() {

    private var _binding: FragmentAboutMeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutMeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imvFacebook.setOnClickListener {
            Intent(Intent.ACTION_VIEW).also {intent ->
                intent.data = Uri.parse("https://www.facebook.com/share/1Dkf69S2sH/")
                startActivity(intent)
            }
        }

        binding.imvInstagram.setOnClickListener {
            Intent(Intent.ACTION_VIEW).also { intent ->
                intent.data = Uri.parse("https://www.instagram.com/ap20y_?igsh=MXQ1cGJtcXo1YWxzaw==")
                startActivity(intent)
            }
        }

        binding.imvPhone.setOnClickListener {
            Intent(Intent.ACTION_DIAL).also {intent ->
                intent.data = Uri.parse("tel: 09759090762")
                startActivity(intent)
            }
        }

        binding.imvGitHub.setOnClickListener{
            Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse("https://github.com/ApxJes")
                startActivity(it)
            }
        }

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}