package fr.iut.chords.database.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.iut.chords.api.UberChordsRepository
import fr.iut.chords.api.dto.Chord
import fr.iut.chords.database.ChordDatabase
import fr.iut.chords.database.repo.ChordRepository
import kotlinx.coroutines.runBlocking

class ChordsViewModel : ViewModel() {
    private val chordRepository = ChordRepository(ChordDatabase.getInstance().chordDAO())

    var chords: MutableLiveData<List<Chord>>? = null

    fun loadFromApi(chordId: String) {
        runBlocking {
            chords = MutableLiveData(UberChordsRepository.getChords(chordId))
        }
    }

    fun loadAllFromRepo() {
        runBlocking {
            chords = MutableLiveData(chordRepository.getAll())
        }
    }

    fun changeChords(newChordId: String) {
        if (chords == null) return

        runBlocking {
            chords!!.value = UberChordsRepository.getChords(newChordId)
        }
    }
}
