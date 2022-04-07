package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();
    Collection<Post> findTodayPosts();
    Collection<Candidate> findAllCandidates();
    Collection<Candidate> findTodayCandidates();
    Collection<City> findAllCities();
    void save(Post post);
    void save(Candidate candidate);
    Post findByIdPost(int id);
    Candidate findByIdCandidate(int id);
    Post findByNamePost(String name);
    Candidate findByNameCandidate(String name);
    void deleteCandidateById(int id);
    void clear(String table);
    User findByEmail(String email);
    boolean registrationUser(User user);
    boolean authenticationUser(User user);
}
