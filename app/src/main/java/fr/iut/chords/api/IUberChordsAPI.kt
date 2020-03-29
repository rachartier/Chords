package fr.iut.chords.api

import fr.iut.chords.api.dto.Chord
import retrofit2.http.GET
import retrofit2.http.Query

interface IUberChordsAPI {
    companion object {
        val BASE_URL = "https://api.uberchord.com/v1/"
    }

    @GET("chords")
    suspend fun getChords(@Query("nameLike") chords: String): List<Chord>
}