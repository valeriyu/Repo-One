package com.valeriyu.a02_toolbar

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_dynamic.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iniToolBar()
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun iniToolBar() {

        val continerDynamic =
            layoutInflater.inflate(R.layout.layout_dynamic, container, false)
        container.addView(continerDynamic)
        //continerDynamic.visibility = View.VISIBLE

        val searchItem = toolbar.menu.findItem(R.id.search)
        var actionView = (searchItem.actionView as androidx.appcompat.widget.SearchView)

        toolbar.setTitle("   Вот такой Тулбар")

        toolbar.setNavigationOnClickListener {
            actionView.onActionViewCollapsed()
            toast("Это кнопка назад !!!")
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_2 -> {
                    toast("Привет !!!")
                    true
                }

                R.id.exit -> {
                    toast("Выход")
                    finish()
                    true
                }

                R.id.search -> {
                    true
                }
                else -> false
            }
        }

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                continerDynamic.visibility = View.VISIBLE
                continerDynamic.apply {
                    textView2.text = ""
                    textView1.text = ""
                }
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                continerDynamic.visibility = View.GONE
                continerDynamic.apply {
                    textView2.text = ""
                    textView1.text = ""
                }
                return true
            }
        })

        actionView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                continerDynamic.apply {
                    textView2.text = query ?: ""
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                continerDynamic.apply {
                    textView1.text = newText ?: ""
                }
                return true
            }
        })
    }
}




