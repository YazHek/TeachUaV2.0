package com.softserve.teachua.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.softserve.teachua.R
import com.softserve.teachua.model.CategoryModel
import kotlinx.android.synthetic.main.banner_item.view.*
import kotlinx.android.synthetic.main.category_item.view.*

class CategoriesAdapter(context: Context) :
    ListAdapter<CategoryModel, CategoriesAdapter.CategoriesViewHolder>(DiffCallback()) {


    private val base = "https://speak-ukrainian.org.ua/dev/"
    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(layoutInflater
            .inflate(R.layout.category_item, parent, false))
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CategoriesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(model: CategoryModel) {
            itemView.categoryTitle.text = model.categoryName
            itemView.categoryDescription.text = model.categoryDescription


            if (model.categoryUrlLogo.endsWith(".svg"))
                GlideToVectorYou
                    .init()
                    .with(layoutInflater.context)
                    .load((base + model.categoryUrlLogo).toUri(), itemView.categoryLogo)
           itemView.categoryBackground.setCardBackgroundColor(Color.parseColor(model.categoryBackgroundColor))


        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<CategoryModel>() {

    override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
        return oldItem.categoryId == newItem.categoryId
    }

    override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
        return oldItem == newItem
    }
}