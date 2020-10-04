# Recommendations builder engine
## The task
> Рекомендательный алгоритм. Эту часть я ожидаю увидеть в коде (Используем Spring / Hibernate )​​​​. На сайте есть 3 типа материалов (diletant.media): новости/тесты/статьи. Из метаданных можно использовать теги/автора/просмотры, так же возможно вам что-то еще придет в голову ;) На данный момент мы не привязываемся к данным - поэтому можете брать на свое усмотрение материалы. Задача сделать rest api, который по запросу материала генерирует выборку рекомендательных материалов. (Как это выглядит сейчас можно посмотреть открыв любую статью на сайте и проскролить до блока "рекомендованно вам"). Особое внимание нужно обратить на алгоритм и скорость его работы.
## How to start
1. Make sure your Elasticsearch is running.
2. Load test data to elasticsearch
    1. Load post index model <br>
        `curl -XPUT "http://localhost:9200/post?pretty" -H "Content-Type: application/json" -d "@\src\main\resources\indexModel.json"`
    2. Load test entities to index, I used [elasticsearch_loader](https://github.com/moshe/elasticsearch_loader) <br>
        `elasticsearch_loader --index post json .\src\main\resources\articlesData.json`
        `elasticsearch_loader --index post json .\src\main\resources\quizData.json`
3. Start this app.
## Endpoints
1. GET /api/post?pageSize - returns you all posts, pageSize is 10 by default
2. GET /api/post/search - returns posts by search query. <br>
    Consumes JSON Object: `{ "query": "<your search query here>" }`
3. GET /api/post/recommendations/{id} - returns recommendations by post id (you can receive it by getting all posts).
