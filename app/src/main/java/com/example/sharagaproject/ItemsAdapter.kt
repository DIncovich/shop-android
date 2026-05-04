package com.example.sharagaproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(
    var items: List<Item>,
    var context: Context,
    val onBuyClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.desc.text = item.desc
        holder.price.text = "${item.price} ₽"

        val imageId = context.resources.getIdentifier(
            item.image,
            "drawable",
            context.packageName
        )
        holder.image.setImageResource(imageId)

        holder.button.setOnClickListener {
            onBuyClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.imageView)
        val title: TextView = view.findViewById(R.id.item_title)
        val desc: TextView = view.findViewById(R.id.item_desc)
        val price: TextView = view.findViewById(R.id.item_price)
        val button: Button = view.findViewById(R.id.button2)
    }
}