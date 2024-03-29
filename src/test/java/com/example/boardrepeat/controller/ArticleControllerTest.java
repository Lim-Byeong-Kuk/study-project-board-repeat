package com.example.boardrepeat.controller;

import com.example.boardrepeat.config.SecurityConfig;
import com.example.boardrepeat.dto.ArticleWithCommentsDto;
import com.example.boardrepeat.dto.UserAccountDto;
import com.example.boardrepeat.service.ArticleService;
import com.example.boardrepeat.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;    // MockBean 은 @Autowired 로 생성자 주입 안됨
    @MockBean private PaginationService paginationService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상호출")
    @Test
    void articles() throws Exception {
        // given
        BDDMockito.given(articleService.searchArticles(
                ArgumentMatchers.eq(null), ArgumentMatchers.eq(null), ArgumentMatchers.any(Pageable.class)))
                        .willReturn(Page.empty());
        BDDMockito.given(paginationService.getPaginationBarNumbers(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                        .willReturn(List.of(0,1,2,3,4));

        // when & then
        mvc.perform(MockMvcRequestBuilders.get("/articles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("paginationBarNumbers"));

        BDDMockito.then(articleService).should().searchArticles(ArgumentMatchers.eq(null), ArgumentMatchers.eq(null), ArgumentMatchers.any(Pageable.class));
        BDDMockito.then(paginationService).should().getPaginationBarNumbers(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상호출")
    @Test
    void given_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // given
        Long articleId = 1L;
        BDDMockito.given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

        // when & then
        mvc.perform(MockMvcRequestBuilders.get("/articles/" + articleId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("article"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articleComments"));

        BDDMockito.then(articleService).should().getArticle(articleId);
    }

    @Disabled
    @DisplayName("[view][GET} 게시글 해시태그 검색 페이지 - 정상호출")
    @Test
    void given_when_then() throws Exception {
        // given

        // when & then
        mvc.perform(MockMvcRequestBuilders.get("articles/search-hashtag"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/search-hashtag"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles"));

    }

    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "lbk",
                LocalDateTime.now(),
                "lbk"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(1L,
                "lbk",
                "pw",
                "lbk@gmail.com",
                "Forest",
                "memo",
                LocalDateTime.now(),
                "lbk",
                LocalDateTime.now(),
                "lbk"
        );
    }
}
