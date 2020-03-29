package fr.iut.chords.api

class UberChordsRepository {
    companion object {
        private var client: IUberChordsAPI = UberChordsClient.webservice
        suspend fun getChords(chords: String) = client.getChords(chords)
    }
}
