package com.example.androidstack.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidstack.model.Question
import com.example.androidstack.model.StackRequest

@Dao
interface StackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Question>)


    @Query("SELECT * FROM dbcache WHERE `query` = :query AND `sort` = :sort AND `order` = :order ORDER BY score DESC" )
    fun stackByVotesDesc(query: String, sort: String, order: String): DataSource.Factory<Int, Question>

    @Query("SELECT * FROM dbcache WHERE `query` = :query AND `sort` = :sort AND `order` = :order ORDER BY score ASC" )
    fun stackByVotesAsc(query: String, sort: String, order: String): DataSource.Factory<Int, Question>

    @Query("SELECT * FROM dbcache WHERE `query` = :query AND `sort` = :sort AND `order` = :order ORDER BY creationDate ASC" )
    fun stackByCreationAsc(query: String, sort: String, order: String): DataSource.Factory<Int, Question>

    @Query("SELECT * FROM dbcache WHERE `query` = :query AND `sort` = :sort AND `order` = :order ORDER BY creationDate DESC" )
    fun stackByCreationDesc(query: String, sort: String, order: String): DataSource.Factory<Int, Question>

    @Query("SELECT * FROM dbcache WHERE `query` = :query AND `sort` = :sort AND `order` = :order ORDER BY activityDate ASC" )
    fun stackByActivityAsc(query: String, sort: String, order: String): DataSource.Factory<Int, Question>

    @Query("SELECT * FROM dbcache WHERE `query` = :query AND `sort` = :sort AND `order` = :order ORDER BY activityDate DESC" )
    fun stackByActivityDesc(query: String, sort: String, order: String): DataSource.Factory<Int, Question>

    @Query("DELETE FROM dbcache")
    fun clearAll()

    @Query("DELETE FROM dbcache WHERE `query` = :query AND `sort` = :sort AND `order` = :order ")
    fun clearCache(query: String, sort: String, order: String)

}