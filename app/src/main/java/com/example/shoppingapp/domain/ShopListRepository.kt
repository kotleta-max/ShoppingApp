package com.example.shoppingapp.domain

import androidx.lifecycle.LiveData
import com.example.shoppingapp.domain.ShopItem

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(item: ShopItem)

    fun getShopItemFromId(shopItemId: Int): ShopItem

    fun getShopList(): LiveData<List<ShopItem>>


}