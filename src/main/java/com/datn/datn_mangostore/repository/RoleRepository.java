package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "select r.* from accounts a join authentication b on a.id = b.id_account join roles r on b.id_role = r.id where a.email= :email", nativeQuery = true)
    Role getRoleByEmail(@Param("email") String email);

    @Query(value = "select * from roles where name= 'USER'", nativeQuery = true)
    Role getAllRoleByUser();
}
