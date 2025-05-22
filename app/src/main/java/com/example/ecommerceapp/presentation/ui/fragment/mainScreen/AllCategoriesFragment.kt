package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentAllCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCategoriesFragment : Fragment() {

    private var _binding: FragmentAllCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllCategoriesBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToRelateProductsByItsCategory()

        binding.imvBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun navigateToRelateProductsByItsCategory() {

        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()

        binding.btnLaptop.setOnClickListener {
            findNavController().navigate(
                AllCategoriesFragmentDirections.actionAllCategoriesFragmentToLaptopFragment(),
                navOptions
            )
        }

        binding.btnSmartPhone.setOnClickListener {
            findNavController().navigate(
                AllCategoriesFragmentDirections.actionAllCategoriesFragmentToSmartPhoneFragment(),
                navOptions
            )
        }

        binding.btnWatch.setOnClickListener {
            findNavController().navigate(
                AllCategoriesFragmentDirections.actionAllCategoriesFragmentToWatchFragment(),
                navOptions
            )
        }

        binding.btnMobileAccessory.setOnClickListener {
            findNavController().navigate(
                AllCategoriesFragmentDirections.actionAllCategoriesFragmentToMobileAccessoryFragment(),
                navOptions
            )
        }

        binding.btnKitchenAccessory.setOnClickListener {
            findNavController().navigate(
                AllCategoriesFragmentDirections.actionAllCategoriesFragmentToKitchenAccessoriesFragment(),
                navOptions
            )
        }

        binding.btnSportAccessory.setOnClickListener {
            findNavController().navigate(
                AllCategoriesFragmentDirections.actionAllCategoriesFragmentToSportAccessoriesFragment(),
                navOptions
            )
        }

        binding.btnSunglass.setOnClickListener {
            findNavController().navigate(
                AllCategoriesFragmentDirections.actionAllCategoriesFragmentToSunglassesFragment(),
                navOptions
            )
        }

        binding.btnVehicle.setOnClickListener {
            findNavController().navigate(
                AllCategoriesFragmentDirections.actionAllCategoriesFragmentToVehicleFragment(),
                navOptions
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}