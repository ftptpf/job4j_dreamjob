package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = new User(
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("password"));
        boolean userAddedToDatabase = DbStore.instOf().registrationUser(user);
        if (userAddedToDatabase) {
            req.setAttribute("addedUser", user);
            resp.sendRedirect(req.getContextPath() + "/auth.do");
        } else {
            req.setAttribute("notAdded", "Пользователь с таким email уже зарегистрирован в базе.");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);


        }

    }
}
