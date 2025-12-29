package com.app.urlshortner.Repository;

import com.app.urlshortner.Entity.Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DetailsRepo extends JpaRepository<Details, String> {

    @Query("""
        SELECT d FROM Details d
            WHERE d.code = :code
                AND (d.expiresAt IS NULL OR d.expiresAt > :now)
    """)
    Optional<Details> findActiveByCode(String code, LocalDateTime now);

    boolean existsByCode(String code);

    @Modifying
    @Query("""
        DELETE FROM Details d
            WHERE d.expiresAt IS NOT NULL
                AND d.expiresAt < :now
    """)
    int deleteExpired(LocalDateTime now);

}
