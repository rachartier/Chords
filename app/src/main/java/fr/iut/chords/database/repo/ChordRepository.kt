package fr.iut.chords.database.repo

import fr.iut.chords.api.dto.Chord
import fr.iut.chords.database.dao.ChordDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChordRepository(private val chordDao: ChordDao) {
    suspend fun insert(chord: Chord) = withContext(Dispatchers.IO) {
        chordDao.insert(chord)
    }

    suspend fun delete(chord: Chord) = withContext(Dispatchers.IO) {
        chordDao.delete(chord)
    }

    suspend fun update(chord: Chord) = withContext(Dispatchers.IO) {
        chordDao.update(chord)
    }

    suspend fun findById(chordId: String) = withContext(Dispatchers.IO) {
        chordDao.findById(chordId)
    }

    suspend fun getAll() = withContext(Dispatchers.IO) {
        chordDao.getAll()
    }
}