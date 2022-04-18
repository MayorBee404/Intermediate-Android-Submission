package com.example.storyapplication.view.dashboard.home

import android.content.res.Resources
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapplication.R
import com.example.storyapplication.databinding.ItemRowStoryBinding
import com.example.storyapplication.model.StoryModel
import com.example.storyapplication.utilities.DiffUtilCallback

class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>(){
    private var _listStory = ArrayList<StoryModel>()

    inner class ListViewHolder(binding : ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root){
        var image = binding.storyImage
        var name = binding.storyName
        var description = binding.storyDescription
        var detail = binding.tvDetail
        var isExpanded = false

        val getResources: Resources = binding.root.context.resources
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemrowbinding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(itemrowbinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = _listStory[position]
        holder.apply {
            name.text = user.name
            Log.e("Adapter","${user.image}")
            Glide.with(itemView.context)
                .load(user.image)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(image)
            description.text = user.description

            holder.detail.setOnClickListener{
                TransitionManager.beginDelayedTransition(itemView as ViewGroup)
                if (isExpanded){
                    description.visibility = View.GONE
                    detail.text = getResources.getString(R.string.detail)
                    detail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_keyboard_arrow_down_24, 0,0,0)
                    isExpanded = false
                }else {
                    description.visibility = View.VISIBLE
                    detail.text = getResources.getString(R.string.detail)
                    detail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_keyboard_arrow_up_24,0,0,0)
                    isExpanded = true

                }
            }
        }
    }

    override fun getItemCount(): Int = _listStory.size

    fun setData(newData: ArrayList<StoryModel>) {
        val diffUtilCallback = DiffUtilCallback(_listStory, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        _listStory = newData
        diffResult.dispatchUpdatesTo(this)
    }

}