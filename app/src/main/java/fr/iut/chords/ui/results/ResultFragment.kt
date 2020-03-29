package fr.iut.chords.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController

import fr.iut.chords.R
import fr.iut.chords.database.viewmodel.ChordsViewModel
import fr.iut.chords.databinding.FragmentResultBinding
import fr.iut.chords.ui.chordinfo.ChordInfoFragment
import fr.iut.chords.utils.ChordRecyclerViewAdapter
import fr.iut.chords.utils.viewModelFactory
import kotlinx.android.synthetic.main.fragment_result.*


class ResultFragment : Fragment(), ChordRecyclerViewAdapter.Callbacks {
    override fun onChordSelected(chordId: String) {
        findNavController().navigate(
            R.id.navigation_notifications, bundleOf(
                ChordInfoFragment.ARG_CHORDNAME to chordId.replace(' ', ',')
            )
        )
    }

    companion object {
        const val ARG_CHORDNAME = "chordname"
    }

    private lateinit var chordsViewModel: ChordsViewModel
    private val chordsListAdapter = ChordRecyclerViewAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true

        val binding = FragmentResultBinding.inflate(inflater)
        val chordId = arguments?.get(ARG_CHORDNAME).toString()

        chordsViewModel = ViewModelProvider(this, viewModelFactory { ChordsViewModel() }).get()

        if (chordId != "null") {
            chordsViewModel.loadFromApi(chordId)
            chordsViewModel.changeChords(chordId)
            chordsListAdapter.submitList(chordsViewModel.chords!!.value)
        }

        binding.chordsViewModel = chordsViewModel
        binding.lifecycleOwner = this

        binding.executePendingBindings()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.adapter = chordsListAdapter
    }
}