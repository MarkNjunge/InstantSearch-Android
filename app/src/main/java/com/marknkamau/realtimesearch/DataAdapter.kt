package com.marknkamau.realtimesearch

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_data.view.*

class DataAdapter(private val context: Context) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    private val items by lazy { mutableListOf<DataModel>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_data))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], context)

    override fun getItemCount() = items.size

    fun setItems(items: MutableList<DataModel>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: DataModel, context: Context) {
            itemView.run {
                dataModel.run {
                    tvTitle.text = title
                    tvScore.text = averageScore.toString()
                    Glide.with(context).load(coverImage).into(imgCover)
                }
            }
        }
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}