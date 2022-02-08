package ru.job4j.dream.servlet;

import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class DeleteCandidateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> id = Optional.ofNullable(req.getParameter("id"));
        if (id.isPresent()) {
            for (File file : Objects.requireNonNull(new File("c:\\images\\").listFiles())) {
                Optional<String> fileName = Arrays.stream(file.getName().split(".")).findFirst();
                if (fileName.isPresent()) {
                    if (id.get().equals(fileName.get()) && !file.isDirectory()) {
                        Files.delete(file.toPath());
                        break;
                    }
                }
            }
            Store.instOf().deleteCandidateById(Integer.parseInt(id.get()));
        }
        req.getRequestDispatcher("candidates.jsp").include(req, resp);
        resp.sendRedirect(req.getContextPath() + "/delete.do");
    }
}
