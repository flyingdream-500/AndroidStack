package com.example.androidstack

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import com.example.androidstack.di.Injection
import com.example.androidstack.model.Question
import com.example.androidstack.model.StackRequest
import com.example.androidstack.ui.StackAdapter
import com.example.androidstack.util.*
import com.example.androidstack.viewmodel.StackViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {

    //Preferences
    private lateinit var appPreferences: SharedPreferences

    //Popup sort menu
    private lateinit var popupMenu: PopupMenu

    private lateinit var viewModel: StackViewModel
    private val adapter = StackAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
            .get(StackViewModel::class.java)


        initAdapter()

        appPreferences = getSharedPreferences(PREFFERENCES_FILE, MODE_PRIVATE)

        initSortMenu()
        loadPreferences()

        initSwipeToRefresh()

        initSearch()

    }

    private fun loadPreferences() {

        val sortID = appPreferences.getSortID()
        val order = appPreferences.getOrder()

        popupMenu.menu.findItem(sortID).isChecked = true
        popupMenu.menu.findItem(R.id.sort_desc).isChecked = order

        val search = appPreferences.getQuery()
        val sort = appPreferences.getSortName()
        val orderName = appPreferences.getOrderName()

        et_search.setText(search)

        viewModel.searchRepo(StackRequest(search, sort, orderName))

    }

    private fun initSortMenu() {
        val wrapper = ContextThemeWrapper(this, R.style.SortPopupStyle)
        popupMenu = PopupMenu(wrapper,iv_main_sort)
        popupMenu.inflate(R.menu.popup_sort)

        iv_main_sort.setOnClickListener {
            popupSortClickListener()
            popupMenu.show()

        }
    }

    private fun popupSortClickListener() {

        popupMenu.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.sort_activity ->  {
                    saveSortItem(item, "activity")
                    true
                }
                R.id.sort_creation ->  {
                    saveSortItem(item, "creation")
                    true
                }
                R.id.sort_votes ->  {
                    saveSortItem(item, "votes")
                    true
                }
                R.id.sort_desc ->  {
                    item.isChecked = !item.isChecked
                    if (item.isChecked) {
                        saveOrderItem(true,"asc")
                    } else {
                        saveOrderItem(false,"desc")
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun saveSortItem(item: MenuItem, value: String) {
        item.isChecked = true
        //updateRepoStackList(StackRequest(getCurrentQuery(), value, appPreferences.getOrderName()))
        appPreferences.saveSortPreference(item.itemId)
        appPreferences.saveSortNamePreference(value)
        updateRepoListFromInput()

    }

    private fun saveOrderItem(checked: Boolean, value: String) {
        //updateRepoStackList(StackRequest(getCurrentQuery(), appPreferences.getSortName(), value))
        appPreferences.saveOrderPreference(checked)
        appPreferences.saveOrderNamePreference(value)
        updateRepoListFromInput()

    }

    private fun getCurrentQuery(): String {
        return et_search.text.toString()
    }

    private fun updateRepoStackList(search: StackRequest) {
        if (viewModel.searchRepo(search)) {
            rv_questions.scrollToPosition(0)
            Log.d("TTT", "scroll to 0")
            (rv_questions.adapter as? StackAdapter)?.submitList(null)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        currentFocus?.clearFocus()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        appPreferences.saveQueryPreference(getCurrentQuery())
    }

    private fun initSwipeToRefresh() {
        swipe_refresh_layout.setOnRefreshListener {
            viewModel.refresh()
            swipe_refresh_layout.isRefreshing = false
        }
    }

    private fun initAdapter() {
        rv_questions.adapter = adapter
        viewModel.repos.observe(this, Observer<PagedList<Question>> {
            Log.d("TAGG", "repos list: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun initSearch() {

        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        et_search.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateRepoListFromInput() {
        et_search.text.trim().let {
            if (it.isNotEmpty()) {
                updateRepoStackList(StackRequest(it.toString(),appPreferences.getSortName(), appPreferences.getOrderName()))
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            rv_questions.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            rv_questions.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val PREFFERENCES_FILE = "uniquePreference"
    }
}