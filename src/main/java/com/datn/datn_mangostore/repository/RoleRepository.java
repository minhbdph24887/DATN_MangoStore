package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "select r.* from accounts a join authentication b on a.id = b.id_account join roles r on b.id_role = r.id where a.email= :email", nativeQuery = true)
    Role getRoleByEmail(@Param("email") String email);

    @Query(value = "select * from roles where name= 'USER'", nativeQuery = true)
    Role getAllRoleByUser();

    @Query(value = "select * from roles where name= 'STAFF'", nativeQuery = true)
    Role getAllRoleByStaff();

    @Query(value = "select * from roles where status= 0 order by id desc", nativeQuery = true)
    List<Role> getAllRoleByStatus0();

    @Query(value = "select * from roles where status= 1 order by id desc ", nativeQuery = true)
    List<Role> getAllRole();

    @Query(value = "select * from roles where name= :nameRole", nativeQuery = true)
    Role findRoleByCodeRole(@Param("nameRole") String name);
}
