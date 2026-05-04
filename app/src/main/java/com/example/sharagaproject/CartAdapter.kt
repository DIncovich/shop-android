package com.example.sharagaproject

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    var items: ArrayList<CartItem>,
    val onPlus: (CartItem) -> Unit,
    val onMinus: (CartItem) -> Unit,
    val onDelete: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val image: ImageView = v.findViewById(R.id.cart_item_image)
        val title: TextView = v.findViewById(R.id.cart_item_title)
        val count: TextView = v.findViewById(R.id.cart_item_count)
        val btnPlus: Button = v.findViewById(R.id.btn_plus)
        val btnMinus: Button = v.findViewById(R.id.btn_minus)
        val btnDelete: ImageButton = v.findViewById(R.id.btn_delete_all)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_in_cart, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = items[position]
        holder.title.text = cartItem.item.title
        holder.count.text = cartItem.count.toString()

        val context = holder.itemView.context
        val imageId = context.resources.getIdentifier(
            cartItem.item.image,
            "drawable",
            context.packageName
        )
        holder.image.setImageResource(imageId)

        holder.btnPlus.setOnClickListener { onPlus(cartItem) }
        holder.btnMinus.setOnClickListener { onMinus(cartItem) }
        holder.btnDelete.setOnClickListener { onDelete(cartItem) }
    }

    override fun getItemCount() = items.size
}