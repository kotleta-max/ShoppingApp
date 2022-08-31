package com.example.shoppingapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    // приватная Переменная _errorInputName содержит мутабельную лайвдату, чтобы менять ее значения из зис вьюмодели
    private val _errorInputName = MutableLiveData<Boolean>()

    // паблик переменная errorInputname типа LiveData создается, чтобы обращаться к ней из активити и мочь оттуда поменять значение.
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    //2. Добавляем методы, которые будут работать с юскейсами.
    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItemFromId(shopItemId)
        _shopItem.value = item

    }

    //Создаем лайвдату, чтобы класть в нее значения при сохранении нового шопитема
    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    // создаем еще одну лайвдату, которая будет оповещать активити о том что можно закрыть окошечки с добавлением файла
    private val _closeScreen = MutableLiveData<Boolean>()
    val closeScreen: LiveData<Boolean>
        get() = _closeScreen

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            closeWindow()
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            /*val shopItem = ShopItem(name, count, true) === здесь ошибка, т.к. элементу будет присвоен айди -1, и будет всегда енаблед, код ниже, в котором мы берем элемент из лайв даты решает проблему
            val shopItem = _shopItem.value === значение в этом объекте может не лежать, поэтому его нельзя передать в editShopItem, для этого ниже код где используются let, который будет обрабатывать только в том случае если значение не равно null
            editShopItemUseCase.editShopItem(shopItem)
            closeWindow()*/
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                val shopItem = _shopItem.value
                editShopItemUseCase.editShopItem(item)
                closeWindow()
            }
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
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    //Добавим метод сброса ошибки ввода имени, который позволит убрать ошибку, если пользователь продолжает вводить в поле данные
    public fun resetErrorInputName() {
        _errorInputName.value = false
    }

    public fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    //Чтобы не дублировать код, создаем метод по закрытию окошка
    private fun closeWindow() {
        _closeScreen.value = true
    }


}