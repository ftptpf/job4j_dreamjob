package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DbStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();
    private static final Logger LOG = LoggerFactory.getLogger(DbStore.class.getName());

    private DbStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(DbStore.class.getClassLoader()
                                .getResourceAsStream("db.properties"))
                )
        )) {
            cfg.load(io);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new DbStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    /**
     * Получаем информацию из базы обо всех постах.
     * @return
     */
    public List<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(
                            it.getInt("id"),
                            it.getString("name")));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return posts;
    }

    /**
     * Получаем информацию из базы о всех кандидатах.
     * @return
     */
    public List<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(
                            it.getInt("id"),
                            it.getString("name")));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return candidates;
    }

    /**
     * Получаем информацию из базы о городах.
     * @return
     */
    public List<City> findAllCities() {
        List<City> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM city")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cities.add(new City(
                            it.getInt("id"),
                            it.getString("name")));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return cities;
    }

    /**
     * Сохраняем пост в базу.
     * Если получаем пост с id = 0, это признак нового поста который должен будет записан в базу,
     * в ином случае пост обновляется.
     * @param post
     */
    public void save(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else {
            update(post);
        }
    }

    /**
     * Сохраняем кандидата в базу.
     * Если получаем кандидата с id = 0, это признак нового кандидата который должен будет записан в базу,
     * в ином случае запись о кандидате обновляется в базе.
     * @param candidate
     */
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            update(candidate);
        }
    }


    /**
     * Создаем пост в базе.
     * @param post
     * @return созданный пост
     */
    private Post createPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO post(name, create_date) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            Date currentDate = new Date(System.currentTimeMillis());
            ps.setDate(2, currentDate);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return post;
    }

    /**
     * Создаем запись о кандидате в базе.
     * @param candidate
     * @return созданный кандидат
     */
    private Candidate createCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO candidate(name, city_id, create_date) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCityId());
            Date currentDate = new Date(System.currentTimeMillis());
            ps.setDate(3, currentDate);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return candidate;
    }

    /**
     * Обновляем пост в базе.
     * @param post
     */
    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE post SET name = ? WHERE id = ?")) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
    }

    /**
     * Обновляем запись о кандидате в базе.
     * @param candidate
     */
    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE candidate SET name = ?, city_id = ? WHERE id = ?")) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCityId());
            ps.setInt(3, candidate.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
    }

    /**
     * Ищем в базе пост по id.
     * @param id
     * @return
     */
    public Post findByIdPost(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(
                            it.getInt("id"),
                            it.getString("name"));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return null;
    }

    /**
     * Ищем в базе кандидата по id.
     * @param id
     * @return
     */
    public Candidate findByIdCandidate(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Candidate(
                            it.getInt("id"),
                            it.getString("name"));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return null;
    }

    /**
     * Ищем в базе пост по наименованию.
     * @param name
     * @return
     */
    public Post findByNamePost(String name) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet post = ps.executeQuery()) {
                if (post.next()) {
                    return new Post(
                            post.getInt("id"),
                            post.getString("name"));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return null;
    }

    /**
     * Ищем в базе кандидата по имени.
     * @param name
     * @return
     */
    public Candidate findByNameCandidate(String name) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet candidate = ps.executeQuery()) {
                if (candidate.next()) {
                    return new Candidate(
                            candidate.getInt("id"),
                            candidate.getString("name"));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return null;
    }

    /**
     * Удаляем запись о кандидате из базы данных по-заданному id.
     * @param id
     */
    public void deleteCandidateById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM candidate WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
    }

    /**
     * Очищаем таблицу от данных, и обнуляем счетчик чтобы он начинался с id = 1.
     * @param table имя таблицы
     */
    public void clear(String table) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "DELETE FROM " + table + "; ALTER TABLE " + table + " ALTER COLUMN id RESTART WITH 1;")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
    }

    /**
     * Проверка есть ли пользователь в базе.
     * Проверку делаем по email.
     * @param user
     * @return
     */
    public boolean existUser(User user) {
        return findByEmail(user.getEmail()) != null;
    }

    /**
     * Ищем пользователя в базе по email.
     * @param email
     * @return
     */
    public User findByEmail(String email) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return null;
    }

    /**
     * Регистрация пользователя в базе.
     * Проверяем есть ли пользователь в базе, если нет - создаем.
     * @param user
     * @return
     */
    public boolean registrationUser(User user) {
        if (!existUser(user)) {
            createUser(user);
            return true;
        }
        return false;
    }

    /**
     * Создаем запись пользователя в базе.
     * @param user
     * @return созданный пользователь
     */
    private User createUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO users(name, email, password) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return user;
    }

    /**
     * Аутентификация пользователя.
     * Проверяем email и пароль.
     * @param user
     * @return
     */
    public boolean authenticationUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM users WHERE email = ? AND password = ?")) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return false;
    }

    /**
     * Получаем информацию из базы обо всех постах которые были созданы сегодня.
     * @return
     */
    public List<Post> findTodayPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM post WHERE create_date = CURRENT_DATE")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(
                            it.getInt("id"),
                            it.getString("name")));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return posts;
    }

    /**
     * Получаем информацию из базы о всех кандидатах которые были созданы сегодня.
     * @return
     */
    public List<Candidate> findTodayCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM candidate WHERE create_date = CURRENT_DATE")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(
                            it.getInt("id"),
                            it.getString("name")));
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception information:", e);
        }
        return candidates;
    }


}
