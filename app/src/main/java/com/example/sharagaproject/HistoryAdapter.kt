package com.example.sharagaproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val orders: List<Pair<String, List<CartItem>>>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val date: TextView = v.findViewById(R.id.order_date)
        val items: TextView = v.findViewById(R.id.order_items_text)
        val total: TextView = v.findViewById(R.id.order_total_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order_group, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (date, items) = orders[position]
        holder.date.text = date
        
        val itemsDescription = items.joinToString("\n") { 
            "${it.item.title} x${it.count} — ${it.item.price * it.count} ₽" 
        }
        holder.items.text = itemsDescription
        
        val totalPrice = items.sumOf { it.item.price * it.count }
        holder.total.text = "Итого: $totalPrice ₽"
    }

    override fun getItemCount() = orders.size
}