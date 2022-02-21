package ru.job4j.dream.servlet.sample;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.store.DbStore;
import ru.job4j.dream.utility.PropertyLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class UploadCandidatePhotoServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(DbStore.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("photoUpload.jsp").forward(req, resp);
    }

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
            File folder = new File(PropertyLoader.get("images.store"));
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
            LOG.error("File Upload Exception information:", e);
        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
