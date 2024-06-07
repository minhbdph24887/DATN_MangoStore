package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {
    @Query(value = "select rank.* from rank inner join accounts on accounts.id_rank = rank.id where :accumulatedPoints >= rank.minimum_score and :accumulatedPoints < rank.maximum_score", nativeQuery = true)
    Rank detailRankByAccumulatedPoints(@Param("accumulatedPoints") Integer accumulatedPoints);
}
