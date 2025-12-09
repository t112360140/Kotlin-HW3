package com.example.lab7

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyAdapter(
    context: Context,
    data: List<Item>,
    private val layout: Int
) : ArrayAdapter<Item>(context, layout, data) {

    // 定義 ViewHolder 內部類別，用來暫存 View 元件
    private class ViewHolder(view: View) {
        val imgPhoto: ImageView = view.findViewById(R.id.imgPhoto)
        val tvMsg: TextView = view.findViewById(R.id.tvMsg)
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val view: View
        val holder: ViewHolder

        // 如果 convertView 為 null，表示沒有可重用的畫面，需要建立新的
        if (convertView == null) {
            view = View.inflate(parent.context, layout, null)
            holder = ViewHolder(view)
            // 將 ViewHolder 存入 View 的 Tag 中
            view.tag = holder
        } else {
            // 如果有可重用的畫面，直接取出 Tag 中的 ViewHolder
            view = convertView
            holder = view.tag as ViewHolder
        }

        // 依據 position 取得對應的資料內容
        val item = getItem(position) ?: return view

        // 使用 ViewHolder 設定資料，避免重複 findViewById
        holder.imgPhoto.setImageResource(item.photo)
        
        holder.tvMsg.text = if (layout == R.layout.adapter_vertical) {
            item.name
        } else {
            "${item.name}: ${item.price}元"
        }

        // 回傳此項目的畫面
        return view
    }
}