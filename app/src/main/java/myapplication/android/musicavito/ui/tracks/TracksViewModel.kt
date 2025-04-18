package myapplication.android.musicavito.ui.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import myapplication.android.core_ui.recycler_item.TrackModel

@OptIn(FlowPreview::class)
class TracksViewModel: ViewModel() {

    private val _items: MutableStateFlow<MutableList<TrackModel>>
            = MutableStateFlow(mutableListOf())
    val items: StateFlow<List<TrackModel>> = _items.asStateFlow()

    private val _query = MutableStateFlow<String?>(null)
    val query: StateFlow<String?> = _query.asStateFlow()

    init {
        viewModelScope.launch {
            query
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { _query.value = it }
        }
    }

    fun sendQuery(query: String){
        _query.value = query
    }

    fun removeItems(){
        _items.value.clear()
    }

    fun addItems(newItems: List<TrackModel>) {
        _items.value.addAll(newItems)
    }

}