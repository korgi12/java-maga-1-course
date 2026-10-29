package ru.maga.musicweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@WebServlet(urlPatterns = {"/", "/artists", "/albums", "/tracks", "/reports"})
public final class MusicServlet extends HttpServlet {
    private static final Set<String> SECTIONS = Set.of("artists", "albums", "tracks", "reports");

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String section = req.getServletPath().substring(1);
        if (!SECTIONS.contains(section)) {
            resp.sendRedirect(req.getContextPath() + "/artists");
            return;
        }
        req.setAttribute("section", section);
        req.setAttribute("artists", MusicRepository.artists());
        req.setAttribute("albums", MusicRepository.albums());
        req.setAttribute("tracks", MusicRepository.tracks());
        req.setAttribute("artistReport", MusicRepository.artistAlbumReport());
        req.setAttribute("trackReport", MusicRepository.shortestTrackReport());
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String type = req.getServletPath().substring(1);
        if (!Set.of("artists", "albums", "tracks").contains(type)) {
            resp.sendError(404);
            return;
        }
        String error = "";
        try {
            String action = req.getParameter("action");
            if (!Set.of("create", "update", "delete").contains(action)) throw new IllegalArgumentException("Invalid action");
            MusicRepository.save(type, action,
                number(req.getParameter("id")),
                req.getParameter("title"),
                req.getParameter("genre"),
                number(req.getParameter("relationId")),
                (int) number(req.getParameter("durationSeconds")));
        } catch (Exception ex) {
            error = "?error=" + URLEncoder.encode(ex.getMessage() == null ? "Operation failed" : ex.getMessage(), StandardCharsets.UTF_8);
        }
        resp.sendRedirect(req.getContextPath() + "/" + type + error);
    }

    private static long number(String text) {
        return text == null || text.isBlank() ? 0 : Long.parseLong(text);
    }
}
