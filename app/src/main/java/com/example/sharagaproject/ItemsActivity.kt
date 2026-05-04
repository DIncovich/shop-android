package com.example.sharagaproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_list)

        val db = DBHelper(this, null)
        val items = db.getAllItems()

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        val linkToCart: Button = findViewById(R.id.link_to_cart)

        linkToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        val itemsList: RecyclerView = findViewById(R.id.itemList)
        itemsList.layoutManager = LinearLayoutManager(this)

        itemsList.adapter = ItemsAdapter(items, this) { selectedItem ->
            if (userId != -1) {
                db.addToCart(userId, selectedItem.id)
                Toast.makeText(this, "Товар '${selectedItem.title}' добавлен в корзину!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ошибка: вы не авторизованы!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}