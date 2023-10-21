package com.example.myshop.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.retrofit.response.Products
import com.example.core.retrofit.response.SearchProducts
import com.example.myshop.R
import com.example.myshop.databinding.ItemProductBinding

class HomeSearchAdapter(
    private val onProductClick: (SearchProducts) -> Unit
) : ListAdapter<SearchProducts, HomeSearchAdapter.ListViewHolder>(SearchProductDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }

    class ListViewHolder(var binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(data: SearchProducts) {
            binding.apply {
                Glide.with(itemView)
                    .load(data.thumbnail)
                    .placeholder(R.drawable.image_loading)
                    .into(binding.itemImage)
                binding.itemTitle.text = data.title
            }
        }
    }

    private class SearchProductDiffCallback : DiffUtil.ItemCallback<SearchProducts>() {
        override fun areItemsTheSame(oldItem: SearchProducts, newItem: SearchProducts): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: SearchProducts, newItem: SearchProducts): Boolean {
            return oldItem == newItem
        }
    }

}