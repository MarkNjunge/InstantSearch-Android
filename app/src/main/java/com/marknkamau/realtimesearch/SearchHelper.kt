package com.marknkamau.realtimesearch

import com.algolia.search.saas.Client
import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import timber.log.Timber

object SearchHelper {
    private var index: Index
    private var moshiAdapter: JsonAdapter<AlgoliaResult>

    init {
        val client = Client(BuildConfig.ALGOLIA_APPLICATION_ID, BuildConfig.API_KEY)
        index = client.getIndex("anime")
        index.enableSearchCache()
        moshiAdapter = Moshi.Builder().build().adapter(AlgoliaResult::class.java)
    }

    fun searchSync(title: String): MutableList<DataModel> {
        val result = mutableListOf<DataModel>()
        val content = index.searchSync(Query(title))

        val algoliaResult: AlgoliaResult? = moshiAdapter.fromJson(content.toString())
        algoliaResult?.let {
            algoliaResult.hits.forEach {
                result.add(it)
            }
        }

        return result
    }

    fun searchAsync(title: String) {
        index.searchAsync(Query(title)) { content, algoliaException ->
            if (algoliaException != null) {
                Timber.e(algoliaException)
            } else {
                val result = mutableListOf<DataModel>()
                val algoliaResult: AlgoliaResult? = moshiAdapter.fromJson(content.toString())

                algoliaResult?.let {
                    result.addAll(algoliaResult.hits)
                }
            }
        }
    }
}
