package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = "select * from invoice where id_customer= :idCustomer and invoice_form= 'paying' and invoice_status= 0", nativeQuery = true)
    Invoice findInvoiceByIdAccount(@Param("idCustomer") Long idCustomer);

    @Query(value = "select top 1 name_invoice from invoice order by name_invoice desc", nativeQuery = true)
    String getMaxInvoiceCode();

    @Query(value = "select * from invoice where id_customer= :idAccount and invoice_form= 'online' and invoice_status= :statusInvoice", nativeQuery = true)
    Page<Invoice> findAllInvoiceOnline(@Param("idAccount") Long idAccount,
                                       @Param("statusInvoice") Integer statusInvoice,
                                       Pageable pageable);

    @Query(value = "select count(*) from invoice where id_voucher = :idVoucher", nativeQuery = true)
    int countInvoicesByVoucherId(@Param("idVoucher") Long idVoucher);

    @Query(value = "select * from invoice where id_customer= :idAccount and invoice_form= 'online'", nativeQuery = true)
    Page<Invoice> findAllInvoiceOnlineByAllStatus(@Param("idAccount") Long idAccount,
                                                  Pageable pageable);

    @Query(value = "select * from invoice where invoice_status= 0 and id_account= :idAccount", nativeQuery = true)
    List<Invoice> getAllInvoiceByAccount(@Param("idAccount") Long idAccount);

    @Query(value = "select * from invoice where invoice_status= 1 and invoice_form= 'online'", nativeQuery = true)
    List<Invoice> findAllInvoiceOnline();

    @Query(value = "select * from invoice where id_account= :idAccount and invoice_status in (2, 3, 4, 5)", nativeQuery = true)
    List<Invoice> getAllInvoiceOnline(@Param("idAccount") Long idAccount);

    @Query(value = "select count(*) from invoice where invoice_status = :statusInvoice and year(invoice_payment_date)= :years", nativeQuery = true)
    Integer countByInvoiceStatusAndYear(@Param("statusInvoice") Integer statusInvoice,
                                        @Param("years") Integer years);

    @Query(value = "select count(*) from invoice where invoice_status = 6 and year(invoice_payment_date)= :years", nativeQuery = true)
    Integer countByInvoiceStatusCancel(@Param("years") Integer years);

    @Query(value = "select sum(invoice.total_payment) from invoice where invoice_status= 5 and year(invoice_payment_date)= :years", nativeQuery = true)
    Long sumTotalPaymentByYear(@Param("years") Integer years);

    @Query(value = "select month(invoice_payment_date) as month, sum(total_payment) as totalRevenue from invoice where invoice_status = 5 and year(invoice_payment_date) = :year group by month(invoice_payment_date)", nativeQuery = true)
    List<Object[]> findMonthlyRevenueByYear(@Param("year") Integer year);

    @Query(value = "select * from invoice where id_account= :idAccount and invoice_status in (2, 3, 4, 5) and code_invoice like %:codeInvoice%", nativeQuery = true)
    List<Invoice> findInvoiceByCodeInvoice(@Param("idAccount") Long idAccount,
                                           @Param("codeInvoice") String findByCode);

    @Query(value = "select count(*) from invoice where invoice_status = :statusInvoice and year(invoice_payment_date) = :years and month(invoice_payment_date) between :startMonth and :endMonth", nativeQuery = true)
    Integer countByInvoiceStatusAndYearAndQuarter(@Param("statusInvoice") Integer statusInvoice,
                                                  @Param("years") Integer years,
                                                  @Param("startMonth") int startMonth,
                                                  @Param("endMonth") int endMonth);

    @Query(value = "select sum(invoice.total_payment) from invoice where invoice_status= 5 and year(invoice_payment_date)= :years and month(invoice_payment_date) between :startMonth and :endMonth", nativeQuery = true)
    Long sumTotalPaymentByYearAndQuarter(@Param("years") Integer years,
                                         @Param("startMonth") int startMonth,
                                         @Param("endMonth") int endMonth);

    @Query(value = "select count(*) from invoice where invoice_status = 6 and year(invoice_payment_date) = :years and month(invoice_payment_date) between :startMonth and :endMonth", nativeQuery = true)
    Integer countByInvoiceStatusCancelAndQuarter(@Param("years") Integer years,
                                                 @Param("startMonth") int startMonth,
                                                 @Param("endMonth") int endMonth);

    @Query(value = "select month(invoice_payment_date) as month, sum(total_payment) as totalRevenue from invoice where invoice_status = 5 and year(invoice_payment_date) = :year and month(invoice_payment_date) between :startMonth and :endMonth group by month(invoice_payment_date)", nativeQuery = true)
    List<Object[]> findQuarterlyRevenueByYear(@Param("year") Integer year,
                                              @Param("startMonth") int startMonth,
                                              @Param("endMonth") int endMonth);

    @Query(value = "select * from invoice where invoice_status in (2, 3, 4, 5)", nativeQuery = true)
    List<Invoice> findAllOrder();

    @Query(value = "select * from invoice where invoice_status in (2, 3, 4, 5) and code_invoice like %:codeInvoice%", nativeQuery = true)
    List<Invoice> searchOrder(@Param("codeInvoice") String findByCode);

    @Query(value = "select count(*) from invoice where invoice_status = :statusInvoice and cast(invoice_payment_date as date) between :startDate and :endDate", nativeQuery = true)
    Integer countByInvoiceStatusAndDateRange(@Param("statusInvoice") Integer statusInvoice,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    @Query(value = "select count(*) from invoice where invoice_status = :statusInvoice and cast(invoice_payment_date as date) between :startDate and :endDate and id_account= :idStaff", nativeQuery = true)
    Integer countByInvoiceStatusAndDateRangeByStaff(@Param("statusInvoice") Integer statusInvoice,
                                                    @Param("idStaff") Long idStaff,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query(value = "select count(*) from invoice where invoice_status = :statusInvoice and cast(invoice_payment_date as date) = :date", nativeQuery = true)
    Integer countByInvoiceStatusAndDate(@Param("statusInvoice") Integer statusInvoice,
                                        @Param("date") LocalDate date);

    @Query(value = "select count(*) from invoice where invoice_status = :statusInvoice and cast(invoice_payment_date as date) = :date and id_account= :idStaff", nativeQuery = true)
    Integer countByInvoiceStatusAndDateByStaff(@Param("statusInvoice") Integer statusInvoice,
                                               @Param("idStaff") Long idStaff,
                                               @Param("date") LocalDate date);

    @Query(value = "select sum(i.total_payment) from Invoice i where cast(i.invoice_payment_date as date) between :start and :end", nativeQuery = true)
    Long sumTotalPaymentByDateRange(@Param("start") LocalDate start,
                                    @Param("end") LocalDate end);

    @Query(value = "select sum(i.total_payment) from Invoice i where cast(i.invoice_payment_date as date) between :start and :end and id_account= :idStaff", nativeQuery = true)
    Long sumTotalPaymentByDateRangeByStaff(@Param("start") LocalDate start,
                                           @Param("idStaff") Long idStaff,
                                           @Param("end") LocalDate end);

    @Query(value = "select sum(i.total_payment) from Invoice i where cast(i.invoice_payment_date as date) = :today", nativeQuery = true)
    Long sumTotalPaymentByDate(@Param("today") LocalDate today);

    @Query(value = "select sum(i.total_payment) from Invoice i where cast(i.invoice_payment_date as date) = :today and id_account= :idStaff", nativeQuery = true)
    Long sumTotalPaymentByDateByStaff(@Param("idStaff") Long idStaff,
                                      @Param("today") LocalDate today);

    @Query(value = "select day(i.invoice_payment_date), sum(i.total_payment) from Invoice i where cast(i.invoice_payment_date as date) between :start and :end group by day(i.invoice_payment_date)", nativeQuery = true)
    List<Object[]> findDailyRevenueByDateRange(@Param("start") LocalDate start,
                                               @Param("end") LocalDate end);

    @Query(value = "select day(i.invoice_payment_date), sum(i.total_payment) from Invoice i where cast(i.invoice_payment_date as date) between :start and :end and id_account= :idStaff group by day(i.invoice_payment_date) ", nativeQuery = true)
    List<Object[]> findDailyRevenueByDateRangeByStaff(@Param("idStaff") Long idStaff,
                                                      @Param("start") LocalDate start,
                                                      @Param("end") LocalDate end);

    @Query(value = "select day(i.invoice_payment_date), sum(i.total_payment) from Invoice i where cast(i.invoice_payment_date as date) = :date group by day(i.invoice_payment_date)", nativeQuery = true)
    List<Object[]> findDailyRevenueByDate(@Param("date") LocalDate date);

    @Query(value = "select day(i.invoice_payment_date), sum(i.total_payment) from Invoice i where cast(i.invoice_payment_date as date) = :date and id_account= :idStaff group by day(i.invoice_payment_date)", nativeQuery = true)
    List<Object[]> findDailyRevenueByDateByStaff(@Param("idStaff") Long idStaff,
                                                 @Param("date") LocalDate date);
}