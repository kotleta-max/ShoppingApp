package com.example.shoppingapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.ShopItem

class ShopListAdapter: ListAdapter<ShopItem, ShopItemViewHolder> (ShopItemDiffCallback()) {

    //var count = 0

   /* var shopList = listOf<ShopItem>()                              После того как наследовались от ListAdapter, не нужно
    set(value) {
        val callback = ShopListDiffCallback(shopList, value)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        field = value
    }*/

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        val layout = when (viewType){
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        //Log.d("ShopListAdapter", "OnBindViewHolder, count: ${count++}" )
        //val shopItem = shopList[position]  После наследования от ListAdapter, тот же способ приведен ниже
        val shopItem = getItem(position)

        viewHolder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }

        viewHolder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
            true
        }

        viewHolder.tvName.text = shopItem.name
        viewHolder.tvCount.text = shopItem.count.toString()

    }


    /* override fun onViewRecycled(viewHolder: ShopItemViewHolder) {
               super.onViewRecycled(viewHolder)
               viewHolder.tvName.text = ""
               viewHolder.tvCount.text = ""
           }
       */


    override fun getItemViewType(position: Int): Int {
        //val item = shopList[position]
        val item = getItem(position)
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

   /* Этот метод больше можно не переопределять, так как эта логика происходит, внутри самого ListAdapter и скрыта от нас

    override fun getItemCount(): Int {
        return shopList.size
    }

    */

    companion object {
        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101

        const val MAX_POOL_SIZE = 15
    }
}