package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.VoucherClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherClientRepository extends JpaRepository<VoucherClient, Long> {
}
