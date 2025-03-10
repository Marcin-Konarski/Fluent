package com.example.fluent.data

import javax.inject.Inject

class DataRepositoryImpl @Inject constructor() : DataRepository {
    // Sample data
    private val data = listOf(
        SampleData(1, "Word 1", "Translation 1"),
        SampleData(2, "Word 2", "Translation 2"),
        SampleData(3, "Word 3", "Translation 3")
    )

    override fun getSampleData(): List<SampleData> = data

    override fun getDetailData(id: Int): SampleData =
        data.find { it.id == id } ?: throw IllegalArgumentException("Word not found")
}