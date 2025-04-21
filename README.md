## Avito Music
Дизайн приложения в Figma - https://www.figma.com/design/NK1WGitpS5uKHG38ycldmH/AvitoMusic?node-id=62-306&t=RRTSwoaXFJiaCq0I-1

Приложение для прослушивания музыки через Deezer API

## Экран треков из API

- Подгружается список треков по запросу https://api.deezer.com/chart, у каждого трека есть название, исполнитель, обложка, а также кнопка для скачивания трека, по клику на трек открывается экран воспроизведения трека

- По клику на кнопку "Скачать" трек добавляется в локальную базу данных, и появляется SnackBar, оповещающий пользователя об успешном сохранении трека

- Реализован поиск по списку треков по запросу https://api.deezer.com/search?q={query}

<img src = https://psv4.userapi.com/s/v1/d/P9IzY5cWc7dcX-oujz9qzFG0f6KPfi7DAopaJ_Umz6TtYf4ga1w61pf3zIHZThPUxaazGmUUNLwiV_k3d7v6Uhn4nDxLnCKOFUgfClnAPkMhkMsC2fFNEw/tracks_screen.png>

## Экран скачанный треков

- Из локальной базы данных берутся сохраненные треки, у каждого трека есть название, исполнитель, обложка, по клику на трек открывается экран воспроизведения трека

- Реализован свайп влево для удаления трека из скачанных  

- Реализован поиска по локальному списку

<img src = https://psv4.userapi.com/s/v1/d/OkG8VzOC_uSqMuOmzwao_gEWMFjMuecKGKy5gOGVRsYoHYNdxm6uxxvP-fH24fK1WkoNaeMaGM4gPekDWEMAUvOxu-bFubkewEVz3OxBtdgBZhtlXqrcBw/downloaded_tracks.png>

## Экран воспроизведения треков

- На экране отображены название трека/альбома, его исполнитель, обложка трека

- Можно управлять воспроизведением трека: перемотка с помощью SeekBar, переключение следующего/предыдущего трека по кнопкам или при свайпе обложек(ViewPager2), остановка/воспроизведение

- Список треков воспроизводится циклично: при завершении последней композиции из списка, заново начинается первая и наоборот

- Под Seekbar также отображается сколько времени прошло от начала трека и сколько осталось до конца композиции

- При сворачивании приложения на данном экране появляется уведомление (ForegroundService) для фонового прослушивания музыки

<img src = https://psv4.userapi.com/s/v1/d/R0HrYLh0BdhKlOF9CISFhnE9LiaxCzHCIqpZT0h1_82nvktkxqpHcEu7x760GwitG2zi_7iDxNdYT7-1K-q9WrqadpOnWjdqEl3Dbl_VLgNqRHCRL_aZIA/proslushivanie_treka.png>

## Фоновый плеер

- При сворачивании экрана воспроизведения треков показывается фоновый плеер в виде foreground уведомления с кастомный layout, реализованным посредством RemoteViews

- Трек продолжается с того же момента, на котором остановился при закрытии экрана, можно останавливать и воспроизводить трек, а также переключаться на следующую/предыдущую композицию из списка

- Треки воспроизводятся циклично

- По клику на уведомление пользователь возвращается обратно на экран воспроизведения, воспроизводится тот же трек и на том же моменте, на котором пользователь остановился

<img src = https://psv4.userapi.com/s/v1/d/cG-wo7jOUCbQvbkO5OK3h77Nu_MtNrLxLSlCKAzfBQCIRKc59tadshBsuGTC1cu3vayPTN21ogMPRdU4yg1QZ9ibTrEFRRdyLk7MGJNvaBSjRIhBR6Gk8Q/uvedomlenia.png>

