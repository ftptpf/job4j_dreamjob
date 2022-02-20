package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

public class MainStore {
    public static void main(String[] args) {
        Store store = DbStore.instOf();
        for (int i = 0; i < 5; i++) {
            store.save(new Post(0, "Java job " + i));
        }
        /* выводим информацию о всех постах */
        store.findAllPosts().forEach(System.out::println);
        /* ищем пост по id = 1 и выводим не печать */
        System.out.println(store.findById(1));
        /* обновляем пост с id = 1 и выводим на печать */
        store.save(new Post(1, "New Java job"));
        System.out.println(store.findById(1));
        /* сохраняем новый пост в базу и выводим информацию о всех постах */
        store.save(new Post(0, "Java job for junior"));
        store.findAllPosts().forEach(System.out::println);
    }
}
