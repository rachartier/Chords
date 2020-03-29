package fr.iut.chords.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import fr.iut.chords.api.dto.Chord

@Dao
interface ChordDao {
    @Query("SELECT * FROM chords")
    fun getAll(): List<Chord>

    @Query("SELECT * FROM chords WHERE chordName = :id")
    fun findById(id: String): Chord?

    @Insert(onConflict = REPLACE)
    fun insert(chord: Chord)

    @Insert
    fun insertAll(vararg chords: Chord)

    @Update(onConflict = REPLACE)
    fun update(chord: Chord)

    @Delete
    fun delete(chord: Chord)
}