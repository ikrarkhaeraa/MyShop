package com.example.myshop.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.room.FavoriteEntity
import com.example.myshop.R
import com.example.myshop.databinding.ItemFavoriteBinding
import java.text.NumberFormat
import java.util.Locale

class FavoriteAdapter(
    private val deleteItem: (FavoriteEntity) -> Unit,
    private val onButtonClick: (FavoriteEntity) -> Unit
) :
    ListAdapter<FavoriteEntity, FavoriteAdapter.ListViewHolder>(CartEntityDiffCallback()) {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val favoriteEntity = getItem(position)
        holder.bind(favoriteEntity)

        val goToDetailButton = holder.binding.goToDetail
        goToDetailButton.setOnClickListener {
            onButtonClick(favoriteEntity)
        }


        val deleteIcon = holder.binding.deleteIcon
        deleteIcon.setOnClickListener {
            deleteItem(favoriteEntity)
        }
    }

    class ListViewHolder(var binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(data: FavoriteEntity) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(data.thumbnail)
                    .placeholder(R.drawable.image_loading)
                    .into(itemImage)
                itemTitle.text = data.title
            }
        }
    }

    private class CartEntityDiffCallback : DiffUtil.ItemCallback<FavoriteEntity>() {
        override fun areItemsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
            return oldItem == newItem
        }
    }
}