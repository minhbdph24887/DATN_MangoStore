package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query(value = "select count(*) from invoice where invoice_status = 6", nativeQuery = true)
    Integer countByInvoiceStatusCancel();

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
}
