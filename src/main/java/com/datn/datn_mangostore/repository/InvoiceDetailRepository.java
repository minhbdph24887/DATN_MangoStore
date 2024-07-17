package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {
    @Query(value = "select * from invoice_detail where id_invoice= :idInvoice and form= 'online'", nativeQuery = true)
    List<InvoiceDetail> findAllByIdInvoice(@Param("idInvoice") Long id);

    @Query(value = "select * from invoice_detail where id_invoice= :idInvoice and form= 'offline'", nativeQuery = true)
    List<InvoiceDetail> findAllByIdInvoiceOffline(@Param("idInvoice") Long id);

    @Query(value = "select * from invoice_detail where form = 'offline' and id_invoice= :idInvoice", nativeQuery = true)
    List<InvoiceDetail> getAllInvoiceDetailByIdInvoice(@Param("idInvoice") Long idInvoice);

    @Query(value = "select * from invoice_detail where id_product_detail= :idProductDetail and id_invoice= :idInvoice and form= 'offline'", nativeQuery = true)
    InvoiceDetail findByIdInvoiceAndProductDetails(@Param("idProductDetail") Long idProductDetail,
                                                   @Param("idInvoice") Long idInvoice);

    @Query(value = "select * from invoice_detail where id_invoice= :idInvoice", nativeQuery = true)
    List<InvoiceDetail> findAllInvoiceDetailByIdInvoice(@Param("idInvoice") Long idInvoice);

    @Query(value = "select top(3)\n" +
            "    id.id_product_detail,\n" +
            "    sum(id.quantity) as total_quantity,\n" +
            "    p.name_product as product_name,\n" +
            "    s.name_size as size_name,\n" +
            "    c.name_color as color_name,\n" +
            "    img.images_file as image_path\n" +
            "from invoice_detail id\n" +
            "         join product_detail pd on id.id_product_detail = pd.id\n" +
            "         join product p on pd.id_product = p.id\n" +
            "         join size s on pd.id_size = s.id\n" +
            "         join color c on pd.id_color = c.id\n" +
            "         join images img on p.id_images = img.id\n" +
            "         join invoice i on id.id_invoice = i.id\n" +
            "where\n" +
            "    i.invoice_status = 5\n" +
            "  and year(i.invoice_payment_date) = :year\n" +
            "group by\n" +
            "    id.id_product_detail,\n" +
            "    p.name_product,\n" +
            "    s.name_size,\n" +
            "    c.name_color,\n" +
            "    img.images_file\n" +
            "order by total_quantity desc;", nativeQuery = true)
    List<Object[]> findTopSellingProductsByYear(@Param("year") Integer year);
}
