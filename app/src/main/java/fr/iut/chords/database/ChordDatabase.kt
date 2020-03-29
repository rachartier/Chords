package fr.iut.chords.database

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.iut.chords.api.dto.Chord
import fr.iut.chords.database.dao.ChordDao

private const val CHORD_DB_FILENAME = "chord_database.db"

@Database(entities = [Chord::class], version = 1)
abstract class ChordDatabase : RoomDatabase() {
    abstract fun chordDAO(): ChordDao

    companion object {
        @Volatile
        private var instance: ChordDatabase? = null
        private var application: AppCompatActivity? = null

        fun getInstance(): ChordDatabase {
            if (application != null) {
                if (instance == null)
                    synchronized(this) {
                        if (instance == null)
                            instance = Room.databaseBuilder(
                                application!!.applicationContext,
                                ChordDatabase::class.java,
                                CHORD_DB_FILENAME
                            )
                                .build()
                    }
                return instance!!
            } else
                throw RuntimeException("Database must be initialized.")
        }


        @Synchronized
        fun initialize(app: AppCompatActivity) {
            if (application == null)
                application = app
            else
                throw RuntimeException("Database must not be initialized twice.")
        }
    }
}

