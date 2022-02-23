package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

public class MainStore {
    public static void main(String[] args) {
        Store store = DbStore.instOf();
        for (int i = 0; i < 3; i++) {
            store.save(new Post(0, "Java job " + i));
        }
        for (int i = 0; i < 3; i++) {
            store.save(new Candidate(0, "Junior Java " + i));
        }

        /* выводим информацию о всех постах */
        store.findAllPosts().forEach(System.out::println);
        /* ищем пост по id = 1 и выводим не печать */
        System.out.println(store.findByIdPost(1));
        /* обновляем пост с id = 1 и выводим на печать */
        store.save(new Post(1, "New Java job"));
        System.out.println(store.findByIdPost(1));
        /* сохраняем новый пост в базу и выводим информацию о всех постах */
        store.save(new Post(0, "Java job for junior"));
        store.findAllPosts().forEach(System.out::println);
        System.out.println();

        /* выводим информацию о всех кандидатах */
        store.findAllCandidates().forEach(System.out::println);
        /* ищем кандидата по id = 1 и выводим на печать */
        System.out.println(store.findByIdCandidate(1));
        /* обновляем кандидата с id = 1 и выводим на печать */
        store.save(new Candidate(1, "Middle Java"));
        System.out.println(store.findByIdCandidate(1));
        /* сохраняем нового кандидата в базу и выводим информацию о всех кандидатах */
        store.save(new Candidate(0, "Senior Java"));
        store.findAllCandidates().forEach(System.out::println);
        /* удаляем кандидата id = 1 из базы и выводим информацию о всех кандидатах */
        store.deleteCandidateById(1);
        store.findAllCandidates().forEach(System.out::println);
    }
}
