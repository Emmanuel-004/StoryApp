package com.dicoding.picodiploma.loginwithanimation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoUrl = intent.getStringExtra("PHOTO_URL")
        val name = intent.getStringExtra("NAME")
        val description = intent.getStringExtra("DESCRIPTION")

        binding.apply {
            detailNameStory.text = name
            detailDescriptionStory.text = description
            Glide.with(this@DetailStoryActivity)
                .load(photoUrl)
                .into(detailImageStory)
        }

    }
}