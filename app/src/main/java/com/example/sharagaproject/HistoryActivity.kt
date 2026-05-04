package com.example.sharagaproject

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val db = DBHelper(this, null)
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        val historyList: RecyclerView = findViewById(R.id.historyList)
        historyList.layoutManager = LinearLayoutManager(this)

        if (userId != -1) {
            val orders = db.getUserOrders(userId)
            historyList.adapter = HistoryAdapter(orders)
        }

        val btnBack: Button = findViewById(R.id.btn_back_to_cart)
        btnBack.setOnClickListener {
            finish()
        }
    }
}