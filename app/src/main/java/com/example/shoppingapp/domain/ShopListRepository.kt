package com.example.shoppingapp.domain

import com.example.shoppingapp.domain.ShopItem

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(item: ShopItem)

    fun getShopItemFromId(shopItemId: Int): ShopItem

    fun getShopList(): List<ShopItem>


}