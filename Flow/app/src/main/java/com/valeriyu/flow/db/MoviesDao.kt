package com.valeriyu.flow.db

import androidx.room.*
import com.valeriyu.flow.models.Movies
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Update(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun updateMovies(list: List<Movies>)
    suspend fun updateMovies(m: Movies)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(list: List<Movies>)

    @Query("select * from movies")
    fun observeMovies(): Flow<List<Movies>>

    @Query(
        """
       select  * from movies where imdbID in (:list)
        """
    )
    suspend fun selectMoviesBy_imdbID(list: List<String>): List<Movies>?

    @Query(
        """
       select  * from movies 
        """
    )
    suspend fun getMoviesList(): List<Movies>?

/*

    @Query("""
       select imdbID from movies where imdbID in (:list)
        """)
    suspend fun geTtestList (list: List<String>): List<Movies>?*/


    @Query("select * from movies where title like :title and type in (:types)")
    suspend fun findMovies(title: String, types: List<String>): List<Movies>?
}