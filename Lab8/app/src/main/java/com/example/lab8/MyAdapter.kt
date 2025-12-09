package com.example.lab8

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.databinding.AdapterRowBinding

// 改繼承 ListAdapter，並傳入 DiffCallback
class MyAdapter(
    private val onItemClick: (Contact) -> Unit // 傳入點擊事件的 Lambda
) : ListAdapter<Contact, MyAdapter.ViewHolder>(DiffCallback) {

    // DiffUtil 用來比較新舊資料差異，提升效能
    companion object DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            // 在實際專案通常比對 ID，這裡比對物件本身
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }

    // ViewHolder 使用 ViewBinding
    class ViewHolder(private val binding: AdapterRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contact, clickListener: (Contact) -> Unit) {
            binding.tvName.text = item.name
            binding.tvPhone.text = item.phone
            // 設定刪除按鈕監聽
            binding.imgDelete.setOnClickListener {
                clickListener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 使用 ViewBinding 建立畫面
        val binding = AdapterRowBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
}