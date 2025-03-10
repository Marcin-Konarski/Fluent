package com.example.fluent.data

import javax.inject.Inject

class DataRepositoryImpl @Inject constructor() : DataRepository {
    // Sample data
    private val data = listOf(
        SampleData(1, "Maester", "An order of scholars, healers, and learned men"),
        SampleData(2, "Unsullied", "Unblemished"),
        SampleData(3, "Crapulous", "Hungover after too much alcohol")
    )

    override fun getSampleData(): List<SampleData> = data

    override fun getDetailData(id: Int): SampleData =
        data.find { it.id == id } ?: throw IllegalArgumentException("Word not found")
}