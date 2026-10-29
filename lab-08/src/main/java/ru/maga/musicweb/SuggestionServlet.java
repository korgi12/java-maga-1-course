package ru.maga.musicweb;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/suggestions")
public final class SuggestionServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String type = req.getParameter("type");
        String term = req.getParameter("q") == null ? "" : req.getParameter("q");
        if (type == null || !List.of("artists", "albums", "tracks").contains(type)) {
            resp.sendError(400);
            return;
        }
        if (term.length() > 100) { resp.sendError(400); return; }
        var suggestions = MusicRepository.suggestions(type, term);
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < suggestions.size(); i++) {
            MusicRepository.Suggestion item = suggestions.get(i);
            if (i > 0) json.append(',');
            json.append("{\"id\":").append(item.id());
            json.append(",\"label\":\"").append(escape(item.label())).append('"');
            json.append(",\"extra\":\"").append(escape(item.extra())).append("\"}");
        }
        resp.getWriter().write(json.append(']').toString());
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}
