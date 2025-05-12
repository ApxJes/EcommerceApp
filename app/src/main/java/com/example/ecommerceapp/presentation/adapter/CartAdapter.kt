package com.example.ecommerceapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.R
import com.example.ecommerceapp.domain.model.Product

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartItemViewHolder>() {

    private val products = mutableListOf<Product>()
    private val quantities = mutableMapOf<Product, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        return CartItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item_layout, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val product = products[position]
        val quantity = quantities[product] ?: 1

        with(holder) {
            Glide.with(itemView.context).load(product.thumbnail).into(cartImage)
            cartProductName.text = product.title
            cartProductPrice.text = "$${product.price}"
            txvItemCount.text = quantity.toString()
            cartProductTotalPrice.text = "$${product.price?.times(quantity)}"

            plus.setOnClickListener {
                quantities[product] = quantity + 1
                notifyItemChanged(position)
            }

            minus.setOnClickListener {
                if (quantity > 1) {
                    quantities[product] = quantity - 1
                    notifyItemChanged(position)
                } else {
                    val index = products.indexOf(product)
                    products.removeAt(index)
                    quantities.remove(product)
                    notifyItemRemoved(index)
                }
            }
        }
    }

    override fun getItemCount(): Int = products.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(newItems: List<Product>) {
        products.clear()
        products.addAll(newItems)
        quantities.clear()
        newItems.forEach { product ->
            quantities[product] = 1
        }
        notifyDataSetChanged()
    }

    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartImage: ImageView = itemView.findViewById(R.id.imvCartImage)
        val cartProductName: TextView = itemView.findViewById(R.id.txvCartItemName)
        val cartProductPrice: TextView = itemView.findViewById(R.id.txvCartItemPrice)
        val cartProductTotalPrice: TextView = itemView.findViewById(R.id.txvCartItemTotalPrice)
        val txvItemCount: TextView = itemView.findViewById(R.id.txvItemCount)
        val plus: ImageView = itemView.findViewById(R.id.plus)
        val minus: ImageView = itemView.findViewById(R.id.minus)
    }
}
