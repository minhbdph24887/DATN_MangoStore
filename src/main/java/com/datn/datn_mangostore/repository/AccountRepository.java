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

    @Query(value = "select ac.* from accounts ac \n" +
            "           join authentication au on ac.id = au.id_account \n" +
            "           join roles r on r.id = au.id_role \n" +
            "            where r.id = 1 and ac.status = 1 order by id desc ", nativeQuery = true)
    List<Account> getAllAccountByRoleManagerAndStatus1();

    @Query(value = "select ac.* from accounts ac \n" +
            "           join authentication au on ac.id = au.id_account \n" +
            "           join roles r on r.id = au.id_role \n" +
            "            where r.id = 3 and ac.status = 1 order by id desc ", nativeQuery = true)
    List<Account> getAllAccountByRoleStaffAndStatus1();

    @Query(value = "select ac.* from accounts ac \n" +
            "           join authentication au on ac.id = au.id_account \n" +
            "           join roles r on r.id = au.id_role \n" +
            "            where r.id = 1 and ac.status = 0 order by id desc ", nativeQuery = true)
    List<Account> getAllAccountByRoleManagerAndStatus0();

    @Query(value = "select ac.* from accounts ac \n" +
            "           join authentication au on ac.id = au.id_account \n" +
            "           join roles r on r.id = au.id_role \n" +
            "            where r.id = 3 and ac.status = 0 order by id desc ", nativeQuery = true)
    List<Account> getAllAccountByRoleStaffAndStatus0();

    @Query(value = " select ac.* from accounts ac " +
            "join authentication au on ac.id = au.id_account " +
            "join roles r on r.id = au.id_role " +
            "where r.id = 2 and ac.status = 1 ", nativeQuery = true)
    List<Account> getAllAccountByClient();

    @Query(value = " select ac.* from accounts ac " +
            "join authentication au on ac.id = au.id_account " +
            "join roles r on r.id = au.id_role " +
            "where r.id = 2 and ac.status = 0 ", nativeQuery = true)
    List<Account> getAllAccountByClient0();

    @Query(value = "select a.* from accounts a join authentication b on a.id = b.id_account join roles r on b.id_role = r.id where r.id= :idRole", nativeQuery = true)
    List<Account> getAllAccountByRole(@Param("idRole") Long idRole);

    @Query(value = "select * from accounts where number_phone= :numberPhoneClient", nativeQuery = true)
    Account findAccountByNumberPhone(@Param("numberPhoneClient") String numberPhoneClient);
}
