package com.example.ecommerceapp.presentation.ui.fragment.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.databinding.FragmentReviewBottomSheetBinding
import com.example.ecommerceapp.domain.model.ReviewVo
import com.example.ecommerceapp.presentation.adapter.ReviewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReviewBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentReviewBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewAdapter: ReviewAdapter
    private var reviewList: List<ReviewVo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reviewList = arguments?.getSerializable("reviewList") as? List<ReviewVo> ?: emptyList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviewAdapter = ReviewAdapter()
        binding.rcvReview.apply {
            adapter = reviewAdapter
            reviewAdapter.differ.submitList(reviewList)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.txvBack.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance(reviews: List<ReviewVo>): ReviewBottomSheet {
            val bundle = Bundle().apply {
                putSerializable("reviewList", ArrayList(reviews))
            }
            return ReviewBottomSheet().apply {
                arguments = bundle
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}