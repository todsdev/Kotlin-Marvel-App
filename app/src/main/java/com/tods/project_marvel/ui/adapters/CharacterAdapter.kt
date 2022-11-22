package com.tods.project_marvel.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tods.project_marvel.R
import com.tods.project_marvel.data.model.character.CharacterModel
import com.tods.project_marvel.databinding.ItemCharacterBinding
import com.tods.project_marvel.util.limitedDescription
import com.tods.project_marvel.util.loadImage

class CharacterAdapter: RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    inner class CharacterViewHolder(val binding: ItemCharacterBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<CharacterModel>() {
        override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.name == newItem.name
                    && oldItem.description == newItem.description
                    && oldItem.thumbnail.path == newItem.thumbnail.path
                    && oldItem.thumbnail.extension == newItem.thumbnail.extension
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var characters: List<CharacterModel>
    get() = differ.currentList
    set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.binding.apply {
            tvNameCharacter.text = character.name
            if (character.description == "") {
                tvDescriptionCharacter.text = holder.itemView.context.getString(R.string.empty_description)
            } else {
                tvDescriptionCharacter.text = character.description.limitedDescription(100)
            }
            loadImage(imgCharacter, character.thumbnail.path, character.thumbnail.extension)

        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(character)
            }
        }
    }

    private var onItemClickListener: ((CharacterModel) -> Unit)? = null

    fun setOnClickListener(listener: (CharacterModel) -> Unit) {
        onItemClickListener = listener
    }

    fun getCharacterPosition(position: Int): CharacterModel {
        return characters[position]
    }
}