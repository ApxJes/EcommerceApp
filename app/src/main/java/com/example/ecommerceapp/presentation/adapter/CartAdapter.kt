package com.example.ecommerceapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.databinding.CartItemLayoutBinding
import com.example.ecommerceapp.domain.model.ProductVo

class CartAdapter(
    private val onQuantityChanged: () -> Unit,
    private val onItemsRemove: (ProductVo) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartItemViewHolder>() {

    private val products = mutableListOf<ProductVo>()
    private val quantities = mutableMapOf<ProductVo, Int>()

    private var onClick: ((ProductVo) -> Unit)? = null
    fun setOnClickListener(listener: (ProductVo) -> Unit) {
        onClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = CartItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val product = products[position]
        val quantity = quantities[product] ?: 1

        holder.binding.apply {
            Glide.with(root.context).load(product.thumbnail).into(imvCartImage)
            txvCartItemName.text = product.title
            txvCartItemPrice.text = "$${product.price}"
            txvItemCount.text = quantity.toString()
            txvCartItemTotalPrice.text = "$" + String.format("%.2f", product.price?.times(quantity))

            root.setOnClickListener {
                onClick?.invoke(product)
            }

            plus.setOnClickListener {
                quantities[product] = quantity + 1
                notifyItemChanged(position)
                onQuantityChanged()
            }

            minus.setOnClickListener {
                if (quantity > 1) {
                    quantities[product] = quantity - 1
                    notifyItemChanged(position)
                    onQuantityChanged()
                } else {
                    val index = products.indexOf(product)
                    products.removeAt(index)
                    quantities.remove(product)
                    notifyItemRemoved(index)
                    onQuantityChanged()
                    onItemsRemove(product)
                }
            }
        }
    }

    override fun getItemCount(): Int = products.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(newItems: List<ProductVo>) {
        products.clear()
        products.addAll(newItems)
        quantities.clear()
        newItems.forEach { product ->
            quantities[product] = 1
        }
        notifyDataSetChanged()
    }

    fun getTotalPrice(): Double {
        return quantities.entries.sumOf { (product, quantity) ->
            (product.price ?: 0.0) * quantity
        }
    }

    inner class CartItemViewHolder(val binding: CartItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
