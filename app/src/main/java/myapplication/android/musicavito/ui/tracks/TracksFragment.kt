package myapplication.android.musicavito.ui.tracks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.launch
import myapplication.android.core_mvi.LceState
import myapplication.android.core_mvi.MviBaseFragment
import myapplication.android.core_mvi.MviStore
import myapplication.android.musicavito.R
import myapplication.android.musicavito.databinding.FragmentTracksBinding
import myapplication.android.musicavito.di.DaggerAppComponent
import myapplication.android.musicavito.ui.main.MainActivity
import myapplication.android.musicavito.ui.mapper.toRecyclerItems
import myapplication.android.musicavito.ui.model.TrackUi
import myapplication.android.musicavito.ui.model.TracksUiList
import myapplication.android.musicavito.ui.tracks.di.DaggerTracksComponent
import myapplication.android.musicavito.ui.tracks.mvi.TracksEffect
import myapplication.android.musicavito.ui.tracks.mvi.TracksIntent
import myapplication.android.musicavito.ui.tracks.mvi.TracksLocalDi
import myapplication.android.musicavito.ui.tracks.mvi.TracksPartialState
import myapplication.android.musicavito.ui.tracks.mvi.TracksState
import myapplication.android.musicavito.ui.tracks.mvi.TracksStoreFactory
import javax.inject.Inject

class TracksFragment : MviBaseFragment<
        TracksPartialState,
        TracksIntent,
        TracksState,
        TracksEffect>(R.layout.fragment_tracks) {

    @Inject
    lateinit var localDi: TracksLocalDi

    private var _binding: FragmentTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TracksViewModel by viewModels()
    private var trackItems: List<TrackUi> = emptyList()

    override val store: MviStore<TracksPartialState, TracksIntent, TracksState, TracksEffect>
            by viewModels { TracksStoreFactory(localDi.actor, localDi.reducer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = DaggerAppComponent.factory().create(requireContext())
        DaggerTracksComponent.factory().create(appComponent).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTracksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectData()
        if (binding.tracks.isEmpty()) {
            store.sendIntent(TracksIntent.GetTracks)
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.query.collect { query ->
                if (query != null) {
                    if (query.isNotEmpty()) {
                        store.sendIntent(TracksIntent.SearchTracks(query))
                    } else {
                        store.sendIntent(TracksIntent.GetTracks)
                    }
                }
            }
        }
    }

    override fun resolveEffect(effect: TracksEffect) {
        when (effect) {
            is TracksEffect.DownloadTrack ->
                with(effect.track) {
                    store.sendIntent(
                        TracksIntent.AddTrackToLocalDb(
                            trackId = id,
                            title = title,
                            image = album?.image,
                            audio = audio,
                            albumTitle = album?.title,
                            artist = artist.name
                        )
                    )
                }

            is TracksEffect.PlayTrack ->
                (activity as MainActivity).openTrackLaunchActivity(
                    tracks = Gson().toJson(trackItems),
                    currentPosition = effect.trackPosition
                )
            is TracksEffect.ShowSnackBar -> {
                Snackbar.make(
                    requireView(),
                    "${getString(R.string.track)} ${effect.track} ${getString(R.string.was_successfully_downloaded)}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun render(state: TracksState) {
        when (state.content) {
            is LceState.Content -> {
                if (state.newLocalTrack == null) {
                    setUi(state.content.data)
                } else {
                    store.sendEffect(TracksEffect.ShowSnackBar(state.newLocalTrack.second))
                    binding.tracks.updateItem(state.newLocalTrack.first, true)
                }
            }

            is LceState.Error -> {
                setLayoutsVisibility(GONE, VISIBLE)
                binding.tracks.error.onTryAgainClickListener = {
                    val query = viewModel.query.value
                    if (query.isNullOrEmpty()) store.sendIntent(TracksIntent.GetTracks)
                    else store.sendIntent(TracksIntent.SearchTracks(query))
                }
                Log.i(TRACKS_FRAGMENT_TAG, state.content.throwable.message.toString())
            }

            LceState.Loading -> setLayoutsVisibility(VISIBLE, GONE)
        }
    }

    private fun setUi(content: TracksUiList) {
        setLayoutsVisibility(GONE, GONE)
        initSearchView()
        initRecyclerView(content)
    }

    private fun initRecyclerView(tracks: TracksUiList) {
        with(binding.tracks) {
            val query = viewModel.query.value
            clearItems()
            val items = tracks.toRecyclerItems(
                onItemClicked = { store.sendEffect(TracksEffect.PlayTrack(it)) },
                onIconClicked = { store.sendEffect(TracksEffect.DownloadTrack(it)) }
            )
            trackItems = tracks.tracks
            if (items.isNotEmpty()) {
                binding.tracks.emptyView.visibility = GONE
                setItems(items)
            } else {
                with(binding.tracks.emptyView) {
                    if (query.isNullOrEmpty()) setTitle(getString(R.string.nothing_found))
                    else setTitle("${getString(R.string.nothing_found_for)} $query")
                }
                binding.tracks.emptyView.visibility = VISIBLE
            }
        }
    }

    private fun initSearchView() {
        binding.tracks.searchView.doAfterTextChanged { text ->
            viewModel.sendQuery(text.toString())
        }
    }

    private fun setLayoutsVisibility(loading: Int, error: Int) {
        binding.tracks.loading.visibility = loading
        binding.tracks.error.visibility = error
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TRACKS_FRAGMENT_TAG = "TracksFragmentTag"
    }

}