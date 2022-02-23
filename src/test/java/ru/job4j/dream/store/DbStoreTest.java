package ru.job4j.dream.store;

import org.junit.Test;
import ru.job4j.dream.model.Post;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

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
    }
}