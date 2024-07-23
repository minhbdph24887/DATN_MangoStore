package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
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
