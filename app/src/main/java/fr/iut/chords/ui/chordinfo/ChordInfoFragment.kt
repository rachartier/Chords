package fr.iut.chords.ui.chordinfo

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import fr.iut.chords.R
import fr.iut.chords.api.UberChordsClient
import fr.iut.chords.database.viewmodel.ChordViewModel
import fr.iut.chords.databinding.FragmentChordInfoBinding
import fr.iut.chords.utils.viewModelFactory
import fr.iut.chords.utils.zipLiveData
import kotlinx.android.synthetic.main.fragment_chord_info.view.*


class ChordInfoFragment : Fragment() {
    private lateinit var chordViewModel: ChordViewModel

    companion object {
        const val ARG_CHORDNAME = "chordname"
        const val ARG_ISINFAV = "isinfav"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChordInfoBinding.inflate(inflater)

        val chordId = arguments?.get(ARG_CHORDNAME).toString()
        val isInFav = if(arguments?.get(ARG_ISINFAV) == null) false else arguments?.get(ARG_ISINFAV) as Boolean

        chordViewModel = ViewModelProvider(this, viewModelFactory { ChordViewModel() }).get()

        if (chordId != "null") {
            chordViewModel.loadFromApi(chordId, isInFav)
        }

        if ((chordViewModel.nameLiveData.value == null || chordViewModel.nameLiveData.value == "none") && chordId == "null") {
            chordViewModel.nameLiveData.value = getString(R.string.text_no_chord_selected)

            binding.buttonFav.visibility = View.GONE
            binding.chordVM = chordViewModel
            binding.lifecycleOwner = this

            return binding.root
        }

        binding.chordVM = chordViewModel
        binding.lifecycleOwner = this

        binding.buttonFav.setOnClickListener {
            binding.chordVM!!.let {
                it.isInFavLiveData.value = !it.isInFavLiveData.value!!

                if (it.isInFavLiveData.value == true) {
                    Toast.makeText(
                        context,
                        """${it.printableName()} ${getString(R.string.text_added_to_fav)}""",

                        Toast.LENGTH_SHORT
                    )
                        .show()

                    it.save()
                    binding.buttonFav.setImageResource(R.drawable.ic_favorite_black_24dp)
                } else {
                    Toast.makeText(
                        context,
                        """${it.printableName()} ${getString(R.string.text_retired_from_fav)}""",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    it.delete()
                    binding.buttonFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                }
            }
        }

        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.builtInZoomControls = true
        binding.webview.settings.setSupportZoom(true)

        binding.root.webview.loadUrl(UberChordsClient.EMBEDDED_URL + chordViewModel.chordId/*, "text/html", "UTF-8", ""*/)
        binding.executePendingBindings()

        zipLiveData(
            chordViewModel.stringsLiveData,
            chordViewModel.fingeringLiveData,
            chordViewModel.tonesLiveData
        ).observeForever {
            if (chordViewModel.stringsLiveData.value != null
                && chordViewModel.fingeringLiveData.value != null
                && chordViewModel.tonesLiveData.value != null
            ) {
                initGridLayout(binding.root)
            }
        }

        return binding.root
    }

    private fun initGridLayout(view: View) {
        val names = arrayOf(
            getString(R.string.text_tuning), getString(R.string.text_strings), getString(
                R.string.text_fingering
            )
        )

        val parsedStrings = chordViewModel.stringsLiveData.value!!.split(" ")
        val parsedFingering = chordViewModel.fingeringLiveData.value!!.split(" ")

        val values = arrayOf(listOf("E", "A", "D", "G", "B", "E"), parsedStrings, parsedFingering)

        view.gridlayout.removeAllViews()

        view.gridlayout.columnCount = parsedStrings.size + 1
        view.gridlayout.rowCount = values.count()

        for (y in 0 until view.gridlayout.rowCount) {
            addViewGrid(view.gridlayout, names[y], true)

            for (note in values[y]) {
                addViewGrid(view.gridlayout, note, false)
            }
        }
    }

    private fun addViewGrid(gridlayout: GridLayout, content: String, isTitle: Boolean) {
        val infoView = TextView(context).apply {
            text = content
            textSize = 16.0f
        }

        val layoutParams: GridLayout.LayoutParams = GridLayout.LayoutParams(
            GridLayout.spec(GridLayout.UNDEFINED, 1f),
            GridLayout.spec(GridLayout.UNDEFINED, 1f)
        ).apply {
            width = 0
            height = 0
        }

        if (isTitle) {
            infoView.setTypeface(null, Typeface.BOLD)
            layoutParams.width = 80
        }

        gridlayout.addView(infoView, layoutParams)
    }
}