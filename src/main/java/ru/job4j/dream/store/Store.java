package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();
    Collection<Candidate> findAllCandidates();
    void save(Post post);
    void save(Candidate candidate);
    Post findByIdPost(int id);
    Candidate findByIdCandidate(int id);
    void deleteCandidateById(int id);
    void clear(String table);
    User findByEmail(String email);
    boolean registrationUser(User user);
    boolean authenticationUser(User user);
}
