package myapplication.android.musicavito.ui.downloaded_tracks

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
import kotlinx.coroutines.launch
import myapplication.android.core_mvi.LceState
import myapplication.android.core_mvi.MviBaseFragment
import myapplication.android.core_mvi.MviStore
import myapplication.android.musicavito.R
import myapplication.android.musicavito.databinding.FragmentDownloadedTracksBinding
import myapplication.android.musicavito.di.DaggerAppComponent
import myapplication.android.musicavito.ui.downloaded_tracks.di.DaggerDownloadedTracksComponent
import myapplication.android.musicavito.ui.downloaded_tracks.mvi.DownloadedTracksEffect
import myapplication.android.musicavito.ui.downloaded_tracks.mvi.DownloadedTracksIntent
import myapplication.android.musicavito.ui.downloaded_tracks.mvi.DownloadedTracksLocalDi
import myapplication.android.musicavito.ui.downloaded_tracks.mvi.DownloadedTracksPartialState
import myapplication.android.musicavito.ui.downloaded_tracks.mvi.DownloadedTracksState
import myapplication.android.musicavito.ui.downloaded_tracks.mvi.DownloadedTracksStoreFactory
import myapplication.android.musicavito.ui.mapper.toRecyclerItems
import myapplication.android.musicavito.ui.model.TrackUi
import javax.inject.Inject

class DownloadedTracksFragment : MviBaseFragment<
        DownloadedTracksPartialState,
        DownloadedTracksIntent,
        DownloadedTracksState,
        DownloadedTracksEffect>(R.layout.fragment_downloaded_tracks) {

    @Inject
    lateinit var localDi: DownloadedTracksLocalDi

    private var _binding: FragmentDownloadedTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DownloadedTracksViewModel by viewModels()

    override val store: MviStore<DownloadedTracksPartialState, DownloadedTracksIntent, DownloadedTracksState, DownloadedTracksEffect>
            by viewModels { DownloadedTracksStoreFactory(localDi.actor, localDi.reducer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = DaggerAppComponent.factory().create(requireContext())
        DaggerDownloadedTracksComponent.factory().create(appComponent).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadedTracksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectData()
        if (binding.tracks.isEmpty()){
            store.sendIntent(DownloadedTracksIntent.GetLocalTracks)
        }
    }

    override fun resolveEffect(effect: DownloadedTracksEffect) {
        when (effect) {
            is DownloadedTracksEffect.DeleteTrack -> TODO()
            is DownloadedTracksEffect.PlayTrack -> TODO()
        }
    }

    override fun render(state: DownloadedTracksState) {
        when (state.content) {
            is LceState.Content -> {
                setLayoutsVisibility(GONE, GONE)
                with(state.content.data) {
                    if (filteredTracks == null) setUi(tracks)
                    else {
                        if (filteredTracks!!.isEmpty()) {
                            val query = viewModel.query.value
                            binding.tracks.emptyView.setTitle("${getString(R.string.nothing_found_for)} $query")
                            binding.tracks.emptyView.visibility = VISIBLE
                        } else {
                            setUi(filteredTracks!!)
                        }
                    }
                }
            }

            is LceState.Error -> {
                setLayoutsVisibility(GONE, VISIBLE)
                binding.tracks.error.onTryAgainClickListener = {
                    val query = viewModel.query.value
                    if (query.isNullOrEmpty()) store.sendIntent(DownloadedTracksIntent.GetLocalTracks)
                    else store.sendIntent(DownloadedTracksIntent.FilterTracks(query))
                }
                Log.e(DOWNLOADED_TRACKS_FRAGMENT_TAG, state.content.throwable.message.toString())
            }

            LceState.Loading -> setLayoutsVisibility(VISIBLE, GONE)
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.query.collect { query ->
                if (query != null) {
                    if (query.isNotEmpty()) {
                        store.sendIntent(DownloadedTracksIntent.FilterTracks(query))
                    } else {
                        store.sendIntent(DownloadedTracksIntent.GetLocalTracks)
                        binding.tracks.emptyView.visibility = GONE
                    }
                }
            }
        }
    }

    private fun setUi(content: List<TrackUi>) {
        setLayoutsVisibility(GONE, GONE)
        initSearchView()
        initRecyclerView(content)
    }

    private fun initRecyclerView(tracks: List<TrackUi>) {
        with(binding.tracks) {
            val query = viewModel.query.value
            clearItems()
            val items = tracks.toRecyclerItems(
                onItemClicked = { store.sendEffect(DownloadedTracksEffect.PlayTrack(it)) },
                isVisible = false
            )
            binding.tracks.emptyView.visibility = GONE
            if (items.isNotEmpty()) setItems(items)
            else {
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
        const val DOWNLOADED_TRACKS_FRAGMENT_TAG = "DownloadedTracksFragmentTag"
    }

}