package com.example.shoppingapp.domain

class GetShopItemFromIdUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopItemFromId(shopItemId: Int): ShopItem {
        return shopListRepository.getShopItemFromId(shopItemId)
    }
}