package com.example.boardrepeat.repository;


import com.example.boardrepeat.config.JpaConfig;
import com.example.boardrepeat.domain.Article;
import com.example.boardrepeat.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
public class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired UserAccountRepository userAccountRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void articleRepository_select() {
        // given

        // when
        List<Article> articles = articleRepository.findAll();

        // then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert 테스트")
    @Test
    void articleRepository_insert() {
        // given
        long preCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("newLbk", "pw", null, null, null));
        Article article = Article.of(userAccount, "new article", "new content", "#hashtag");


        // when
        articleRepository.save(article);
        List<Article> articles = articleRepository.findAll();

        // then
        assertThat(articleRepository.count())
                .isEqualTo(preCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void articleRepository_update() {
        // given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#New-Hashtag";

        // when
        article.setHashtag(updatedHashtag);
        Article updatedArticle = articleRepository.saveAndFlush(article);

        // then
        assertThat(updatedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    void articleRepository_delete() {
        // given
        Article article = articleRepository.findById(1L).orElseThrow();
        long preArticleCount = articleRepository.count();
        long preArticleCommentCount = articleCommentRepository.count();
        int deletedCommentCount = article.getArticleComments().size();

        // when
        articleRepository.deleteById(1L);

        // then
        assertThat(articleRepository.count()).isEqualTo(preArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(preArticleCommentCount - deletedCommentCount);
    }
}
