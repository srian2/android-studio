package com.example.shoppy.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy.R
import com.example.shoppy.model.ShoppingItem

class favoriteAdapter(
    private var items: List<ShoppingItem>, // List of favorite items
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<favoriteAdapter.ViewHolder>() {

    // Interface for item click actions
    interface OnItemClickListener {
        fun onFavoriteToggle(item: ShoppingItem, isFavorite: Boolean) // Toggle favorite status
    }

    // Method to update the list of items
    fun updateItems(newItems: List<ShoppingItem>) {
        this.items = newItems
        notifyDataSetChanged() // Refresh the entire list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item_shoppinglist layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shoppinglist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemName.text = item.name
        holder.itemQuantity.text = item.quantity.toString()
        holder.itemPrice.text = item.price.toString()

        // Set the favorite icon based on item state
        if (item.favorite == true) {
            holder.favoriteIcon.setImageResource(R.drawable.filled) // Filled heart
        } else {
            holder.favoriteIcon.setImageResource(R.drawable.heart) // Empty heart
        }

        // Toggle favorite status when clicking the icon
        holder.favoriteIcon.setOnClickListener {
            item.favorite = !(item.favorite ?: false)  // Toggle favorite status
            updateItems(items)  // Refresh the adapter list with updated favorite status

            // Call the listener to notify the ViewModel to update the favorite status
            onItemClickListener.onFavoriteToggle(item, item.favorite ?: false)

            // Update the icon based on new favorite status
            if (item.favorite == true) {
                holder.favoriteIcon.setImageResource(R.drawable.filled)  // Set filled heart icon
            } else {
                holder.favoriteIcon.setImageResource(R.drawable.heart)  // Set empty heart icon
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // ViewHolder class to hold references to each view in the item layout
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemQuantity: TextView = itemView.findViewById(R.id.itemQuantity)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon) // Favorite icon
    }
}
