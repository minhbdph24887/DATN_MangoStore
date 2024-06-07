package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = "select * from invoice where id_customer= :idCustomer and invoice_form= 'paying' and invoice_status= 0", nativeQuery = true)
    Invoice findInvoiceByIdAccount(@Param("idCustomer") Long idCustomer);

    @Query(value = "select top 1 name_invoice from invoice order by name_invoice desc", nativeQuery = true)
    String getMaxInvoiceCode();
}
