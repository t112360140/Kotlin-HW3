package com.example.lab8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: MyAdapter
    
    // 雖然使用 ListAdapter，我們還是在此維護一份資料來源
    private val contacts = ArrayList<Contact>()

    // 宣告 ActivityResultLauncher
    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val name = intent?.getStringExtra("name") ?: ""
            val phone = intent?.getStringExtra("phone") ?: ""
            
            // 新增資料
            contacts.add(Contact(name, phone))
            
            // 更新 Adapter，注意要傳送新的 List 實例 (toList) 讓 DiffUtil 偵測變化
            myAdapter.submitList(contacts.toList())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 初始化 RecyclerView 設定
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = linearLayoutManager

        // 創建 MyAdapter，傳入刪除的 Callback 函式
        myAdapter = MyAdapter { contact ->
            contacts.remove(contact)
            // 提交新的列表以更新畫面
            myAdapter.submitList(contacts.toList())
        }
        
        binding.recyclerView.adapter = myAdapter

        // 設定按鈕監聽器
        binding.btnAdd.setOnClickListener {
            val i = Intent(this, SecActivity::class.java)
            startForResult.launch(i)
        }
    }
}