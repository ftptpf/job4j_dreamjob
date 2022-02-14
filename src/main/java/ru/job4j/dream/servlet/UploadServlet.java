package ru.job4j.dream.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.job4j.dream.utility.PropertyLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Сервлет загружает файл на сервер.
 */
public class UploadServlet extends HttpServlet {

    /**
     * Получаем список доступных файлов в папке c:\images.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> images = new ArrayList<>();
        for (File name : Objects.requireNonNull(new File(PropertyLoader.get("images.store")).listFiles())) {
            images.add(name.getName());
        }
        req.setAttribute("images", images);
        req.getRequestDispatcher("upload.jsp").forward(req, resp);
    }

    /**
     * Загружаем выбранный файл на сервер в папку c:\images.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* Создаем фабрику по которой можем понять, какие данные есть в запросе. Данные могу быть: поля или файлы. */
        DiskFileItemFactory factory = new DiskFileItemFactory();
        /* Конфигурируем репозиторий для временного хранения файлов */
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        /* Создаем загрузчик файлов */
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            /* Парсим запрос */
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File(PropertyLoader.get("images.store)"));
            /*  Поверяем если элемент не поле,
            то это файл и из него можно прочитать весь входной поток и записать его в файл */
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    File file = new File(folder + File.separator + item.getName());
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(item.getInputStream().readAllBytes());
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        doGet(req, resp);
    }
}
