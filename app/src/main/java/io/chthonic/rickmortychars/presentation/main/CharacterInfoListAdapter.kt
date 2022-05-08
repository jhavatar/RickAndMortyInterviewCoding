package io.chthonic.rickmortychars.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import io.chthonic.rickmortychars.databinding.CharInfoItemBinding
import io.chthonic.rickmortychars.domain.model.CharacterInfo

class CharacterInfoListAdapter() :
    PagingDataAdapter<CharacterInfo, CharacterInfoListAdapter.ViewHolder>(CharacterInfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(
        private val binding: CharInfoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CharInfoItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(characterInfo: CharacterInfo?) {
            characterInfo?.apply {
                // Note, img library caches results
                binding.charImage.load(image)
                binding.charName.text = name
            }
        }
    }
}

class CharacterInfoDiffCallback : DiffUtil.ItemCallback<CharacterInfo>() {
    override fun areItemsTheSame(
        oldPreferenceItem: CharacterInfo,
        newPreferenceItem: CharacterInfo
    ): Boolean {
        return oldPreferenceItem.id == newPreferenceItem.id
    }

    override fun areContentsTheSame(
        oldPreferenceItem: CharacterInfo,
        newPreferenceItem: CharacterInfo
    ): Boolean {
        return oldPreferenceItem == newPreferenceItem
    }
}