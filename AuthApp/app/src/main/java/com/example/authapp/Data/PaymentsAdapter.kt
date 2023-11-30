package com.example.authapp.Data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.authapp.databinding.RecyclerItemBinding

class PaymentsAdapter : ListAdapter<Payment, ViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding){
            id.text = item.id
            title.text = item.title
            amount.text = item.amount
            created.text= item.created
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Payment>() {
    override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean =
        oldItem.id == newItem.id
}

class ViewHolder (val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)
