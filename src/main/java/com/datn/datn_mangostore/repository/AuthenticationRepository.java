package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
}
