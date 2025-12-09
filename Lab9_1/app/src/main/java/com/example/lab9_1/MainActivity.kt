package com.example.lab9_1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.lab9_1.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    
    // 用於控制比賽狀態
    private var isRacing = false
    
    // 用於儲存協程 Job，以便取消
    private var rabbitJob: Job? = null
    private var turtleJob: Job? = null

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

        binding.btnStart.setOnClickListener {
            startRace()
        }
    }

    private fun startRace() {
        // 初始化狀態
        binding.btnStart.isEnabled = false
        binding.sbRabbit.progress = 0
        binding.sbTurtle.progress = 0
        isRacing = true

        // 啟動兔子協程 (使用 lifecycleScope 自動管理生命週期)
        rabbitJob = lifecycleScope.launch {
            var progress = 0
            val sleepProbability = arrayOf(true, true, false)
            
            // isActive 用來檢查協程是否被取消
            while (isActive && isRacing && progress < 100) {
                delay(100) // 延遲 0.1 秒 (非阻塞)
                
                if (sleepProbability.random()) {
                    delay(300) // 兔子偷懶 0.3 秒
                }
                
                progress += 3
                // 直接更新 UI，因為 lifecycleScope 預設在 Main Dispatcher
                binding.sbRabbit.progress = progress.coerceAtMost(100)

                if (progress >= 100 && isRacing) {
                    endRace("兔子勝利")
                }
            }
        }

        // 啟動烏龜協程
        turtleJob = lifecycleScope.launch {
            var progress = 0
            while (isActive && isRacing && progress < 100) {
                delay(100) // 延遲 0.1 秒
                
                progress += 1
                binding.sbTurtle.progress = progress.coerceAtMost(100)

                if (progress >= 100 && isRacing) {
                    endRace("烏龜勝利")
                }
            }
        }
    }

    private fun endRace(winner: String) {
        if (!isRacing) return // 防止重複呼叫
        
        isRacing = false
        // 取消兩者的跑動
        rabbitJob?.cancel()
        turtleJob?.cancel()
        
        Toast.makeText(this, winner, Toast.LENGTH_SHORT).show()
        binding.btnStart.isEnabled = true
    }
}