package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.AddressClient;
import com.datn.datn_mangostore.bean.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
    @Query(value = "select * from address_client where status= 1 order by id desc", nativeQuery = true)
    List<AddressClient> getAllAddressClientByStatus1();

    @Query(value = "select * from address_client where status= 0 order by id desc", nativeQuery = true)
    List<AddressClient> getAllAddressClientByStatus0();

    @Query(value = "select * from address_client where name_client like %:searchNameClient% and status= 1", nativeQuery = true)
    List<AddressClient> searchNameClient(@Param("searchNameClient") String searchClient);

    @Query(value = "select distinct addres.* from address_client addres inner join  accounts client on addres.id_account = client.id where client.id = :idAccount and addres.status= 1", nativeQuery = true)
    List<AddressClient> findAllByIdAccountAndStatus(@Param("idAccount") Long idAccount);

    @Query(value = "select * from address_client where id_account= :idAccount", nativeQuery = true)
    List<AddressClient> findAllByAccount(@Param("idAccount") Long id);

    @Query(value = "select * from address_client where id_account= :idAccount and status= 1", nativeQuery = true)
    AddressClient addressClientDefault(@Param("idAccount") Long idAccount);

    @Query(value = "select count(*) AS admin_count\n" +
            "from accounts a\n" +
            "join authentication auth on a.id = auth.id_account\n" +
            "join roles r on auth.id_role = r.id\n" +
            "where r.name = 'ADMIN'", nativeQuery = true)
    Integer countAccountByRoleAdmin();

    @Query(value = "select \n" +
            "case \n" +
            "when count(auth.id_account) > 0 then 'true' \n" +
            "else 'false' \n" +
            "end as isAdmin\n" +
            "from authentication auth\n" +
            "join roles r on auth.id_role = r.id\n" +
            "where auth.id_account = :idAccount and r.name = 'ADMIN'", nativeQuery = true)
    boolean isAdminAccount(@Param("idAccount") Long idAccount);
}
