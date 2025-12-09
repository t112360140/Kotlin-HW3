package com.example.lab9_2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.lab9_2.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding

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

        binding.btnCalculate.setOnClickListener {
            // 驗證輸入
            if (validateInput()) {
                startCalculation()
            }
        }
    }

    private fun validateInput(): Boolean {
        return when {
            binding.edHeight.text.isEmpty() -> {
                showToast("請輸入身高")
                false
            }
            binding.edWeight.text.isEmpty() -> {
                showToast("請輸入體重")
                false
            }
            binding.edAge.text.isEmpty() -> {
                showToast("請輸入年齡")
                false
            }
            else -> true
        }
    }

    private fun startCalculation() {
        // 重置 UI 顯示
        binding.tvWeightResult.text = "標準體重\n無"
        binding.tvFatResult.text = "體脂肪\n無"
        binding.tvBmiResult.text = "BMI\n無"
        binding.progressBar.progress = 0
        binding.tvProgress.text = "0%"
        
        // 顯示進度條，鎖定按鈕
        binding.llProgress.visibility = View.VISIBLE
        binding.btnCalculate.isEnabled = false

        // 啟動協程執行模擬計算
        lifecycleScope.launch {
            // 模擬進度跑動 (0 到 100)
            for (progress in 1..100) {
                delay(50) // 延遲 50ms，不會卡死 UI
                binding.progressBar.progress = progress
                binding.tvProgress.text = "$progress%"
            }

            // 進度跑完後，開始計算數值
            val height = binding.edHeight.text.toString().toDouble()
            val weight = binding.edWeight.text.toString().toDouble()
            val age = binding.edAge.text.toString().toDouble()
            
            // 計算 BMI
            val bmi = weight / ((height / 100).pow(2))
            
            // 計算男女體脂率 (使用解構宣告)
            val (standWeight, bodyFat) = if (binding.btnBoy.isChecked) {
                Pair((height - 80) * 0.7, 1.39 * bmi + 0.16 * age - 19.34)
            } else {
                Pair((height - 70) * 0.6, 1.39 * bmi + 0.16 * age - 9)
            }

            // 更新結果畫面
            binding.llProgress.visibility = View.GONE
            binding.tvWeightResult.text = "標準體重 \n${String.format("%.2f", standWeight)}"
            binding.tvFatResult.text = "體脂肪 \n${String.format("%.2f", bodyFat)}"
            binding.tvBmiResult.text = "BMI \n${String.format("%.2f", bmi)}"
            
            // 恢復按鈕
            binding.btnCalculate.isEnabled = true
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}