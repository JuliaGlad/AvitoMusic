## Avito Music
Дизайн приложения в Figma - 8BVyhZYBrywUkJ8SMPcpWWCCG8LEoUpSSYBw7jo3xa46JjVqwY8CC9xGYAk3j4ShUYhNKuLkhqK44MuSq3rq1KYbL7r71iB

Приложение для прослушивания музыки через Deezer API

## Экран треков из API

- Подгружается список треков по запросу https://api.deezer.com/chart, у каждого трека есть название, исполнитель, обложка, а также кнопка для скачиваня трека

- По клику на кнопку "Скачать" трек добавляется в локальную базу данных, и появляется SnackBar, оповещающий пользователя об успешном сохранении трека

- Реализован поиск по списку треков по запросу https://api.deezer.com/search?q={query}

<img src = https://psv4.userapi.com/s/v1/d/P9IzY5cWc7dcX-oujz9qzFG0f6KPfi7DAopaJ_Umz6TtYf4ga1w61pf3zIHZThPUxaazGmUUNLwiV_k3d7v6Uhn4nDxLnCKOFUgfClnAPkMhkMsC2fFNEw/tracks_screen.png>

## Экран скачанный треков

- Из локальной базы данных берутся сохраненные треки, у каждого трека есть название, исполнитель, обложка

- Реализован свайп влево для удаления трека из скачанных  

