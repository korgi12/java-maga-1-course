<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Музыкальный магазин</title>
    <style>
        * { box-sizing: border-box; }
        body { font: 16px system-ui, sans-serif; margin: 0; background: #f6f7fb; color: #172033; }
        .content { max-width: 1060px; margin: 34px auto; padding: 0 22px; }
        h1 { margin-bottom: 24px; }
        nav { display: flex; gap: 16px; margin-bottom: 28px; flex-wrap: wrap; }
        nav a { color: #27396c; font-weight: 600; text-decoration: none; padding: 8px 12px; border-radius: 8px; }
        nav a:hover { background: #e5e8fa; }
        .panel { background: white; padding: 22px; border-radius: 12px; margin-bottom: 24px; box-shadow: 0 2px 14px #1019390d; }
        table { width: 100%; border-collapse: collapse; }
        th, td { text-align: left; padding: 10px 8px; border-bottom: 1px solid #e7eaf0; }
        th { color: #596477; }
        form { display: grid; grid-template-columns: repeat(auto-fit, minmax(170px, 1fr)); gap: 12px; align-items: end; margin: 14px 0 24px; }
        label { display: grid; gap: 5px; font-size: 14px; }
        input, select, button { font: inherit; border: 1px solid #c9ceda; padding: 10px; border-radius: 7px; width: 100%; }
        button { background: #2b4eb3; color: white; border: none; cursor: pointer; }
        .warning { padding: 12px; background: #ffebeb; border-radius: 8px; margin-bottom: 20px; }
        h2 { margin: 0 0 14px; }
        h3 { margin: 20px 0 4px; font-size: 16px; }
    </style>
</head>
<body>
<div class="content">
    <h1>Музыкальный магазин</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/artists">Исполнители</a>
        <a href="${pageContext.request.contextPath}/albums">Альбомы</a>
        <a href="${pageContext.request.contextPath}/tracks">Композиции</a>
        <a href="${pageContext.request.contextPath}/reports">Запросы</a>
    </nav>
    <c:if test="${not empty param.error}"><div class="warning"><c:out value="${param.error}"/></div></c:if>

    <c:if test="${section == 'artists'}">
        <div class="panel">
            <h2>Исполнители</h2>
            <table><thead><tr><th>ID</th><th>Имя</th></tr></thead><tbody>
            <c:forEach var="artist" items="${artists}"><tr><td>${artist.id}</td><td><c:out value="${artist.name}"/></td></tr></c:forEach>
            </tbody></table>
        </div>
        <div class="panel">
            <h2>Редактирование исполнителей</h2>
            <h3>Добавить</h3>
            <form method="post">
                <input type="hidden" name="action" value="create">
                <label>Имя<input name="title" required data-suggest="artists" list="suggest-artists-create"></label>
                <datalist id="suggest-artists-create"></datalist>
                <button>Добавить</button>
            </form>
            <h3>Изменить</h3>
            <form method="post">
                <input type="hidden" name="action" value="update">
                <label>Исполнитель<select name="id" required><c:forEach var="artist" items="${artists}"><option value="${artist.id}"><c:out value="${artist.name}"/></option></c:forEach></select></label>
                <label>Новое имя<input name="title" required></label>
                <button>Сохранить</button>
            </form>
            <h3>Удалить</h3>
            <form method="post">
                <input type="hidden" name="action" value="delete">
                <label>Исполнитель<select name="id" required><c:forEach var="artist" items="${artists}"><option value="${artist.id}"><c:out value="${artist.name}"/></option></c:forEach></select></label>
                <button>Удалить</button>
            </form>
        </div>
    </c:if>

    <c:if test="${section == 'albums'}">
        <div class="panel">
            <h2>Альбомы</h2>
            <table><thead><tr><th>ID</th><th>Исполнитель</th><th>Альбом</th><th>Жанр</th></tr></thead><tbody>
            <c:forEach var="album" items="${albums}"><tr><td>${album.id}</td><td><c:out value="${album.artist.name}"/></td><td><c:out value="${album.title}"/></td><td><c:out value="${album.genre}"/></td></tr></c:forEach>
            </tbody></table>
        </div>
        <div class="panel">
            <h2>Редактирование альбомов</h2>
            <h3>Добавить</h3>
            <form method="post">
                <input type="hidden" name="action" value="create">
                <label>Исполнитель<select name="relationId" required><c:forEach var="artist" items="${artists}"><option value="${artist.id}"><c:out value="${artist.name}"/></option></c:forEach></select></label>
                <label>Название<input name="title" required data-suggest="albums" data-target="genre-create" list="suggest-albums-create"></label>
                <datalist id="suggest-albums-create"></datalist>
                <label>Жанр<input name="genre" id="genre-create" required></label>
                <button>Добавить</button>
            </form>
            <h3>Изменить</h3>
            <form method="post">
                <input type="hidden" name="action" value="update">
                <label>Альбом<select name="id" required><c:forEach var="album" items="${albums}"><option value="${album.id}"><c:out value="${album.title}"/></option></c:forEach></select></label>
                <label>Исполнитель<select name="relationId" required><c:forEach var="artist" items="${artists}"><option value="${artist.id}"><c:out value="${artist.name}"/></option></c:forEach></select></label>
                <label>Название<input name="title" required data-suggest="albums" data-target="genre-update" list="suggest-albums-update"></label>
                <datalist id="suggest-albums-update"></datalist>
                <label>Жанр<input id="genre-update" name="genre" required></label>
                <button>Сохранить</button>
            </form>
            <h3>Удалить</h3>
            <form method="post">
                <input type="hidden" name="action" value="delete">
                <label>Альбом<select name="id" required><c:forEach var="album" items="${albums}"><option value="${album.id}"><c:out value="${album.title}"/></option></c:forEach></select></label>
                <button>Удалить</button>
            </form>
        </div>
    </c:if>

    <c:if test="${section == 'tracks'}">
        <div class="panel">
            <h2>Композиции</h2>
            <table><thead><tr><th>ID</th><th>Альбом</th><th>Название</th><th>Секунд</th></tr></thead><tbody>
            <c:forEach var="track" items="${tracks}"><tr><td>${track.id}</td><td><c:out value="${track.album.title}"/></td><td><c:out value="${track.title}"/></td><td>${track.durationSeconds}</td></tr></c:forEach>
            </tbody></table>
        </div>
        <div class="panel">
            <h2>Редактирование композиций</h2>
            <h3>Добавить</h3>
            <form method="post">
                <input type="hidden" name="action" value="create">
                <label>Альбом<select name="relationId" required><c:forEach var="album" items="${albums}"><option value="${album.id}"><c:out value="${album.title}"/></option></c:forEach></select></label>
                <label>Название<input name="title" required data-suggest="tracks" data-target="duration-create" list="suggest-tracks-create"></label>
                <datalist id="suggest-tracks-create"></datalist>
                <label>Длительность, сек<input id="duration-create" type="number" min="1" name="durationSeconds" required></label>
                <button>Добавить</button>
            </form>
            <h3>Изменить</h3>
            <form method="post">
                <input type="hidden" name="action" value="update">
                <label>Композиция<select name="id" required><c:forEach var="track" items="${tracks}"><option value="${track.id}"><c:out value="${track.title}"/> — <c:out value="${track.album.title}"/></option></c:forEach></select></label>
                <label>Альбом<select name="relationId" required><c:forEach var="album" items="${albums}"><option value="${album.id}"><c:out value="${album.title}"/></option></c:forEach></select></label>
                <label>Название<input name="title" required data-suggest="tracks" data-target="duration-update" list="suggest-tracks-update"></label>
                <datalist id="suggest-tracks-update"></datalist>
                <label>Длительность, сек<input id="duration-update" type="number" min="1" name="durationSeconds" required></label>
                <button>Сохранить</button>
            </form>
            <h3>Удалить</h3>
            <form method="post">
                <input type="hidden" name="action" value="delete">
                <label>Композиция<select name="id" required><c:forEach var="track" items="${tracks}"><option value="${track.id}"><c:out value="${track.title}"/> — <c:out value="${track.album.title}"/></option></c:forEach></select></label>
                <button>Удалить</button>
            </form>
        </div>
    </c:if>

    <c:if test="${section == 'reports'}">
        <div class="panel">
            <h2>Количество альбомов по исполнителям</h2>
            <table><thead><tr><th>Исполнитель</th><th>Альбомов</th></tr></thead><tbody>
            <c:forEach var="row" items="${artistReport}"><tr><td><c:out value="${row[0]}"/></td><td>${row[1]}</td></tr></c:forEach>
            </tbody></table>
        </div>
        <div class="panel">
            <h2>Минимальная длительность композиции для альбомов с пятью и более композициями</h2>
            <table><thead><tr><th>Альбом</th><th>Длительность, сек</th></tr></thead><tbody>
            <c:forEach var="row" items="${trackReport}"><tr><td><c:out value="${row[0]}"/></td><td>${row[1]}</td></tr></c:forEach>
            </tbody></table>
        </div>
    </c:if>
</div>
<script>
const contextPath = '${pageContext.request.contextPath}';
for (const input of document.querySelectorAll('[data-suggest]')) {
    const list = document.getElementById(input.getAttribute('list'));
    let matches = new Map();
    input.addEventListener('input', async () => {
        const query = input.value.trim();
        if (!query) {
            list.replaceChildren();
            matches.clear();
            return;
        }
        const response = await fetch(contextPath + '/api/suggestions?type=' + encodeURIComponent(input.dataset.suggest) + '&q=' + encodeURIComponent(query));
        if (!response.ok) return;
        const suggestions = await response.json();
        matches = new Map(suggestions.map(item => [item.label, item]));
        list.replaceChildren(...suggestions.map(item => {
            const option = document.createElement('option');
            option.value = item.label;
            return option;
        }));
    });
    input.addEventListener('change', () => {
        const suggestion = matches.get(input.value);
        const target = input.dataset.target && document.getElementById(input.dataset.target);
        if (suggestion && target) target.value = suggestion.extra;
    });
}
</script>
</body>
</html>
