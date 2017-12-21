package com.marknkamau.realtimesearch

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val dataAdapter by lazy { DataAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvAnime.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvAnime.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        rvAnime.adapter = dataAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "DataModel title"
        searchView.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        searchView.setIconifiedByDefault(false)

        RxSearch.fromSearchView(searchView)
                // Set the minimum length searchable
                .filter {
                    it.length > 3
                }
                // The amount of time before it starts to search after a user stops typing
                .debounce(300, TimeUnit.MILLISECONDS)
                .map {
                    runOnUiThread {
                        pbLoading.visibility = View.VISIBLE
                    }

                    // Perform the actual search
                    SearchHelper.searchSync(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            pbLoading.visibility = View.GONE
                            dataAdapter.setItems(result)
                        },
                        { throwable ->
                            Timber.e(throwable)
                            Toast.makeText(this, throwable.message, Toast.LENGTH_SHORT).show()
                        })

        return true
    }

    // From https://gist.github.com/matdziu/8562c57fce16f4dc910655ecc6b394e5#file-rxsearch-java
    // RxBinding didn't work for the search view.
    // If you are not using a SearchView, use RxBinding instead
    object RxSearch {
        fun fromSearchView(searchView: SearchView): Observable<String> {
            BehaviorSubject.create<String>()
            val subject = BehaviorSubject.create<String>()

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    subject.onComplete()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (!newText.isEmpty()) {
                        subject.onNext(newText)
                    }
                    return true
                }
            })

            return subject
        }
    }


}
