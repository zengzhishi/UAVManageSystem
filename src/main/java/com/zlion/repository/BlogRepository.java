package com.zlion.repository;

import com.zlion.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by zzs on 2016/9/1.
 */
@Repository
public interface BlogRepository extends JpaRepository<Article, Long> {

    @Modifying
    @Query("update Article at set at.title=:qTitle, at.content=:qContent where at.id=:qId")
    public void updateArticle(@Param("qTitle") String title, @Param("qContent") String content, @Param("qId") Long id);

}
