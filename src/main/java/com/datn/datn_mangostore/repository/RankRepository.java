package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {
    @Query(value = "select rank.* from rank inner join accounts on accounts.id_rank = rank.id where :accumulatedPoints >= rank.minimum_score and :accumulatedPoints < rank.maximum_score", nativeQuery = true)
    Rank detailRankByAccumulatedPoints(@Param("accumulatedPoints") Integer accumulatedPoints);

    @Query(value = "select * from rank where status= 1 order by id desc", nativeQuery = true)
    List<Rank> getAllRankByStatus1();

    @Query(value = "select * from rank where status= 0 order by id desc", nativeQuery = true)
    List<Rank> getAllRankByStatus0();

    List<Rank> findByCodeRank(String codeRank);

    List<Rank> findByNameRank(String nameRank);

    @Query(value = "select * from rank where name_rank like %:searchRank% and status= 1", nativeQuery = true)
    List<Rank> searchRank(@Param("searchRank") String searchRank);
}
