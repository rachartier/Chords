package fr.iut.chords.ui.search

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
import fr.iut.chords.databinding.FragmentSearchBinding
import fr.iut.chords.ui.chordinfo.ChordInfoFragment
import fr.iut.chords.ui.results.ResultFragment
import fr.iut.chords.utils.ChordRecyclerViewAdapter
import fr.iut.chords.utils.viewModelFactory
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment(), ChordRecyclerViewAdapter.Callbacks {
    private lateinit var chordsViewModel: ChordsViewModel
    private val chordsListAdapter = ChordRecyclerViewAdapter(this)

    override fun onChordSelected(chordId: String) {
        findNavController().navigate(
            R.id.navigation_notifications, bundleOf(
                ChordInfoFragment.ARG_CHORDNAME to chordId,
                ChordInfoFragment.ARG_ISINFAV to true
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val binding = FragmentSearchBinding.inflate(inflater)

        chordsViewModel = ViewModelProvider(this, viewModelFactory { ChordsViewModel() }).get()
        chordsViewModel.loadAllFromRepo()

        binding.root.button_search.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_dashboard, bundleOf(
                    ResultFragment.ARG_CHORDNAME to binding.root.textinput_chords.text)
            )
        }

        chordsListAdapter.submitList(chordsViewModel.chords!!.value)

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