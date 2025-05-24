package com.example.ecommerceapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.ItemLayoutBinding
import com.example.ecommerceapp.domain.model.ProductVo

class PagingAdapter :
    PagingDataAdapter<ProductVo, PagingAdapter.ProductsViewHolder>(ProductDiffCallback()) {

    private var onClick: ((ProductVo) -> Unit)? = null
    fun setOnClickListener(listener: (ProductVo) -> Unit) {
        onClick = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsViewHolder {
        val binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(
        holder: ProductsViewHolder,
        position: Int
    ) {
        val product = getItem(position) ?: return

        holder.binding.apply {
            Glide.with(root.context)
                .load(product.thumbnail)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imvProductImage)

            txvProductName.text = product.title
            txvProductPrize.text = "$${product.price}"
            txvRating.text = String.format("%.1f", product.rating)

            root.setOnClickListener {
                onClick?.invoke(product)
            }
        }
    }

    inner class ProductsViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductVo>() {
        override fun areItemsTheSame(oldItem: ProductVo, newItem: ProductVo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductVo, newItem: ProductVo): Boolean {
            return oldItem == newItem
        }
    }
}
