package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "select * from accounts where email= :email", nativeQuery = true)
    Account detailAccountByEmail(@Param("email") String email);

    @Query(value = "select a.* from accounts a join invoice i on i.id_customer = a.id where i.code_invoice = :codeInvoice", nativeQuery = true)
    Account findByCodeInvoice(@Param("codeInvoice") String codeInvoice);

    @Query(value = "select * from accounts where number_phone= :numberPhoneClient", nativeQuery = true)
    Account findAccountByNumberPhone(@Param("numberPhoneClient") Integer numberPhoneClient);

    @Query(value = "select * from accounts where id= :idAccount", nativeQuery = true)
    Account findAccountByInvoice(@Param("idAccount") Long idAccount);

    @Query(value = "select * from accounts where status= 1 order by id desc", nativeQuery = true)
    List<Account> getAllAccountByStatus1();
}
