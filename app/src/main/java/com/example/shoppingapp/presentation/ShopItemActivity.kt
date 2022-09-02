package com.example.shoppingapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity() {

    //Добавляем ссылки на вью и вьюмодель
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private lateinit var viewModel: ShopItemViewModel

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID


    //При клике на кнопку добавить, открывается активити в режиме добавление итема
    //При клике на элемент списка, открывается активити в режиме редактирования итема


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item) //устанавливаем контент
        parseIntent() // (получаем все данные из интента) если данный метод вызывался успешно, то у будут проинициализированы поля screenMode и shopItemId и в зависимости от этих значений правильно инициализируется экран
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java] //инициализируем вьюмодель
        initViews() //инициализируем все вью
        addTextChangeListener() //добавляем слушатели ввода текста
        launchRightMode() //запускаем правильный режим экрана
        observeViewModel() // и подписываемся на все объекты во вьюмодели
    }

    //вынесем в отдельный метод, подписку на объекты из вьюмодели
    private fun observeViewModel() {
        //подписываемся на объекты ошибки ввода имени и числа
        viewModel.errorInputCount.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = message
        }

        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = message
        }

        //закрываем экран
        viewModel.closeScreen.observe(this) {
            finish()
        }
    }


    //также вынесем в отдельный метод правильный режим отображения
    private fun launchRightMode() {
        //после того как все данные получены, настраиваем экран
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    //создадим отдельный метод слушателей, чтобы почистить
    private fun addTextChangeListener() {
        //Код ниже отвечает, за скрытие ошибки при вводе текса и каунта
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //когда текст был изменен, то у вьюмодели вызываем метод который скрывает ошибку
                viewModel.resetErrorInputName()


            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //когда текст был изменен, то у вьюмодели вызываем метод который скрывает ошибку
                viewModel.resetErrorInputCount()


            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }


    //создаем методы которые запускают экран в режиме редактирования или добавления
    private fun launchEditMode() {
        //если находимся в режиме редактирования, то сперва надо получить элемент по его id
        viewModel.getShopItem(shopItemId)
        //далее подписываемся на данный элемент
        viewModel.shopItem.observe(this) {
            //когда объект будет загружен, то мы установим значения из этого айтема в поля ввода
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        //при клике на кнопку сейв, устанав. слуш. клика и передаем значения вьюмодели
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }

    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }

    }

    //Устанавливаем правильный режим работы, в зависимости от переданных параметров в интенте
    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) { //если интент не содержит параметра EXTRA_SCREEN_MODE то бросим исключение
            throw RuntimeException("Param sreen mode is absent")
        }
        //далее если параметр, который мы передали не равен ни одному из доступных режимов, бросаем исключение, что скрин мод неизвестный
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen $mode")
        }
        //если мы дошли до сюда значит все условия выполнены успешно и кладем это значение в переменную
        screenMode = mode
        //далее проверяем скрин мод на значение редактирование и содержит ли интент EXTRA_SCREEN_MODE
        if (screenMode == MODE_EDIT && !intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param shopItemId is absent")
        }
        //теперь если скрин мод равен MODE_EDIT и параметр был передан, то получаем значение по этому параметру
        if (screenMode == MODE_EDIT) {
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    //Создадим метод для инициализации вью и передаем его в onCreate
    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        tilCount = findViewById(R.id.til_count)
        etName = findViewById(R.id.et_name)
        etCount = findViewById(R.id.et_count)
        buttonSave = findViewById(R.id.save_button)
    }

    /*Создаем константы, чтобы не писать текст и не ошибиться в написании их
    companion object {
        const val EXTRA_SCREEN_MODE = "extra_mode"
        const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        const val MODE_EDIT = "mode_edit"
        const val MODE_ADD = "mode_add"

    }*/

    //Константы не должны быть публичные, переделываем код сверху на приватные с публичными методами

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        //Теперь будет понятно, что shopItemId обязательный параметр определенного типа, которые нужно будет передать, если нужно запустить экран в режиме редактирования
        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }

    }

}