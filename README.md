## Avito Music
Дизайн приложения в Figma - https://www.figma.com/design/NK1WGitpS5uKHG38ycldmH/AvitoMusic?node-id=62-306&t=RRTSwoaXFJiaCq0I-1

Приложение для прослушивания музыки через Deezer API

## Экран треков из API

- Подгружается список треков по запросу https://api.deezer.com/chart, у каждого трека есть название, исполнитель, обложка, а также кнопка для скачиваня трека

- По клику на кнопку "Скачать" трек добавляется в локальную базу данных, и появляется SnackBar, оповещающий пользователя об успешном сохранении трека

- Реализован поиск по списку треков по запросу https://api.deezer.com/search?q={query}

<img src = https://psv4.userapi.com/s/v1/d/P9IzY5cWc7dcX-oujz9qzFG0f6KPfi7DAopaJ_Umz6TtYf4ga1w61pf3zIHZThPUxaazGmUUNLwiV_k3d7v6Uhn4nDxLnCKOFUgfClnAPkMhkMsC2fFNEw/tracks_screen.png>

## Экран скачанный треков

- Из локальной базы данных берутся сохраненные треки, у каждого трека есть название, исполнитель, обложка

- Реализован свайп влево для удаления трека из скачанных  

- Реализован поиска по локальному списку

<img src = https://psv4.userapi.com/s/v1/d/OkG8VzOC_uSqMuOmzwao_gEWMFjMuecKGKy5gOGVRsYoHYNdxm6uxxvP-fH24fK1WkoNaeMaGM4gPekDWEMAUvOxu-bFubkewEVz3OxBtdgBZhtlXqrcBw/downloaded_tracks.png>
