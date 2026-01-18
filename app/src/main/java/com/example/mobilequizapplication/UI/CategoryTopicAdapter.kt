package com.example.mobilequizapplication.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.databinding.ItemCategoryTopicBinding

class CategoryTopicAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryTopicAdapter.CategoryViewHolder>() {


    private val colors = listOf(
        "#E0F7FA",
        "#FFF9C4",
        "#F8BBD0",
        "#C8E6C9"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val binding = ItemCategoryTopicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)


        val color = Color.parseColor(colors[position % colors.size])
        holder.binding.cardCategory.setCardBackgroundColor(color)
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(val binding: ItemCategoryTopicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onCategoryClick(categories[adapterPosition])
                }
            }
        }

        fun bind(category: Category) {

            binding.tvCategoryName.text = category.displayName


            Glide.with(itemView.context)
                .load("https://api.dicebear.com/7.x/bottts/png?seed=${category.displayName}")
                .into(binding.ivCategoryImage)
        }
    }
}
