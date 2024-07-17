package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query(value = "select * from voucher where voucher_status in (0, 1, 2) and status= 0 order by id desc", nativeQuery = true)
    List<Voucher> getAllVoucherByStatus0();

    @Query(value = "select * from voucher where voucher_status in (1, 2) and status = 1 order by id desc", nativeQuery = true)
    List<Voucher> getAllVoucherByStatus1And2();

    @Query(value = "select * from voucher where voucher_status in (1, 2) and code_voucher like %:codeVoucher% and status= :status", nativeQuery = true)
    List<Voucher> searchVoucherByCode(@Param("codeVoucher") String codeVoucher,
                                      @Param("status") Integer status);

    @Query(value = "select * from voucher where voucher_status in (1, 2) and name_voucher like %:nameVoucher% and status= :status", nativeQuery = true)
    List<Voucher> searchVoucherByName(@Param("nameVoucher") String nameVoucher,
                                      @Param("status") Integer status);

    @Query(value = "select * from voucher where voucher_status in (1, 2) and status= 1 and voucher_from= 'online'", nativeQuery = true)
    List<Voucher> getAllVoucherOnline();

    @Query(value = "select * from voucher where code_voucher= :codeVoucher and voucher_from= 'all' and voucher_status= 1 and status= 1", nativeQuery = true)
    Voucher findVoucherByCodeVoucher(@Param("codeVoucher") String codeVoucher);

    @Query(value = "select * from voucher where code_voucher= :codeVoucher", nativeQuery = true)
    Voucher getVoucherByCodeVoucher(@Param("codeVoucher") String codeVoucher);

    @Query(value = "select * from voucher where name_voucher= :nameVoucher", nativeQuery = true)
    Voucher getVoucherByNameVoucher(@Param("nameVoucher") String nameVoucher);

    @Query(value = "select v.* from voucher v join rank r on v.id_rank = r.id where v.voucher_status = 1 and v.status = 1 and v.voucher_from = 'offline' and v.start_day <= cast(getdate() as date) and v.end_date >= cast(getdate() as date) and r.id <= :idRank", nativeQuery = true)
    List<Voucher> findVoucherByVoucherFrom(@Param("idRank") Long idRank);

    @Query(value = "select * from voucher where voucher_status= 1 and status= 1 and voucher_from= 'offline' and id_rank= 1 order by id desc", nativeQuery = true)
    List<Voucher> getAllVoucherByStatus1();
}
