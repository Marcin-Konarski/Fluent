package com.example.fluent.data

interface DataRepository {
    fun getSampleData(): List<SampleData>
    fun getDetailData(id: Int): SampleData
}
