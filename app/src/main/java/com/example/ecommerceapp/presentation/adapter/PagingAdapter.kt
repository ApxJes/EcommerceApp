package com.example.ecommerceapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.R
import com.example.ecommerceapp.domain.model.Product

class PagingAdapter :
    PagingDataAdapter<Product, PagingAdapter.ProductsViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ProductsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ProductsViewHolder,
        position: Int
    ) {
        val product = getItem(position) ?: return

        holder.apply {
            Glide.with(itemView.context)
                .load(product.thumbnail)
                .into(productImage)

            productName.text = product.title
            productPrice.text = "$"+ product.price.toString()+" USD"

            itemView.setOnClickListener {
                onClick?.invoke(product)
            }

            btnAddToCart.setOnClickListener {
                addToCartClick?.invoke(product)
            }

        }
    }

    private var onClick: ((Product) -> Unit)? = null
    private var addToCartClick: ((Product) -> Unit)? = null

    fun setOnClickListener(listener: (Product) -> Unit) {
        onClick = listener
    }

    fun setOnAddToCartClickListener(listener: (Product) -> Unit) {
        addToCartClick = listener
    }

    inner class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.imvProductImage)
        val productName: TextView = itemView.findViewById(R.id.txvProductName)
        val productPrice: TextView = itemView.findViewById(R.id.txvProductPrize)
        val btnAddToCart: ImageView = itemView.findViewById(R.id.btnAddToCart)
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}