package ru.job4j.dream.store;

import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@Ignore
public class DbStoreTest {

    @Test
    public void whenCreatePost() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Java Job");
        store.save(post);
        Post postInDb = store.findByIdPost(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
        store.clear("post");
    }

    @Test
    public void whenUpdatePost() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Java Job");
        store.save(post);
        Post postInDb = store.findByIdPost(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
        Post post2 = new Post(postInDb.getId(), "New Java Job");
        store.save(post2);
        Post postInDb2 = store.findByIdPost(post2.getId());
        assertThat(postInDb2.getName(), is(post2.getName()));
        store.clear("post");
    }

    @Test
    public void whenFindAllPosts() {
        Store store = DbStore.instOf();
        store.clear("post");
        for (int i = 0; i < 3; i++) {
            store.save(new Post(0, "Java job " + i));
        }
        String result = "[Post{id=1, name='Java job 0', description='null', created=null}, "
                + "Post{id=2, name='Java job 1', description='null', created=null}, "
                + "Post{id=3, name='Java job 2', description='null', created=null}]";
        assertThat(store.findAllPosts().toString(), is(result));
        store.clear("post");
    }

    @Test
    public void whenFindByIdPost() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Java Job");
        store.save(post);
        Post postInDb = store.findByIdPost(post.getId());
        assertThat(postInDb.getId(), is(1));
        store.clear("post");
    }

    @Test
    public void whenClearPost() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Java Job");
        store.save(post);
        String result = "[Post{id=1, name='Java Job', description='null', created=null}]";
        assertThat(store.findAllPosts().toString(), is(result));
        store.clear("post");
        String resultEmpty = "[]";
        assertThat(store.findAllPosts().toString(), is(resultEmpty));
    }

    /* test Candidate */

    @Test
    public void whenCreateCandidate() {
        Store store = DbStore.instOf();
        Candidate candidate = new Candidate(0, "Middle Java");
        store.save(candidate);
        Candidate candidateInDb = store.findByIdCandidate(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
        store.clear("candidate");
    }

    @Test
    public void whenUpdateCandidate() {
        Store store = DbStore.instOf();
        Candidate candidate = new Candidate(0, "Middle Java");
        store.save(candidate);
        Candidate candidateInDb = store.findByIdCandidate(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
        Candidate candidate2 = new Candidate(candidateInDb.getId(), "New Middle Java");
        store.save(candidate2);
        Candidate candidateInDb2 = store.findByIdCandidate(candidate2.getId());
        assertThat(candidateInDb2.getName(), is(candidate2.getName()));
        store.clear("candidate");
    }

    @Test
    public void whenFindAllCandidates() {
        Store store = DbStore.instOf();
        store.clear("candidate");
        for (int i = 0; i < 3; i++) {
            store.save(new Candidate(0, "Middle Java " + i));
        }
        String result = "[Candidate{id=1, name='Middle Java 0'}, "
                + "Candidate{id=2, name='Middle Java 1'}, "
                + "Candidate{id=3, name='Middle Java 2'}]";
        assertThat(store.findAllCandidates().toString(), is(result));
        store.clear("candidate");
    }

    @Test
    public void whenFindByIdCandidate() {
        Store store = DbStore.instOf();
        store.clear("candidate");
        Candidate candidate = new Candidate(0, "Middle Java");
        store.save(candidate);
        Candidate candidateInDb = store.findByIdCandidate(candidate.getId());
        assertThat(candidateInDb.getId(), is(1));
        store.clear("candidate");
    }

    @Test
    public void whenClearCandidate() {
        Store store = DbStore.instOf();
        store.clear("candidate");
        Candidate candidate = new Candidate(0, "Middle Java");
        store.save(candidate);
        String result = "[Candidate{id=1, name='Middle Java'}]";
        assertThat(store.findAllCandidates().toString(), is(result));
        store.clear("candidate");
        String resultEmpty = "[]";
        assertThat(store.findAllCandidates().toString(), is(resultEmpty));
    }
}
