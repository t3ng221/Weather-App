package com.example.weatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ForecastItemRowBinding
import com.example.weatherapp.models.ForecastModel

class ForecastAdapter : ListAdapter<ForecastModel.ForecastList, ForecastAdapter.ForecastViewHolder>(ForecastDiffUtil()){

    class ForecastViewHolder(val binding: ForecastItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ForecastModel.ForecastList){
            binding.forecast = item
        }
    }

    class ForecastDiffUtil : DiffUtil.ItemCallback<ForecastModel.ForecastList>(){
        override fun areItemsTheSame(
            oldItem: ForecastModel.ForecastList,
            newItem: ForecastModel.ForecastList
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastModel.ForecastList,
            newItem: ForecastModel.ForecastList
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ForecastItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}