package com.example.myshop.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.retrofit.response.Products
import com.example.myshop.R
import com.example.myshop.databinding.ItemProductBinding

class HomePagingAdapter(private val onProductClick: (Products) -> Unit) :
    PagingDataAdapter<Products, HomePagingAdapter.ListViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Products>() {
            override fun areItemsTheSame(
                oldItem: Products,
                newItem: Products
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Products,
                newItem: Products
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val productData = getItem(position)
        if (productData != null) {
            holder.bind(productData)
            holder.itemView.setOnClickListener {
                onProductClick(productData)
            }
        }
    }

    class ListViewHolder(var binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(productData: Products) {
            binding.apply {
                Glide.with(itemView)
                    .load(productData.thumbnail)
                    .placeholder(R.drawable.image_loading)
                    .into(binding.itemImage)
                binding.itemTitle.text = productData.title
            }
        }
    }
}