package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.*;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.*;
import com.datn.datn_mangostore.service.CheckVnPayPaymentStatusService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CheckVnPayPaymentStatusServiceImpl implements CheckVnPayPaymentStatusService {
    private final Gender gender;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final AccountRepository accountRepository;
    private final VoucherClientRepository voucherClientRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;
    private final VoucherRepository voucherRepository;

    public CheckVnPayPaymentStatusServiceImpl(Gender gender,
                                              InvoiceRepository invoiceRepository,
                                              InvoiceDetailRepository invoiceDetailRepository,
                                              ProductDetailRepository productDetailRepository,
                                              AccountRepository accountRepository,
                                              VoucherClientRepository voucherClientRepository,
                                              ShoppingCartRepository shoppingCartRepository,
                                              ShoppingCartDetailRepository shoppingCartDetailRepository,
                                              VoucherRepository voucherRepository) {
        this.gender = gender;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.productDetailRepository = productDetailRepository;
        this.accountRepository = accountRepository;
        this.voucherClientRepository = voucherClientRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
        this.voucherRepository = voucherRepository;
    }

    @Override
    public String bankingSuccess(HttpServletRequest request, HttpSession session) throws IOException {
        Long idInvoice = (Long) session.getAttribute("idInvoice");
        if (idInvoice != null) {
            int paymentStatus = gender.orderReturn(request);
            boolean hasErrorInvoiceAdmin = false;
            if (paymentStatus == 1) {
                Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);
                assert invoice != null;

                List<InvoiceDetail> getAllInvoiceDetail = invoiceDetailRepository.getAllInvoiceDetailByIdInvoice(idInvoice);
                for (InvoiceDetail detail : getAllInvoiceDetail) {
                    ProductDetail productDetail = productDetailRepository.findById(detail.getProductDetail().getId()).orElse(null);
                    assert productDetail != null;
                    int quantityNew = productDetail.getQuantity() - detail.getQuantity();
                    if (quantityNew < 0) {
                        hasErrorInvoiceAdmin = true;
                        break;
                    } else {
                        productDetail.setQuantity(quantityNew);
                        productDetailRepository.save(productDetail);
                    }
                }

                if (hasErrorInvoiceAdmin) {
                    return "redirect:/mangostore/admin/sell/edit?id=" + idInvoice;
                }

                if (invoice.getVoucher() != null) {
                    Voucher voucher = voucherRepository.findById(invoice.getVoucher().getId()).orElse(null);
                    assert voucher != null;
                    int newQuantity = voucher.getQuantity() - 1;
                    voucher.setQuantity(newQuantity);
                    voucherRepository.save(voucher);
                }

                invoice.setInvoicePaymentDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                invoice.setReturnClientMoney(invoice.getTotalPayment());
                invoice.setPayments("banking");
                invoice.setLeftoverMoney(0);
                invoice.setReturnClientMoney(0);
                invoice.setInvoiceStatus(5);
                invoiceRepository.save(invoice);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss dd-MM-yyyy");
                String formattedDate = invoice.getInvoicePaymentDate().format(formatter);

                String directoryPath = "src/main/resources/static/bill";
                Path path = Paths.get(directoryPath);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }

                String pdfFileName = invoice.getCodeInvoice() + "_" + invoice.getNameInvoice() + "_" + formattedDate + ".pdf";
                String pdfPath = directoryPath + "/" + pdfFileName;

                try {
                    gender.createInvoicePDF(pdfPath, invoice);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (invoice.getIdCustomer() != null) {
                    Double rewardPoints = invoice.getTotalInvoiceAmount().doubleValue() / 12500;
                    Integer addPoints = gender.roundingNumber(rewardPoints);
                    Account detailAccount = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
                    assert detailAccount != null;
                    Integer points = detailAccount.getAccumulatedPoints() + addPoints;
                    detailAccount.setAccumulatedPoints(points);
                    accountRepository.save(detailAccount);

                    gender.updateRankByAccumulatedPointsAccount(detailAccount);
                    accountRepository.save(detailAccount);
                    session.removeAttribute("idInvoice");
                }
                return "redirect:/mangostore/admin/sell";
            } else {
                return "redirect:/mangostore/admin/sell/edit?id=" + idInvoice;
            }
        }

        Long idInvoiceClient = (Long) session.getAttribute("idInvoiceClient");
        if (idInvoiceClient != null) {
            int paymentStatus = gender.orderReturn(request);
            boolean hasErrorInvoiceClient = false;
            if (paymentStatus == 1) {
                Invoice invoice = invoiceRepository.findById(idInvoiceClient).orElse(null);
                assert invoice != null;

                Account detailAccount = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
                assert detailAccount != null;

                Account detailAccountToShoppingCart = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
                assert detailAccountToShoppingCart != null;
                ShoppingCart detailShoppingCart = shoppingCartRepository.findByIdAccount(detailAccountToShoppingCart.getId());
                detailShoppingCart.setTotalShoppingCart(0);
                shoppingCartRepository.save(detailShoppingCart);

                List<ShoppingCartDetail> findAllShoppingCart = shoppingCartDetailRepository.getAllShoppingCart(detailShoppingCart.getId());
                for (ShoppingCartDetail shoppingCartDetail : findAllShoppingCart) {
                    shoppingCartDetail.setStatus(0);
                    shoppingCartDetailRepository.save(shoppingCartDetail);
                }

                if (invoice.getVoucher() != null) {
                    Voucher voucher = voucherRepository.findById(invoice.getVoucher().getId()).orElse(null);
                    assert voucher != null;
                    int newQuantity = voucher.getQuantity() - 1;
                    voucher.setQuantity(newQuantity);
                    voucherRepository.save(voucher);
                }

                if (invoice.getVoucherClient() != null) {
                    VoucherClient voucherClient = voucherClientRepository.findById(invoice.getVoucherClient().getId()).orElse(null);
                    assert voucherClient != null;
                    voucherClient.setStatus(0);
                    voucherClientRepository.save(voucherClient);
                }

                if (invoice.getCustomerPoints() != 0) {
                    detailAccount.setAccumulatedPoints(0);
                    accountRepository.save(detailAccount);
                }

                invoice.setInvoiceForm("online");
                invoice.setInvoicePaymentDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                invoice.setReturnClientMoney(0);
                invoice.setLeftoverMoney(0);
                invoice.setPayments("banking");
                invoice.setDeliveryAddress(detailAccount.getAddressClient().getSpecificAddress() + ", " + detailAccount.getAddressClient().getCommune() + ", " + detailAccount.getAddressClient().getDistrict() + ", " + detailAccount.getAddressClient().getProvince());
                invoice.setInvoiceStatus(1);
                invoiceRepository.save(invoice);

                if (invoice.getIdCustomer() != null) {
                    Double rewardPoints = invoice.getTotalInvoiceAmount().doubleValue() / 12500;
                    Integer addPoints = gender.roundingNumber(rewardPoints);
                    if (invoice.getCustomerPoints() != 0) {
                        detailAccount.setAccumulatedPoints(0);
                        accountRepository.save(detailAccount);
                        detailAccount.setAccumulatedPoints(addPoints);
                    } else {
                        int newPoints = detailAccount.getAccumulatedPoints() + addPoints;
                        detailAccount.setAccumulatedPoints(newPoints);

                    }
                    accountRepository.save(detailAccount);
                    gender.updateRankByAccumulatedPointsAccount(detailAccount);
                    session.removeAttribute("idInvoiceClient");
                }
                return "redirect:/mangostore/home";
            } else {
                return "redirect:/mangostore/cart/checkout";
            }
        }
        return "";
    }
}