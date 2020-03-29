package fr.iut.chords.database.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.iut.chords.api.UberChordsRepository
import fr.iut.chords.api.dto.Chord
import fr.iut.chords.database.ChordDatabase
import fr.iut.chords.database.repo.ChordRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChordViewModel : ViewModel() {
    private val chordRepository = ChordRepository(ChordDatabase.getInstance().chordDAO())

    val chord: MutableLiveData<Chord> = MutableLiveData(Chord.EMPTY)
    var chordId: String = Chord.EMPTY.chordName

    fun loadFromApi(chordId: String, isInFav: Boolean = false) {
        this.chordId = chordId

        runBlocking {
            val chords = UberChordsRepository.getChords(chordId)
            val first = if (chords.isEmpty()) Chord.EMPTY else chords.first()


            chord.value = first

            chord.value!!.isInFav = isInFav
            isInFavLiveData.value = isInFav
        }
    }

    fun printableName(): String {
        return chord.value!!.printableName()
    }

    fun printableNameWithTones(): String {
        return chord.value!!.printableName() + "(" + chord.value!!.tones + ")"
    }


    val isInFavLiveData = MediatorLiveData<Boolean>()

    init {
        isInFavLiveData.addSource(chord) {
            isInFavLiveData.postValue(it?.isInFav)
        }
        isInFavLiveData.observeForever { newIsInFav ->
            chord.value?.let {
                it.isInFav = newIsInFav
            }
        }
    }

    val nameLiveData = MediatorLiveData<String>()

    init {
        nameLiveData.addSource(chord) {
            nameLiveData.postValue(it?.chordName)
        }
        nameLiveData.observeForever { newName ->
            chord.value?.let {
                it.chordName = newName
            }
        }
    }

    val stringsLiveData = MediatorLiveData<String>()

    init {
        stringsLiveData.addSource(chord) {
            stringsLiveData.postValue(it?.strings)
        }
        stringsLiveData.observeForever { newStrings ->
            chord.value?.let {
                it.strings = newStrings
            }
        }
    }


    val fingeringLiveData = MediatorLiveData<String>()

    init {
        fingeringLiveData.addSource(chord) {
            fingeringLiveData.postValue(it?.fingering)
        }
        fingeringLiveData.observeForever { newFingering ->
            chord.value?.let {
                it.fingering = newFingering
            }
        }
    }

    val tonesLiveData = MediatorLiveData<String>()

    init {
        tonesLiveData.addSource(chord) {
            tonesLiveData.postValue(it?.tones)
        }
        tonesLiveData.observeForever { newTones ->
            chord.value?.let {
                it.tones = newTones
            }
        }
    }

    fun save() {
        chord.value?.let {
            viewModelScope.launch {
                if (chordRepository.findById(it.chordName) == null) {
                    chordRepository.insert(it)
                } else {
                    chordRepository.update(it)
                }
            }
        }
    }

    fun delete() {
        chord.value?.let {
            viewModelScope.launch {
                chordRepository.delete(it)
            }
        }
    }
}