package fr.iut.chords.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.iut.chords.api.dto.Chord
import fr.iut.chords.databinding.ItemListChordBinding

class ChordRecyclerViewAdapter(private val listener: Callbacks) :
    ListAdapter<Chord, ChordRecyclerViewAdapter.ChordViewHolder>(ChordDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChordViewHolder(ItemListChordBinding.inflate(LayoutInflater.from(parent.context)), listener)

    override fun onBindViewHolder(holder: ChordViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ChordViewHolder(private val binding: ItemListChordBinding, listener: Callbacks) :
        RecyclerView.ViewHolder(binding.root) {
        val chord: Chord? get() = binding.chord

        init {
            itemView.setOnClickListener {
                chord?.let {
                    listener.onChordSelected(it.chordName)
                }
            }
        }

        fun bind(chord: Chord) {
            binding.chord = chord
            binding.executePendingBindings()
        }
    }

    interface Callbacks {
        fun onChordSelected(chordId: String)
    }

    private object ChordDiffCallback : DiffUtil.ItemCallback<Chord>() {
        override fun areItemsTheSame(oldItem: Chord, newItem: Chord) =
            oldItem.chordName == newItem.chordName

        override fun areContentsTheSame(oldItem: Chord, newItem: Chord) = oldItem == newItem
    }
}