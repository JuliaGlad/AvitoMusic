package myapplication.android.musicavito.data.database.provider

import myapplication.android.musicavito.App.Companion.app
import myapplication.android.musicavito.data.database.entity.TrackEntity
import java.util.stream.Collectors

class TracksProvider {

    fun getTracks(): List<TrackEntity> =
       app.database.tracksDao().getTracks().ifEmpty { emptyList() }


    fun insertTrack(
        trackId: Long,
        title: String,
        audio: String,
        albumTitle: String?,
        image: String?,
        artist: String
    ) {
        app.database.tracksDao().insertTrack(
            TrackEntity(
                trackId = trackId,
                title = title,
                audio = audio,
                albumTitle = albumTitle,
                image = image,
                artist = artist
            )
        )
    }

    fun getTrackIds(): List<Long> {
        val dao = app.database.tracksDao()
        val tracks = dao.getTracks()
        val ids = tracks.stream()
            .map { it.trackId }
            .collect(Collectors.toList())
        return ids
    }

    fun deleteTrack(trackId: Long) {
        val dao = app.database.tracksDao()
        val tracks = dao.getTracks()
        for (i in tracks){
            if (i.trackId == trackId){
                dao.deleteTrack(i)
                break
            }
        }
    }

    fun deleteTracks() {
        app.database.tracksDao().deleteAll()
    }

}