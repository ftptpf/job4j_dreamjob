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
    }
}