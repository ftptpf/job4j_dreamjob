package ru.job4j.dream.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File users = null;
        for (File file : Objects.requireNonNull(new File("c:\\images\\").listFiles())) {
            if ("users.txt".equals(file.getName())) {
                users = file;
                break;
            }
        }
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + Objects.requireNonNull(users).getName() + "\"");
        try (FileInputStream stream = new FileInputStream(Objects.requireNonNull(users))) {
            resp.getOutputStream().write(stream.readAllBytes());
        }
    }
}
