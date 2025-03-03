package  com.example.shoppy.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy.R
import com.example.shoppy.model.ShoppingItem

class ShoppingItemAdapter(
    var items: List<ShoppingItem>, // The list is mutable
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder>() {

    // Interface for item click actions
    interface OnItemClickListener {
        fun onEditClick(item: ShoppingItem)
        fun onDeleteClick(item: ShoppingItem)
        fun onFavoriteToggle(item: ShoppingItem, isFavorite: Boolean) // New: to update the favorite status
    }

    // Method to update the list and notify RecyclerView
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

        // Set favorite icon based on item state
        if (item.favorite == true) {
            holder.favoriteIcon.setImageResource(R.drawable.filled) // "filled" icon
        } else {
            holder.favoriteIcon.setImageResource(R.drawable.heart) // "empty" icon
        }

        holder.editIcon.setOnClickListener {
            onItemClickListener.onEditClick(item)
        }

        holder.deleteIcon.setOnClickListener {
            onItemClickListener.onDeleteClick(item)
        }

        // Handle the favorite toggle
        holder.favoriteIcon.setOnClickListener {
            // Safely toggle the favorite status
            item.favorite = item.favorite?.not() ?: false  // If null, set it to true

            // Update the item list with the new state
            val updatedList = items.toMutableList()
            updatedList[position] = item  // Modify the item directly in the list

            // Notify the adapter that the list has been updated
            updateItems(updatedList)

            // Call the listener to update the favorite status in the ViewModel
            onItemClickListener.onFavoriteToggle(item, item.favorite!!)

            // Update the favorite icon based on the new state
            if (item.favorite!!) {
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
        val editIcon: ImageView = itemView.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon) // Favorite icon
    }
}
