package myapplication.android.musicavito.ui.track_launch

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import myapplication.android.musicavito.ui.model.TrackUi
import java.util.stream.Collectors

class TracksLauncherViewModel: ViewModel() {

    var tracks: MutableList<TrackUi> = mutableListOf()
    var covers: MutableList<String> = mutableListOf()

    fun initTracks(tracksExtra: String){
        tracks = Gson().fromJson(
            tracksExtra,
            object : TypeToken<List<TrackUi>>() {}.type
        )
        initCovers()
    }

    private fun initCovers(){
        covers = tracks.stream()
            .map { it.album?.image }
            .collect(Collectors.toList())
    }

}