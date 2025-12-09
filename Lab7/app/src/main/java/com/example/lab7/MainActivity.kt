package com.example.lab7

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab7.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // 使用 ViewBinding 初始化
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 儲存購買數量資訊
        val count = ArrayList<String>()
        // 儲存水果資訊
        val item = ArrayList<Item>()
        // 建立價格範圍
        val priceRange = IntRange(10, 100)

        // 從 R 類別讀取圖檔
        val array = resources.obtainTypedArray(R.array.image_list)
        for (index in 0 until array.length()) {
            // 水果圖片 Id
            val photo = array.getResourceId(index, 0)
            // 水果名稱
            val name = "水果${index + 1}"
            // 亂數產生價格
            val price = priceRange.random()
            // 新增可購買數量資訊
            count.add("${index + 1}個")
            // 新增水果資訊
            item.add(Item(photo, name, price))
        }
        // 釋放圖檔資源
        array.recycle()

        // 建立 ArrayAdapter 物件，並傳入字串與 simple_list_item_1.xml
        binding.spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            count
        )

        // 設定 GridView
        binding.gridView.numColumns = 3
        binding.gridView.adapter = MyAdapter(this, item, R.layout.adapter_vertical)

        // 設定 ListView
        binding.listView.adapter = MyAdapter(this, item, R.layout.adapter_horizontal)
    }
}