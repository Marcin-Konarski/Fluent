package com.example.fluent.di

import android.app.Application
import androidx.room.Room
import com.example.fluent.data.WordDao
import com.example.fluent.data.WordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): WordDatabase {
        return Room.databaseBuilder(app, WordDatabase::class.java, "word_db").build()
    }

    @Provides
    @Singleton
    fun provideWordDao(db: WordDatabase): WordDao {
        return db.dao
    }

}