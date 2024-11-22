package com.dicoding.picodiploma.loginwithanimation.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.local.StoryEntity
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemStoryBinding

class ListStoryAdapter(
    private var listStory: List<StoryEntity> = emptyList(),
    private val onItemClick: (StoryEntity) -> Unit
): RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStoryAdapter.ViewHolder, position: Int) {
        val story = listStory[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = listStory.size

    inner class ViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(story: StoryEntity) {
            binding.apply {
                Glide.with(binding.storyImage.context)
                    .load(story.photoUrl)
                    .into(storyImage)
                storyName.text = story.name
                storyDescription.text = story.description

                binding.root.setOnClickListener {
                    onItemClick(story)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateStory(newStories: List<StoryEntity>) {
        listStory = newStories
        notifyDataSetChanged()
    }
}