package fr.iut.chords.api

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class ApiREST {
    companion object {
        private var baseUrl = "https://api.uberchord.com/v1/"

        private suspend fun get(endUrl: String, param: String): JSONArray {
            var url = URL(baseUrl + endUrl + "/" + param)
            var stringBuilder = StringBuilder()

            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                inputStream.bufferedReader().use {
                    it.lineSequence().forEach { line ->
                        stringBuilder.append(line)
                    }
                }
            }

            return JSONArray(stringBuilder.toString())
        }

        suspend fun getChords(chordsName: String) = GlobalScope.async {
            get("chords", chordsName)
        }
    }
}