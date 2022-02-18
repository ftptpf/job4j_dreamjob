package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

public class MainStore {
    public static void main(String[] args) {
        Store store = DbStore.instOf();
        store.save(new Post(0, "Java job"));
        /* выводим информацию о всех постах */
        store.findAllPosts().forEach(System.out::println);
        /* ищем пост по id = 0 и выводим не печать*/
        System.out.println(store.findById(0));
        /* обновляем пост с id = 0 и выводим на печать */
        store.save(new Post(0, "New Java job"));
        System.out.println(store.findById(0));
        /* сохраняем новый пост с id = 1 в бзу и выводим информацию о всех постах */
        store.save(new Post(1, "Java job for junior"));
        store.findAllPosts().forEach(System.out::println);
    }
}
