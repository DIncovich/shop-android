package com.example.sharagaproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app_db", factory, 7) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, email TEXT, password TEXT)")
        db.execSQL("CREATE TABLE items (id INTEGER PRIMARY KEY AUTOINCREMENT, image TEXT, title TEXT, desc TEXT, item_text TEXT, price INTEGER)")
        db.execSQL("CREATE TABLE cart (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, item_id INTEGER)")
        db.execSQL("CREATE TABLE orders (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, item_id INTEGER, count INTEGER, date TEXT)")
        insertInitialItems(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS items")
        db.execSQL("DROP TABLE IF EXISTS cart")
        db.execSQL("DROP TABLE IF EXISTS orders")
        onCreate(db)
    }

    private fun insertInitialItems(db: SQLiteDatabase) {
        val items = listOf(
            Item(1, "moon", "луна геншн импкт", "бквы плтные", "на месяц", 300),
            Item(2, "box", "140 мегаящиков", "та самая акция", "через 10 минут пропадёт", 179),
            Item(3, "dragon", "авп драгон лор", "настоящий с завода", "не фейк", 9999)

        )
        items.forEach { item ->
            val v = ContentValues()
            v.put("image", item.image); v.put("title", item.title)
            v.put("desc", item.desc); v.put("item_text", item.text); v.put("price", item.price)
            db.insert("items", null, v)
        }
    }

    fun addUser(user: User) {
        val db = this.writableDatabase
        val v = ContentValues().apply {
            put("login", user.login); put("email", user.email); put("password", user.password)
        }
        db.insert("users", null, v); db.close()
    }

    fun checkUserExists(login: String, email: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE login = ? OR email = ?",
            arrayOf(login, email))
        val exists = cursor.count > 0
        cursor.close(); return exists
    }

    fun getUserId(login: String, pass: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE login = ? AND password = ?",
            arrayOf(login, pass))
        var id = -1
        if (cursor.moveToFirst()) id = cursor.getInt(0)
        cursor.close(); return id
    }

    fun addToCart(userId: Int, itemId: Int) {
        val db = this.writableDatabase
        db.insert("cart", null,
            ContentValues().apply { put("user_id", userId); put("item_id", itemId) })
        db.close()
    }

    fun getUserCartGrouped(userId: Int): ArrayList<CartItem> {
        val list = ArrayList<CartItem>()
        val db = this.readableDatabase
        val query = """
            SELECT items.*, COUNT(cart.item_id) FROM items 
            JOIN cart ON items.id = cart.item_id 
            WHERE cart.user_id = ? GROUP BY items.id
        """
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        while (cursor.moveToNext()) {
            val item = Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getInt(5))
            list.add(CartItem(item, cursor.getInt(6)))
        }
        cursor.close(); return list
    }

    fun removeOneFromCart(userId: Int, itemId: Int) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM cart WHERE id IN (SELECT id FROM cart WHERE user_id = ? AND item_id = ? LIMIT 1)", arrayOf(userId, itemId))
        db.close()
    }

    fun clearItemCompletely(userId: Int, itemId: Int) {
        val db = this.writableDatabase
        db.delete("cart", "user_id = ? AND item_id = ?", arrayOf(userId.toString(), itemId.toString()))
        db.close()
    }

    fun getAllItems(): List<Item> {
        val list = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM items", null)
        while(cursor.moveToNext()) {
            list.add(Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getInt(5)))
        }
        cursor.close(); return list
    }

    fun clearCart(userId: Int) {
        val db = this.writableDatabase
        db.delete("cart", "user_id = ?", arrayOf(userId.toString()))
        db.close()
    }

    fun makeOrder(userId: Int) {
        val cartItems = getUserCartGrouped(userId)
        if (cartItems.isEmpty()) return

        val db = this.writableDatabase
        val date = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())

        cartItems.forEach { cartItem ->
            val v = ContentValues()
            v.put("user_id", userId)
            v.put("item_id", cartItem.item.id)
            v.put("count", cartItem.count)
            v.put("date", date)
            db.insert("orders", null, v)
        }
        clearCart(userId)
        db.close()
    }

    fun getUserOrders(userId: Int): ArrayList<Pair<String, List<CartItem>>> {
        val db = this.readableDatabase
        val result = ArrayList<Pair<String, List<CartItem>>>()
        
        val datesCursor = db.rawQuery("SELECT DISTINCT date FROM orders WHERE user_id = ? ORDER BY id DESC", arrayOf(userId.toString()))
        
        while (datesCursor.moveToNext()) {
            val date = datesCursor.getString(0)
            val items = ArrayList<CartItem>()
            
            val itemsQuery = """
                SELECT items.*, orders.count FROM items 
                JOIN orders ON items.id = orders.item_id 
                WHERE orders.user_id = ? AND orders.date = ?
            """
            val itemCursor = db.rawQuery(itemsQuery, arrayOf(userId.toString(), date))
            while (itemCursor.moveToNext()) {
                val item = Item(itemCursor.getInt(0), itemCursor.getString(1), itemCursor.getString(2),
                    itemCursor.getString(3), itemCursor.getString(4), itemCursor.getInt(5))
                items.add(CartItem(item, itemCursor.getInt(6)))
            }
            itemCursor.close()
            result.add(Pair(date, items))
        }
        datesCursor.close()
        return result
    }
}
