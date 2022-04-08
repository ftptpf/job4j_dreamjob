package ru.job4j.dream.servlet;

import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.DbStore;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.Store;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CandidateServletTest {

    @Test
    public void whenCreateCandidate() throws IOException {
        //DbStore store = mock(DbStore.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("new candidate");
        when(req.getParameter("citiesId")).thenReturn("1");
        new CandidateServlet().doPost(req, resp);
        Candidate candidate = DbStore.instOf().findByNameCandidate("new candidate");
        assertThat(candidate, notNullValue());
    }
}