package com.example.shoppingapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.data.ShopListRepositoryImpl
import com.example.shoppingapp.domain.DeleteShopItemUseCase
import com.example.shoppingapp.domain.EditShopItemUseCase
import com.example.shoppingapp.domain.GetShopListUseCase
import com.example.shoppingapp.domain.ShopItem

class MainViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl


    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()


    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)

    }

    fun editShopItem(shopItem: ShopItem){
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)

    }

}