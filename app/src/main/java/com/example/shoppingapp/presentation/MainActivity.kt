package com.example.shoppingapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this){
            shopListAdapter.submitList(it)
        }
        val buttonAdditem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        buttonAdditem.setOnClickListener {
            /* val intent = Intent(this, ShopItemActivity::class.java)
            //далее в качестве параметра передаем режим работы экрана
            intent.putExtra("extra_mode", "mode_add")
            //запускаем активити
            startActivity(intent) */
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }

    }



    private fun setupRecyclerView(){
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list) //создаем ссылку на рекуклер

        with (rvShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            rvShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            rvShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        } // вставляем в него адаптер и настраиваем его

        onShopItemLongClickListener()

        onShopItemClickListener()

        onSwipeListener(rvShopList)

    }

    private fun onSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun onShopItemLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.editShopItem(it)
        }
    }

    private fun onShopItemClickListener() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("MainActivity", it.toString())
            /* val intent = Intent(this, ShopItemActivity::class.java)
            //далее в качестве параметра передаем режим работы экрана
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            //запускаем активити
            startActivity(intent) */
            //Передаем переделанный вариант, в котором видно какие параметры нужно обязательно передать
            val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

}
