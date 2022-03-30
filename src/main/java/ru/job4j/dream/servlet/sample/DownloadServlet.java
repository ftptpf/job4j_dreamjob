package ru.job4j.dream.servlet.sample;

import ru.job4j.dream.utility.PropertyLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Сервлет скачивает файл, который лежит на сервере.
 */
public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* из параметров запроса извлекаем имя файла который хотим скачать */
        String name = req.getParameter("name");
        File downloadFile = null;
        /* проверяем есть ли нужный нам файл в папке указанной в properties */
        for (File file : Objects.requireNonNull(new File(PropertyLoader.get("images.store")).listFiles())) {
            if (name.equals(file.getName())) {
                downloadFile = file;
                break;
            }
        }
        /* устанавливаем свойства чтобы клиент скачал файл */
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + Objects.requireNonNull(downloadFile).getName() + "\"");
        try (FileInputStream stream = new FileInputStream(Objects.requireNonNull(downloadFile))) {
            resp.getOutputStream().write(stream.readAllBytes());
        }
    }
}
