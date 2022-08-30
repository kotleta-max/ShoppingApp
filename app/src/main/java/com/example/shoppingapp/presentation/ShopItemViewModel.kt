package com.example.shoppingapp.presentation

import androidx.lifecycle.ViewModel
import com.example.shoppingapp.data.ShopListRepositoryImpl
import com.example.shoppingapp.domain.AddShopItemUseCase
import com.example.shoppingapp.domain.EditShopItemUseCase
import com.example.shoppingapp.domain.GetShopItemFromIdUseCase
import com.example.shoppingapp.domain.ShopItem
import java.lang.Exception

class ShopItemViewModel : ViewModel() {

    //1. Создаем useКейсы которые будем использовать во втором активити и передаем в методы реализацию репозитория.

    private val repository = ShopListRepositoryImpl
    private val getShopItemUseCase = GetShopItemFromIdUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    //2. Добавляем методы, которые будут работать с юскейсами.
    fun getShopItem(shopItemId: Int {
        val item = getShopItemUseCase.getShopItemFromId(shopItemId)

    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid){
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid){
            val shopItem = ShopItem(name, count, true)
            editShopItemUseCase.editShopItem(shopItem)
        }
    }

    //Создаем метод который обрезает пробелы, переданные юзером в инпутнэйм
    private fun parseName(inputName: String?): String {
        return inputName?.trim()
            ?: "" // если inputname не null обрезать пробелы, елсе вернуть пустую строку.
    }

    //Cоздаем метод ,который преобразует нуллабельную строку в число.
    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0

        } catch (e: Exception) {
            0
        }
    }

    //Создаем метод ,который проверяет введенные поля на корректность, т.е. проводит валидацию
    private fun validateInput(name: String, count: Int): Boolean {
        val result = true
        if (name.isBlank()) {
            // TODO - show error inputName
            result = false
        }
        if (count <= 0) {
            // TODO - show error inputCount
            result = false
        }
        return result
    }

}