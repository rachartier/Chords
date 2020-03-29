package fr.iut.chords.api.dto

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "chords")
data class Chord(
    @PrimaryKey
    var chordName: String,

    var strings: String,
    var fingering: String,
    var enharmonicChordName: String,
    var voicingID: String,
    var tones: String,
    var isInFav: Boolean
) {

    @Ignore
    fun printableName() = chordName.replace(',', ' ')

    companion object {
        @Ignore
        fun printableNameToApiName(name: String): String {
            return name.replace(' ', ',')
        }

        val EMPTY = Chord("none", "none", "none", "none", "0", "none", false)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Chord) return false

        return strings == other.strings
                && fingering == other.fingering
                && chordName == other.chordName
                && enharmonicChordName == other.enharmonicChordName
                && voicingID == other.voicingID
                && tones == other.tones
    }
}