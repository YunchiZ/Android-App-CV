package com.team13.whatsmissing.data.network.model

import java.util.*

data class AssociationsApiResponse(
    val associations_array: ArrayList<String>,
    val associations_scored: Hashtable<String, Float>
) {

    fun getAssociationsSortedByScore(): List<String> {
        return associations_scored.toList()
            .sortedByDescending { it.second }
            .map { it.first }
    }

}
