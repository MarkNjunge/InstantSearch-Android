package com.marknkamau.realtimesearch

import com.squareup.moshi.Json

data class AlgoliaResult(val hits: MutableList<DataModel>){
    override fun toString(): String {
        return "AlgoliaResult(hits=$hits)"
    }
}

data class DataModel(val title: String, val averageScore: Int?, val coverImage: String, val objectID: String, @Json(name = "_highlightResult") val highlightResult: HighlightResult){
    override fun toString(): String {
        return "DataModel(title='$title', averageScore=$averageScore, coverImage='$coverImage', objectID='$objectID', highlightResult=$highlightResult)"
    }
}

data class HighlightResult(val title: HighlightResultTitle){
    override fun toString(): String {
        return "HighlightResult(title=$title)"
    }
}

data class HighlightResultTitle(val value: String, val matchLevel: String, val fullyHighlighted: Boolean, val matchedWords: MutableList<String>){
    override fun toString(): String {
        return "HighlightResultTitle(value='$value', matchLevel='$matchLevel', fullyHighlighted=$fullyHighlighted, matchedWords=$matchedWords)"
    }
}
