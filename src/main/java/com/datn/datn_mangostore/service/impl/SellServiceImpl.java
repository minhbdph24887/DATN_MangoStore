package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.*;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.*;
import com.datn.datn_mangostore.request.*;
import com.datn.datn_mangostore.service.SellService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class SellServiceImpl implements SellService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final InvoiceRepository invoiceRepository;
    private final Gender gender;
    private final VoucherRepository voucherRepository;
    private final ProductDetailRepository productDetailRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final RankRepository rankRepository;
    private final AuthenticationRepository authenticationRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final FavouriteRepository favouriteRepository;

    public SellServiceImpl(AccountRepository accountRepository,
                           RoleRepository roleRepository,
                           InvoiceRepository invoiceRepository,
                           Gender gender,
                           VoucherRepository voucherRepository,
                           ProductDetailRepository productDetailRepository,
                           InvoiceDetailRepository invoiceDetailRepository,
                           RankRepository rankRepository,
                           AuthenticationRepository authenticationRepository,
                           ShoppingCartRepository shoppingCartRepository,
                           FavouriteRepository favouriteRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.invoiceRepository = invoiceRepository;
        this.gender = gender;
        this.voucherRepository = voucherRepository;
        this.productDetailRepository = productDetailRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.rankRepository = rankRepository;
        this.authenticationRepository = authenticationRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.favouriteRepository = favouriteRepository;
    }

    @Override
    public String indexSellAdmin(Model model,
                                 HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Invoice> itemsInvoice = invoiceRepository.getAllInvoiceByAccount(detailAccount.getId());
            model.addAttribute("listInvoiceByAccount", itemsInvoice);
            return "admin/Sell/IndexSell";
        }
    }

    @Override
    public ResponseEntity<String> createInvoiceAPI(HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        List<Invoice> checkInvoice = invoiceRepository.getAllInvoiceByAccount(detailAccount.getId());
        if (checkInvoice.size() >= 5) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Invoice newInvoice = new Invoice();
            newInvoice.setCodeInvoice(gender.generateCode());
            newInvoice.setNameInvoice(gender.generateNameInvoice());
            newInvoice.setAccount(detailAccount);
            newInvoice.setInvoiceForm("offline");
            newInvoice.setInvoiceCreationDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newInvoice.setCustomerPoints(0);
            newInvoice.setShippingMoney(0);
            newInvoice.setPayments("live");
            newInvoice.setInvoiceStatus(0);
            invoiceRepository.save(newInvoice);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public String editInvoice(Long idInvoice,
                              Model model,
                              HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Invoice> itemsInvoice = invoiceRepository.getAllInvoiceByAccount(detailAccount.getId());
            model.addAttribute("listInvoiceByAccount", itemsInvoice);
            model.addAttribute("idInvoice", idInvoice);

            Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
            model.addAttribute("detailInvoice", detailInvoice);
            assert detailInvoice != null;

            List<Account> findAllAccount = accountRepository.getAllAccountByStatus1();
            model.addAttribute("listAccount", findAllAccount);
            if (detailInvoice.getIdCustomer() != null) {
                Account detailAccountByIdAccount = accountRepository.findById(detailInvoice.getIdCustomer()).orElse(null);
                assert detailAccountByIdAccount != null;
                model.addAttribute("nameClient", detailAccountByIdAccount.getFullName());
                model.addAttribute("pointClient", detailAccountByIdAccount.getAccumulatedPoints());

                List<Voucher> itemsVoucherOffline = voucherRepository.findVoucherByVoucherFrom(detailAccountByIdAccount.getRank().getId());
                model.addAttribute("listVoucherClient", itemsVoucherOffline);
            } else {
                List<Voucher> itemsVoucherOffline = voucherRepository.getAllVoucherByStatus1();
                model.addAttribute("listVoucherClient", itemsVoucherOffline);
            }

            if (detailInvoice.getVoucher() != null) {
                Voucher getReducedValue = voucherRepository.findById(detailInvoice.getVoucher().getId()).orElse(null);
                assert getReducedValue != null;
                model.addAttribute("discountVouchers", getReducedValue.getReducedValue());
            }

            if (detailInvoice.getTotalInvoiceAmount() != null) {
                model.addAttribute("totalInvoice", detailInvoice.getTotalInvoiceAmount());
            }

            if (detailInvoice.getTotalPayment() != null) {
                model.addAttribute("totalPayment", detailInvoice.getTotalPayment());
            }

            if (detailInvoice.getCustomerPoints() != null) {
                Integer customerPoints = detailInvoice.getCustomerPoints() * 1000;
                model.addAttribute("customerPoints", customerPoints);
            }

            List<ProductDetail> finAllProductDetail = productDetailRepository.getAllProductDetailByStatus1();
            model.addAttribute("listAllProductDetail", finAllProductDetail);

            List<InvoiceDetail> itemsInvoiceDetailByIdInvoice = invoiceDetailRepository.getAllInvoiceDetailByIdInvoice(detailInvoice.getId());
            model.addAttribute("listInvoiceDetail", itemsInvoiceDetailByIdInvoice);
            return "admin/Sell/DetailInvoiceSell";
        }
    }

    @Override
    public ResponseEntity<String> addNumberClientAPI(IdInvoiceRequest request,
                                                     HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Account detailAccountCustom = accountRepository.findById(request.getIdClient()).orElse(null);
        assert detailAccountCustom != null;
        if (Objects.equals(detailAccount.getId(), detailAccountCustom.getId())) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else {
            Invoice invoice = invoiceRepository.findById(request.getIdInvoice()).orElse(null);
            assert invoice != null;
            if (invoice.getIdCustomer() == null) {
                invoice.setIdCustomer(detailAccountCustom.getId());
                invoiceRepository.save(invoice);
            }
            return new ResponseEntity<>("0", HttpStatus.OK);
        }
    }

    @Override
    public String updatePoint(Long idInvoice,
                              Integer pointClient) {
        Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);
        assert invoice != null;
        Account detailAccountCustom = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
        assert detailAccountCustom != null;
        Integer customerPoints = detailAccountCustom.getAccumulatedPoints() * 1000;

        if (invoice.getTotalInvoiceAmount() == null) {
            invoice.setCustomerPoints(pointClient);
            invoiceRepository.save(invoice);
        } else {
            Integer reducedVoucher = 0;
            if (invoice.getVoucher() != null) {
                reducedVoucher = invoice.getVoucher().getReducedValue();
            }

            invoice.setCustomerPoints(pointClient);
            int newTotalPayment = invoice.getTotalInvoiceAmount() - reducedVoucher - customerPoints;
            invoice.setTotalPayment(Math.max(newTotalPayment, 0));
            invoiceRepository.save(invoice);
        }
        return "redirect:/mangostore/admin/sell/edit?id=" + idInvoice;
    }

    @Override
    public String cancelInvoice(Long idInvoice) {
        Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);
        assert invoice != null;
        invoice.setInvoiceStatus(6);
        invoiceRepository.save(invoice);
        return "redirect:/mangostore/admin/sell";
    }

    @Override
    public ResponseEntity<String> addVoucherListForSellAPI(VoucherSellRequest request) {
        Invoice detailInvoice = invoiceRepository.findById(Long.parseLong(request.getIdInvoice())).orElse(null);
        if (detailInvoice == null) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        }
        Integer customerPoints = detailInvoice.getCustomerPoints() * 1000;
        Voucher detailVoucher = voucherRepository.findById(Long.parseLong(request.getIdVoucherCombobox())).orElse(null);
        if (detailVoucher == null) {
            return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
        }
        if (detailVoucher.getQuantity() == 0) {
            return new ResponseEntity<>("3", HttpStatus.BAD_REQUEST);
        }
        detailInvoice.setVoucher(detailVoucher);
        int newTotalPayment = detailInvoice.getTotalInvoiceAmount() - detailVoucher.getReducedValue() - customerPoints;
        detailInvoice.setTotalPayment(Math.max(newTotalPayment, 0));
        invoiceRepository.save(detailInvoice);
        return new ResponseEntity<>("0", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> addVoucherCodeForSellAPI(VoucherSellRequest request) {
        Invoice detailInvoice = invoiceRepository.findById(Long.parseLong(request.getIdInvoice())).orElse(null);
        if (detailInvoice == null) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        }
        Account invoiceCustom = accountRepository.findById(Long.parseLong(request.getIdCustomer())).orElse(null);
        if (invoiceCustom == null) {
            return new ResponseEntity<>("4", HttpStatus.BAD_REQUEST);
        }
        Integer customerPoints = detailInvoice.getCustomerPoints() * 1000;

        Voucher detailVoucherByCode = voucherRepository.findVoucherByCodeVoucher(request.getCodeVoucher());
        if (detailVoucherByCode == null) {
            return new ResponseEntity<>("5", HttpStatus.BAD_REQUEST);
        }

        if (detailVoucherByCode.getRank() != null && invoiceCustom.getRank().getId() < detailVoucherByCode.getRank().getId()) {
            return new ResponseEntity<>("7", HttpStatus.BAD_REQUEST);
        }

        if (detailVoucherByCode.getMinimumPrice() > Integer.parseInt(request.getTotalPayment())) {
            return new ResponseEntity<>("6", HttpStatus.BAD_REQUEST);
        }

        if (detailVoucherByCode.getQuantity() == 0) {
            return new ResponseEntity<>("3", HttpStatus.BAD_REQUEST);
        }

        detailInvoice.setVoucher(detailVoucherByCode);
        int newTotalPayment = detailInvoice.getTotalInvoiceAmount() - detailVoucherByCode.getReducedValue() - customerPoints;
        detailInvoice.setTotalPayment(Math.max(newTotalPayment, 0));
        invoiceRepository.save(detailInvoice);
        return new ResponseEntity<>("0", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> addProductForSellAPI(IdProductRequest request,
                                                       HttpSession session) {
        Invoice detailInvoice = invoiceRepository.findById(request.getIdInvoice()).orElse(null);
        if (detailInvoice == null) {
            return new ResponseEntity<>("Invoice not found", HttpStatus.NOT_FOUND);
        }

        ProductDetail detailProduct = productDetailRepository.findById(request.getIdProductDetail()).orElse(null);
        if (detailProduct == null) {
            return new ResponseEntity<>("Product detail not found", HttpStatus.NOT_FOUND);
        }

        int reducedVoucher = detailInvoice.getVoucher() != null ? detailInvoice.getVoucher().getReducedValue() : 0;
        if (request.getNewQuantity() > detailProduct.getQuantity()) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else {
            InvoiceDetail detailInvoiceDetail = invoiceDetailRepository.findByIdInvoiceAndProductDetails(request.getIdProductDetail(), request.getIdInvoice());

            if (detailInvoiceDetail == null) {
                InvoiceDetail invoiceDetail = new InvoiceDetail();
                invoiceDetail.setProductDetail(detailProduct);
                invoiceDetail.setInvoice(detailInvoice);
                invoiceDetail.setQuantity(request.getNewQuantity());
                invoiceDetail.setPrice(detailProduct.getPrice());
                invoiceDetail.setForm("offline");
                invoiceDetail.setCapitalSum(request.getNewQuantity() * detailProduct.getPrice());
                invoiceDetailRepository.save(invoiceDetail);

                int totalInvoiceAmount = detailInvoice.getTotalInvoiceAmount() != null ? detailInvoice.getTotalInvoiceAmount() : 0;
                totalInvoiceAmount += request.getNewQuantity() * detailProduct.getPrice();
                detailInvoice.setTotalInvoiceAmount(totalInvoiceAmount);

                int customPoints = detailInvoice.getCustomerPoints() * 1000;
                int newPayment = totalInvoiceAmount - reducedVoucher - customPoints;
                detailInvoice.setTotalPayment(Math.max(newPayment, 0));
                invoiceRepository.save(detailInvoice);
            } else {
                int newQuantityUpdate = detailInvoiceDetail.getQuantity() + request.getNewQuantity();
                if (newQuantityUpdate > 50) {
                    return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
                } else {
                    int newCapitalSum = newQuantityUpdate * detailProduct.getPrice();
                    detailInvoiceDetail.setQuantity(newQuantityUpdate);
                    detailInvoiceDetail.setCapitalSum(newCapitalSum);
                    invoiceDetailRepository.save(detailInvoiceDetail);

                    int totalInvoiceAmount = detailInvoice.getTotalInvoiceAmount() != null ? detailInvoice.getTotalInvoiceAmount() : 0;
                    totalInvoiceAmount += request.getNewQuantity() * detailProduct.getPrice();
                    detailInvoice.setTotalInvoiceAmount(totalInvoiceAmount);

                    int customPoints = detailInvoice.getCustomerPoints() * 1000;
                    int newPayment = totalInvoiceAmount - reducedVoucher - customPoints;
                    detailInvoice.setTotalPayment(Math.max(newPayment, 0));
                    invoiceRepository.save(detailInvoice);
                }
            }
            return new ResponseEntity<>("0", HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<String> reduceQuantityProductSell(IdInvoiceDetailRequest request) {
        InvoiceDetail invoiceDetail = invoiceDetailRepository.findById(Long.parseLong(request.getIdInvoiceDetail())).orElse(null);
        assert invoiceDetail != null;
        int newQuantity = invoiceDetail.getQuantity() - 1;
        if (newQuantity == 0) {
            invoiceDetailRepository.delete(invoiceDetail);
            int totalInvoiceAmount = invoiceDetail.getInvoice().getTotalInvoiceAmount() - invoiceDetail.getCapitalSum();
            invoiceDetail.getInvoice().setTotalInvoiceAmount(totalInvoiceAmount);

            int reducedVoucher = invoiceDetail.getInvoice().getVoucher() != null ? invoiceDetail.getInvoice().getVoucher().getReducedValue() : 0;
            int customPoints = invoiceDetail.getInvoice().getCustomerPoints() * 1000;
            int totalPayment = totalInvoiceAmount - reducedVoucher - customPoints;
            invoiceDetail.getInvoice().setTotalPayment(Math.max(totalPayment, 0));
            invoiceRepository.save(invoiceDetail.getInvoice());
        } else {
            int newCapitalSum = newQuantity * invoiceDetail.getPrice();
            invoiceDetail.setQuantity(newQuantity);
            invoiceDetail.setCapitalSum(newCapitalSum);
            invoiceDetailRepository.save(invoiceDetail);

            int totalInvoiceAmount = invoiceDetail.getInvoice().getTotalInvoiceAmount() - invoiceDetail.getPrice();
            invoiceDetail.getInvoice().setTotalInvoiceAmount(totalInvoiceAmount);

            int reducedVoucher = invoiceDetail.getInvoice().getVoucher() != null ? invoiceDetail.getInvoice().getVoucher().getReducedValue() : 0;
            int customPoints = invoiceDetail.getInvoice().getCustomerPoints() * 1000;
            int totalPayment = totalInvoiceAmount - reducedVoucher - customPoints;
            invoiceDetail.getInvoice().setTotalPayment(Math.max(totalPayment, 0));
            invoiceRepository.save(invoiceDetail.getInvoice());
        }
        return new ResponseEntity<>("0", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> increaseQuantityProductSell(IdInvoiceDetailRequest request) {
        InvoiceDetail invoiceDetail = invoiceDetailRepository.findById(Long.parseLong(request.getIdInvoiceDetail())).orElse(null);
        assert invoiceDetail != null;
        int newQuantity = invoiceDetail.getQuantity() + 1;
        if (newQuantity > invoiceDetail.getProductDetail().getQuantity()) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else if (newQuantity > 50) {
            return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
        } else {
            int newCapitalSum = newQuantity * invoiceDetail.getPrice();
            invoiceDetail.setQuantity(newQuantity);
            invoiceDetail.setCapitalSum(newCapitalSum);
            invoiceDetailRepository.save(invoiceDetail);

            int totalInvoiceAmount = invoiceDetail.getInvoice().getTotalInvoiceAmount() + invoiceDetail.getPrice();
            invoiceDetail.getInvoice().setTotalInvoiceAmount(totalInvoiceAmount);

            int reducedVoucher = invoiceDetail.getInvoice().getVoucher() != null ? invoiceDetail.getInvoice().getVoucher().getReducedValue() : 0;
            int customPoints = invoiceDetail.getInvoice().getCustomerPoints() * 1000;
            int totalPayment = totalInvoiceAmount - reducedVoucher - customPoints;
            invoiceDetail.getInvoice().setTotalPayment(Math.max(totalPayment, 0));
            invoiceRepository.save(invoiceDetail.getInvoice());
            return new ResponseEntity<>("0", HttpStatus.OK);
        }
    }

    @Override
    public String deleteProduct(Long idInvoiceDetail) {
        InvoiceDetail invoiceDetail = invoiceDetailRepository.findById(idInvoiceDetail).orElse(null);
        assert invoiceDetail != null;
        invoiceDetailRepository.delete(invoiceDetail);

        int totalInvoiceAmount = invoiceDetail.getInvoice().getTotalInvoiceAmount() - invoiceDetail.getCapitalSum();
        invoiceDetail.getInvoice().setTotalInvoiceAmount(totalInvoiceAmount);

        int reducedVoucher = invoiceDetail.getInvoice().getVoucher() != null ? invoiceDetail.getInvoice().getVoucher().getReducedValue() : 0;
        int customPoints = invoiceDetail.getInvoice().getCustomerPoints() * 1000;
        int totalPayment = totalInvoiceAmount - reducedVoucher - customPoints;
        invoiceDetail.getInvoice().setTotalPayment(Math.max(totalPayment, 0));

        invoiceRepository.save(invoiceDetail.getInvoice());
        return "redirect:/mangostore/admin/sell/edit?id=" + invoiceDetail.getInvoice().getId();
    }

    @Override
    public ResponseEntity<String> updateStatusInvoice(InvoiceRequest request) throws IOException {
        List<InvoiceDetail> itemsAllInvoiceDetail = invoiceDetailRepository.findAllByIdInvoiceOffline(request.getIdInvoice());
        boolean hasError = false;

        for (InvoiceDetail invoiceDetail : itemsAllInvoiceDetail) {
            ProductDetail itemProductDetail = productDetailRepository.findById(invoiceDetail.getProductDetail().getId()).orElse(null);
            assert itemProductDetail != null;
            int quantityNew = itemProductDetail.getQuantity() - invoiceDetail.getQuantity();
            if (quantityNew < 0) {
                hasError = true;
                break;
            } else {
                itemProductDetail.setQuantity(quantityNew);
                productDetailRepository.save(itemProductDetail);
            }
        }

        if (hasError) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        }

        Invoice invoice = invoiceRepository.findById(request.getIdInvoice()).orElse(null);
        if (invoice == null) {
            return new ResponseEntity<>("Invoice not found", HttpStatus.NOT_FOUND);
        }

        if (invoice.getVoucher() != null) {
            Voucher findVoucherByInvoice = voucherRepository.findById(invoice.getVoucher().getId()).orElse(null);
            if (findVoucherByInvoice != null) {
                int newQuantityVoucher = findVoucherByInvoice.getQuantity() - 1;
                findVoucherByInvoice.setQuantity(newQuantityVoucher);
                voucherRepository.save(findVoucherByInvoice);
            }
        }

        if (invoice.getIdCustomer() != null) {
            Account detailAccount = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
            assert detailAccount != null;
            if (invoice.getCustomerPoints() != 0) {
                detailAccount.setAccumulatedPoints(0);
                accountRepository.save(detailAccount);
            }

            Double rewardPoints = invoice.getTotalInvoiceAmount().doubleValue() / 12500;
            Integer addPoints = gender.roundingNumber(rewardPoints);

            invoice.setInvoicePaymentDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            invoice.setReturnClientMoney(request.getReturnClientMoney());
            invoice.setPayments("cash");
            Integer leftoverMoney = request.getReturnClientMoney() - invoice.getTotalPayment();
            invoice.setLeftoverMoney(leftoverMoney);
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
                return new ResponseEntity<>("2", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (invoice.getCustomerPoints() != 0) {
                detailAccount.setAccumulatedPoints(addPoints);
            } else {
                int newPoints = detailAccount.getAccumulatedPoints() + addPoints;
                detailAccount.setAccumulatedPoints(newPoints);
            }
            accountRepository.save(detailAccount);
            gender.updateRankByAccumulatedPointsAccount(detailAccount);
        } else {
            invoice.setInvoicePaymentDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            invoice.setReturnClientMoney(request.getReturnClientMoney());
            invoice.setPayments("cash");
            Integer leftoverMoney = request.getReturnClientMoney() - invoice.getTotalPayment();
            invoice.setLeftoverMoney(leftoverMoney);
            invoice.setInvoiceStatus(5);

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
                return new ResponseEntity<>("2", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            invoiceRepository.save(invoice);
        }

        return new ResponseEntity<>("0", HttpStatus.OK);
    }

    @Override
    public String paymentVnPay(Long idInvoice,
                               HttpServletRequest request,
                               HttpSession session) {
        Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);
        session.setAttribute("idInvoice", idInvoice);
        String vnPayUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        assert invoice != null;
        return "redirect:" + gender.createPaymentVnPay(invoice, vnPayUrl);
    }

    @Override
    public ResponseEntity<String> addNewClientAPI(ClientRequest request) {
        Account detailAccount = accountRepository.findAccountByNumberPhone(Integer.parseInt(request.getNumberPhone()));
        Account checkAccountByEmail = accountRepository.detailAccountByEmail(request.getEmail());
        if (detailAccount != null) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else if (checkAccountByEmail != null) {
            return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
        } else {
            Account newAccount = new Account();
            newAccount.setFullName(request.getNameClient());
            newAccount.setNumberPhone(request.getNumberPhone());
            newAccount.setEmail(request.getEmail());
            newAccount.setAccumulatedPoints(0);

            Rank detailRank = rankRepository.detailRankByAccumulatedPoints(0);
            newAccount.setRank(detailRank);

            newAccount.setStatus(1);
            accountRepository.save(newAccount);

            Role roleUser = roleRepository.getAllRoleByUser();
            Authentication newAuthentication = new Authentication();
            newAuthentication.setAccount(newAccount);
            newAuthentication.setRole(roleUser);
            authenticationRepository.save(newAuthentication);

            ShoppingCart newShoppingCart = new ShoppingCart();
            newShoppingCart.setCodeShoppingCart(gender.generateCode());
            newShoppingCart.setAccount(newAccount);
            newShoppingCart.setTotalShoppingCart(0);
            newShoppingCart.setStatus(1);
            shoppingCartRepository.save(newShoppingCart);

            Favourite newFavourite = new Favourite();
            newFavourite.setCodeFavourite(gender.generateCode());
            newFavourite.setAccount(newAccount);
            newFavourite.setStatus(1);
            favouriteRepository.save(newFavourite);

            return new ResponseEntity<>("0", HttpStatus.OK);
        }
    }
}