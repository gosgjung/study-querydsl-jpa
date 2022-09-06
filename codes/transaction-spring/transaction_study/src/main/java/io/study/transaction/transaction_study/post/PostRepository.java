package io.study.transaction.transaction_study.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Post p set p.version = p.version + 1 where p.id = :id")
    public void updateNewVersion(@Param("id") Long id);
}
