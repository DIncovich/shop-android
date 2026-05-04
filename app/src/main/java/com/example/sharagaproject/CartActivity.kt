package com.example.sharagaproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var cartList: RecyclerView
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        db = DBHelper(this, null)

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("USER_ID", -1)

        cartList = findViewById(R.id.cartList)
        cartList.layoutManager = LinearLayoutManager(this)

        val linkToList: TextView = findViewById(R.id.link_to_list)
        linkToList.setOnClickListener {
            val intent = Intent(this, ItemsActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnMakeOrder: Button = findViewById(R.id.btn_make_order)
        btnMakeOrder.setOnClickListener {
            db.makeOrder(userId)
            updateCart()
            android.widget.Toast.makeText(this, "Заказ оформлен!", android.widget.Toast.LENGTH_SHORT).show()
        }

        val btnHistory: Button = findViewById(R.id.btn_history)
        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        updateCart()
    }

    private fun updateCart() {
        if (userId == -1) return

        val items = db.getUserCartGrouped(userId)

        val adapter = CartAdapter(
            items,
            onPlus = { cartItem ->
                db.addToCart(userId, cartItem.item.id)
                updateCart()
            },
            onMinus = { cartItem ->
                db.removeOneFromCart(userId, cartItem.item.id)
                updateCart()
            },
            onDelete = { cartItem ->
                db.clearItemCompletely(userId, cartItem.item.id)
                updateCart()
            }
        )

        cartList.adapter = adapter
    }
}