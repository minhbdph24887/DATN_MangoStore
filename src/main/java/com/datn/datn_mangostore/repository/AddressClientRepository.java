package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.AddressClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressClientRepository extends JpaRepository<AddressClient, Long> {
    @Query(value = "select * from address_client where save_by_account= :idAccount and status= 1", nativeQuery = true)
    AddressClient addressClientDefault(@Param("idAccount") Long idAccount);

    @Query(value = "select * from address_client where save_by_account= :idAccount", nativeQuery = true)
    List<AddressClient> findAllByAccount(@Param("idAccount") Long id);

    @Query(value = "select distinct addres.* from address_client addres inner join  accounts client on addres.save_by_account = client.id where client.id = :idAccount and addres.status= 1", nativeQuery = true)
    List<AddressClient> findAllByIdAccountAndStatus(@Param("idAccount") Long idAccount);
}
