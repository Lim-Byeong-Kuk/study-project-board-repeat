package com.example.boardrepeat.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Disabled("Spring Data Rest 통합테스트는 불필요하므로 제외시킴")
@DisplayName("Data Rest - API 테스트")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class DataRestTest {
    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void test1() throws Exception {
        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/articles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void test2() throws Exception {
        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/articles/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    void test3() throws Exception {
        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/articles/1/articleComments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void test4() throws Exception {
        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/articleComments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void test5() throws Exception {
        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/articleComments/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("[api] 회원 관련 API 는 일체 제공하지 않는다.")
    @Test
    void givenNothing_whenRequestingUserAccounts_thenThrowsException() throws Exception {
        // given & when

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
        mvc.perform(MockMvcRequestBuilders.post("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
        mvc.perform(MockMvcRequestBuilders.put("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
        mvc.perform(MockMvcRequestBuilders.patch("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
        mvc.perform(MockMvcRequestBuilders.delete("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
        mvc.perform(MockMvcRequestBuilders.head("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
