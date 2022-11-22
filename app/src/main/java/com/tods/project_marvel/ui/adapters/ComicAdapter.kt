package com.tods.project_marvel.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tods.project_marvel.R
import com.tods.project_marvel.data.model.character.CharacterModel
import com.tods.project_marvel.data.model.comic.ComicModel
import com.tods.project_marvel.databinding.ItemCharacterBinding
import com.tods.project_marvel.databinding.ItemComicBinding
import com.tods.project_marvel.util.limitedDescription
import com.tods.project_marvel.util.loadImage

class ComicAdapter: RecyclerView.Adapter<ComicAdapter.ComicViewHolder>() {

    inner class ComicViewHolder(val binding: ItemComicBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<ComicModel>() {
        override fun areItemsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.title == newItem.title
                    && oldItem.description == newItem.description
                    && oldItem.thumbnail.path == newItem.thumbnail.path
                    && oldItem.thumbnail.extension == newItem.thumbnail.extension
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    var comics: List<ComicModel>
    get() = differ.currentList
    set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder(
            ItemComicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = comics.size

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comic = comics[position]
        holder.binding.apply {
            tvNameComic.text = comic.title
            tvDescriptionComic.text = comic.description
            loadImage(imgComic, comic.thumbnail.path, comic.thumbnail.extension)
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(comic)
            }
        }
    }

    private var onItemClickListener: ((ComicModel) -> Unit)? = null

    fun setOnClickListener(listener: (ComicModel) -> Unit) {
        onItemClickListener = listener
    }
}