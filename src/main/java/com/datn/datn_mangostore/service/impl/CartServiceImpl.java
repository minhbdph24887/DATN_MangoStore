package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.*;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.reponse.InvoiceResponse;
import com.datn.datn_mangostore.repository.*;
import com.datn.datn_mangostore.request.CodeVoucherRequest;
import com.datn.datn_mangostore.request.QuantityCartRequest;
import com.datn.datn_mangostore.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;
    private final InvoiceRepository invoiceRepository;
    private final Gender gender;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final AddressClientRepository addressClientRepository;
    private final VoucherClientRepository voucherClientRepository;
    private final VoucherRepository voucherRepository;
    private final ProductDetailRepository productDetailRepository;

    public CartServiceImpl(AccountRepository accountRepository,
                           RoleRepository roleRepository,
                           ShoppingCartRepository shoppingCartRepository,
                           ShoppingCartDetailRepository shoppingCartDetailRepository,
                           InvoiceRepository invoiceRepository,
                           Gender gender,
                           InvoiceDetailRepository invoiceDetailRepository,
                           AddressClientRepository addressClientRepository,
                           VoucherClientRepository voucherClientRepository,
                           VoucherRepository voucherRepository, ProductDetailRepository productDetailRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
        this.invoiceRepository = invoiceRepository;
        this.gender = gender;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.addressClientRepository = addressClientRepository;
        this.voucherClientRepository = voucherClientRepository;
        this.voucherRepository = voucherRepository;
        this.productDetailRepository = productDetailRepository;
    }

    @Override
    public String indexCart(Model model,
                            HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }

            ShoppingCart detailShoppingCart = shoppingCartRepository.findByIdAccount(detailAccount.getId());
            List<ShoppingCartDetail> listShoppingCartByAccount = shoppingCartDetailRepository.getAllShoppingCart(detailShoppingCart.getId());
            model.addAttribute("listShoppingCartByAccount", listShoppingCartByAccount);

            Integer totalAmount = listShoppingCartByAccount.stream()
                    .mapToInt(ShoppingCartDetail::getCapitalSum)
                    .sum();
            String formattedTotalAmount = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(totalAmount) + " VND";
            model.addAttribute("totalShoppingCart", formattedTotalAmount);
            return "client/Cart/CartIndex";
        }
    }

    @Override
    public String reduceQuantity(Long idShoppingCartDetail) {
        ShoppingCartDetail detail = shoppingCartDetailRepository.findById(idShoppingCartDetail).orElse(null);
        assert detail != null;

        int quantityReduce = detail.getQuantity() - 1;
        detail.setQuantity(quantityReduce);

        detail.setCapitalSum(quantityReduce * detail.getPrice());
        shoppingCartDetailRepository.save(detail);

        List<ShoppingCart> itemsShoppingCart = shoppingCartRepository.getAllShoppingCartById(detail.getShoppingCart().getId());
        ShoppingCart shoppingCart = itemsShoppingCart.isEmpty() ? null : itemsShoppingCart.getFirst();
        assert shoppingCart != null;

        int totalShoppingCartAmount = shoppingCart.getTotalShoppingCart() - detail.getPrice();
        shoppingCart.setTotalShoppingCart(totalShoppingCartAmount);
        shoppingCartRepository.save(shoppingCart);
        if (quantityReduce == 0) {
            shoppingCartDetailRepository.deleteById(idShoppingCartDetail);
        }
        return "redirect:/mangostore/cart";
    }

    @Override
    public ResponseEntity<String> checkIncreaseQuantityForCart(QuantityCartRequest request) {
        ProductDetail detailProduct = productDetailRepository.findById(Long.parseLong(request.getIdProductDetail())).orElse(null);
        assert detailProduct != null;
        if (Integer.parseInt(request.getQuantityNew()) > detailProduct.getQuantity()) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else if (Integer.parseInt(request.getQuantityNew()) >= 50) {
            return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("0", HttpStatus.OK);
        }
    }

    @Override
    public String increaseQuantity(Long idShoppingCartDetail) {
        ShoppingCartDetail detail = shoppingCartDetailRepository.findById(idShoppingCartDetail).orElse(null);
        assert detail != null;

        int quantityIncrease = detail.getQuantity() + 1;
        detail.setQuantity(quantityIncrease);
        detail.setCapitalSum(quantityIncrease * detail.getPrice());
        shoppingCartDetailRepository.save(detail);

        List<ShoppingCart> itemsShoppingCart = shoppingCartRepository.getAllShoppingCartById(detail.getShoppingCart().getId());
        ShoppingCart shoppingCart = itemsShoppingCart.isEmpty() ? null : itemsShoppingCart.getFirst();
        assert shoppingCart != null;

        int totalShoppingCartAmount = shoppingCart.getTotalShoppingCart() + detail.getPrice();
        shoppingCart.setTotalShoppingCart(totalShoppingCartAmount);
        shoppingCartRepository.save(shoppingCart);
        return "redirect:/mangostore/cart";
    }

    @Override
    public String deleteProductCart(Long idShoppingCartDetail) {
        ShoppingCartDetail shoppingCartDetail = shoppingCartDetailRepository.findById(idShoppingCartDetail).orElse(null);
        assert shoppingCartDetail != null;

        List<ShoppingCart> itemsShoppingCart = shoppingCartRepository.getAllShoppingCartById(shoppingCartDetail.getShoppingCart().getId());
        ShoppingCart shoppingCart = itemsShoppingCart.isEmpty() ? null : itemsShoppingCart.getFirst();
        Integer totalSum = shoppingCartDetail.getQuantity() * shoppingCartDetail.getPrice();
        assert shoppingCart != null;
        Integer totalCustomer = shoppingCart.getTotalShoppingCart() - totalSum;
        shoppingCart.setTotalShoppingCart(totalCustomer);
        shoppingCartRepository.save(shoppingCart);
        shoppingCartDetailRepository.deleteById(idShoppingCartDetail);
        return "redirect:/mangostore/cart";
    }

    @Override
    public String viewCheckOut(Model model,
                               HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
            ShoppingCart shoppingCart = shoppingCartRepository.findByIdAccount(detailAccount.getId());
            Invoice checkInvoiceByAccount = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());
            if (checkInvoiceByAccount != null) {
                if (!Objects.equals(checkInvoiceByAccount.getTotalInvoiceAmount(), shoppingCart.getTotalShoppingCart())) {
                    checkInvoiceByAccount.setInvoiceStatus(8);
                    invoiceRepository.save(checkInvoiceByAccount);

                    Invoice newInvoice = new Invoice();
                    newInvoice.setCodeInvoice(gender.generateCode());
                    newInvoice.setNameInvoice(gender.generateNameInvoice());
                    newInvoice.setInvoiceForm("paying");
                    newInvoice.setIdCustomer(detailAccount.getId());
                    newInvoice.setInvoiceCreationDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                    newInvoice.setTotalInvoiceAmount(shoppingCart.getTotalShoppingCart());
                    newInvoice.setCustomerPoints(0);
                    Integer shippingMoney;
                    if (shoppingCart.getTotalShoppingCart() >= 0 && shoppingCart.getTotalShoppingCart() < 500000) {
                        newInvoice.setShippingMoney(15000);
                        shippingMoney = 15000;
                    } else {
                        newInvoice.setShippingMoney(30000);
                        shippingMoney = 30000;
                    }
                    newInvoice.setTotalPayment(shoppingCart.getTotalShoppingCart() + shippingMoney);
                    newInvoice.setInvoiceStatus(0);
                    invoiceRepository.save(newInvoice);

                    List<ShoppingCartDetail> itemsShoppingCartDetail = shoppingCartDetailRepository.getAllShoppingCart(shoppingCart.getId());
                    for (ShoppingCartDetail shoppingCartDetail : itemsShoppingCartDetail) {
                        InvoiceDetail invoiceDetail = new InvoiceDetail();
                        invoiceDetail.setProductDetail(shoppingCartDetail.getProductDetail());
                        invoiceDetail.setInvoice(newInvoice);
                        invoiceDetail.setQuantity(shoppingCartDetail.getQuantity());
                        invoiceDetail.setPrice(shoppingCartDetail.getPrice());
                        invoiceDetail.setForm("online");
                        invoiceDetail.setCapitalSum(shoppingCartDetail.getCapitalSum());
                        invoiceDetailRepository.save(invoiceDetail);
                    }
                }
            } else {
                Invoice newInvoice = new Invoice();
                newInvoice.setCodeInvoice(gender.generateCode());
                newInvoice.setNameInvoice(gender.generateNameInvoice());
                newInvoice.setInvoiceForm("paying");
                newInvoice.setIdCustomer(detailAccount.getId());
                AddressClient newAddressClient = addressClientRepository.addressClientDefault(detailAccount.getId());

                if (newAddressClient != null) {
                    detailAccount.setAddressClient(newAddressClient);
                    accountRepository.save(detailAccount);
                }

                newInvoice.setInvoiceCreationDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                newInvoice.setTotalInvoiceAmount(shoppingCart.getTotalShoppingCart());
                newInvoice.setCustomerPoints(0);
                Integer shippingMoney = 0;
                if (shoppingCart.getTotalShoppingCart() >= 0 && shoppingCart.getTotalShoppingCart() < 500000) {
                    newInvoice.setShippingMoney(15000);
                    shippingMoney = 15000;
                } else {
                    newInvoice.setShippingMoney(30000);
                    shippingMoney = 30000;
                }
                newInvoice.setTotalPayment(shoppingCart.getTotalShoppingCart() + shippingMoney);
                newInvoice.setInvoiceStatus(0);
                invoiceRepository.save(newInvoice);

                List<ShoppingCartDetail> itemsShoppingCartDetail = shoppingCartDetailRepository.getAllShoppingCart(shoppingCart.getId());
                for (ShoppingCartDetail shoppingCartDetail : itemsShoppingCartDetail) {
                    InvoiceDetail invoiceDetail = new InvoiceDetail();
                    invoiceDetail.setProductDetail(shoppingCartDetail.getProductDetail());
                    invoiceDetail.setInvoice(newInvoice);
                    invoiceDetail.setQuantity(shoppingCartDetail.getQuantity());
                    invoiceDetail.setPrice(shoppingCartDetail.getPrice());
                    invoiceDetail.setForm("online");
                    invoiceDetail.setCapitalSum(shoppingCartDetail.getCapitalSum());
                    invoiceDetailRepository.save(invoiceDetail);
                }
            }

            Invoice detailInvoiceOnline = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());
            model.addAttribute("shoppingCart", detailInvoiceOnline);
            model.addAttribute("pointClient", detailAccount.getAccumulatedPoints());
            model.addAttribute("listAddressClient", addressClientRepository.findAllByAccount(detailAccount.getId()));
            model.addAttribute("addressClientStatus", addressClientRepository.findAllByIdAccountAndStatus(detailAccount.getId()));
            model.addAttribute("newAddressClient", new AddressClient());

            List<VoucherClient> findAllVoucherClient = voucherClientRepository.findAllVoucherStatusVoucher1(detailAccount.getId());
            model.addAttribute("listVoucherClient", findAllVoucherClient);

            model.addAttribute("conversionPoint", detailInvoiceOnline.getCustomerPoints() * 1000);

            List<InvoiceDetail> findAllProductByIdInvoice = invoiceDetailRepository.findAllByIdInvoice(detailInvoiceOnline.getId());
            model.addAttribute("listProductByIdInvoice", findAllProductByIdInvoice);
            return "client/Cart/CartCheckOut";
        }
    }

    @Override
    public String addAddressClientForClient(AddressClient newAddressClient,
                                            HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        List<AddressClient> getAllAddressClientByAccount = addressClientRepository.findAllByAccount(detailAccount.getId());

        if (newAddressClient.getStatus() == 1) {
            for (AddressClient addressClient : getAllAddressClientByAccount) {
                if (addressClient.getStatus() == 1) {
                    addressClient.setStatus(0);
                    addressClientRepository.save(addressClient);
                }
            }
        }

        AddressClient newAddressClientClient = new AddressClient();
        newAddressClientClient.setCodeAddress(gender.generateCode());
        newAddressClientClient.setNameClient(newAddressClient.getNameClient());
        newAddressClientClient.setPhoneNumber(newAddressClient.getPhoneNumber());
        newAddressClientClient.setSpecificAddress(newAddressClient.getSpecificAddress());
        newAddressClientClient.setCommune(newAddressClient.getCommune());
        newAddressClientClient.setDistrict(newAddressClient.getDistrict());
        newAddressClientClient.setProvince(newAddressClient.getProvince());
        newAddressClientClient.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newAddressClientClient.setSaveByAccount(detailAccount.getId());
        newAddressClientClient.setStatus(newAddressClient.getStatus());
        addressClientRepository.save(newAddressClientClient);

        AddressClient addressClientDefault = addressClientRepository.addressClientDefault(detailAccount.getId());
        if (addressClientDefault != null) {
            detailAccount.setAddressClient(addressClientDefault);
            accountRepository.save(detailAccount);
        }
        return "redirect:/mangostore/cart/checkout";
    }

    @Override
    public String updateStatusClientAddress(Long id,
                                            HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        List<AddressClient> findAllAddressByAccount = addressClientRepository.findAllByAccount(detailAccount.getId());
        for (AddressClient addressClient : findAllAddressByAccount) {
            if (addressClient.getStatus() == 1) {
                addressClient.setStatus(0);
                addressClientRepository.save(addressClient);
            }
        }

        AddressClient newAddressClientStatus = addressClientRepository.findById(id).orElse(null);
        assert newAddressClientStatus != null;
        newAddressClientStatus.setStatus(1);
        addressClientRepository.save(newAddressClientStatus);

        detailAccount.setAddressClient(newAddressClientStatus);
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/cart/checkout";
    }

    @Override
    public String updateAddressClientForCheckOut(AddressClient editAddressClient) {
        AddressClient updateAddressClient = addressClientRepository.findById(editAddressClient.getId()).orElse(null);
        assert updateAddressClient != null;
        updateAddressClient.setCodeAddress(updateAddressClient.getCodeAddress());
        updateAddressClient.setNameClient(editAddressClient.getNameClient());
        updateAddressClient.setPhoneNumber(editAddressClient.getPhoneNumber());
        updateAddressClient.setSpecificAddress(editAddressClient.getSpecificAddress());
        updateAddressClient.setCommune(editAddressClient.getCommune());
        updateAddressClient.setDistrict(editAddressClient.getDistrict());
        updateAddressClient.setProvince(editAddressClient.getProvince());
        updateAddressClient.setDateCreate(updateAddressClient.getDateCreate());
        updateAddressClient.setSaveByAccount(updateAddressClient.getSaveByAccount());
        updateAddressClient.setStatus(updateAddressClient.getStatus());
        addressClientRepository.save(updateAddressClient);
        return "redirect:/mangostore/cart/checkout";
    }

    @Override
    public String addPointClientToInvoice(HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Invoice detailInvoice = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());

        detailInvoice.setCustomerPoints(detailAccount.getAccumulatedPoints());
        invoiceRepository.save(detailInvoice);

        int voucherAmount = 0;
        if (detailInvoice.getVoucherClient() != null) {
            voucherAmount = detailInvoice.getVoucherClient().getReducedValue();
        }

        int conversionPoint = detailInvoice.getCustomerPoints() * 1000;
        int newPaymentInvoice = detailInvoice.getTotalInvoiceAmount() + detailInvoice.getShippingMoney() - voucherAmount - conversionPoint;
        detailInvoice.setTotalPayment(Math.max(newPaymentInvoice, 0));
        invoiceRepository.save(detailInvoice);
        return "redirect:/mangostore/cart/checkout";
    }

    @Override
    public String addVoucherToInvoice(Long id,
                                      HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        VoucherClient detailVoucherClient = voucherClientRepository.findById(id).orElse(null);
        Invoice detailInvoice = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());

        if (detailInvoice.getVoucher() != null) {
            detailInvoice.setVoucher(null);
        }

        detailInvoice.setVoucherClient(detailVoucherClient);
        invoiceRepository.save(detailInvoice);

        int pointClient = 0;
        if (detailInvoice.getCustomerPoints() != null) {
            pointClient = detailInvoice.getCustomerPoints();
        }

        assert detailVoucherClient != null;
        int conversionPoint = pointClient * 1000;
        int newPaymentInvoice = detailInvoice.getTotalInvoiceAmount() + detailInvoice.getShippingMoney() - conversionPoint - detailVoucherClient.getReducedValue();
        detailInvoice.setTotalPayment(Math.max(newPaymentInvoice, 0));
        invoiceRepository.save(detailInvoice);
        return "redirect:/mangostore/cart/checkout";
    }

    @Override
    public ResponseEntity<String> addVoucherByCodeVoucher(CodeVoucherRequest request,
                                                          HttpSession session) {
        Voucher detailVoucher = voucherRepository.findVoucherByCodeVoucher(request.getCodeVoucher());
        if (detailVoucher == null) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else if (request.getTotalPayment() < detailVoucher.getMinimumPrice()) {
            return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
        } else if (invoiceRepository.countInvoicesByVoucherId(detailVoucher.getId()) > 0) {
            return new ResponseEntity<>("3", HttpStatus.BAD_REQUEST);
        } else {
            String email = (String) session.getAttribute("loginEmail");
            assert email != null;
            Account detailAccount = accountRepository.detailAccountByEmail(email);

            Invoice detailInvoice = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());
            if (detailInvoice.getVoucherClient() != null) {
                detailInvoice.setVoucherClient(null);
            }

            detailInvoice.setVoucher(detailVoucher);
            invoiceRepository.save(detailInvoice);

            int pointClient = 0;
            if (detailInvoice.getCustomerPoints() != null) {
                pointClient = detailInvoice.getCustomerPoints();
            }

            int conversionPoint = pointClient * 1000;
            int newPaymentInvoice = detailInvoice.getTotalInvoiceAmount() + detailInvoice.getShippingMoney() - conversionPoint - detailVoucher.getReducedValue();
            detailInvoice.setTotalPayment(Math.max(newPaymentInvoice, 0));
            invoiceRepository.save(detailInvoice);
            return new ResponseEntity<>("0", HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<String> updateCashInvoice(Long idInvoice) {
        List<InvoiceDetail> getAllInvoiceDetail = invoiceDetailRepository.findAllByIdInvoice(idInvoice);
        boolean hasError = false;

        for (InvoiceDetail detail : getAllInvoiceDetail) {
            ProductDetail productDetail = productDetailRepository.findById(detail.getProductDetail().getId()).orElse(null);
            if (productDetail == null) {
                continue;
            }

            int quantityNew = productDetail.getQuantity() - detail.getQuantity();
            if (quantityNew < 0) {
                hasError = true;
                break;
            } else {
                productDetail.setQuantity(quantityNew);
                productDetailRepository.save(productDetail);
            }
        }

        if (hasError) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        }

        Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
        if (detailInvoice == null) {
            return new ResponseEntity<>("Invoice not found", HttpStatus.NOT_FOUND);
        }

        if (detailInvoice.getVoucher() != null) {
            Voucher findVoucherByInvoice = voucherRepository.findById(detailInvoice.getVoucher().getId()).orElse(null);
            assert findVoucherByInvoice != null;
            int newQuantityVoucher = findVoucherByInvoice.getQuantity() - 1;
            findVoucherByInvoice.setQuantity(newQuantityVoucher);
            voucherRepository.save(findVoucherByInvoice);
        }

        if (detailInvoice.getVoucherClient() != null) {
            VoucherClient voucherClient = voucherClientRepository.findById(detailInvoice.getVoucherClient().getId()).orElse(null);
            assert voucherClient != null;
            voucherClient.setStatus(0);
            voucherClientRepository.save(voucherClient);
        }

        Account detailAccount = accountRepository.findById(detailInvoice.getIdCustomer()).orElse(null);
        assert detailAccount != null;

        detailInvoice.setInvoiceForm("online");
        detailInvoice.setInvoicePaymentDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        detailInvoice.setReturnClientMoney(0);
        detailInvoice.setLeftoverMoney(0);
        detailInvoice.setPayments("cod");
        detailInvoice.setDeliveryAddress(detailAccount.getAddressClient().getSpecificAddress() + ", " + detailAccount.getAddressClient().getCommune() + ", " + detailAccount.getAddressClient().getDistrict() + ", " + detailAccount.getAddressClient().getProvince());
        detailInvoice.setInvoiceStatus(1);
        invoiceRepository.save(detailInvoice);

        ShoppingCart detailShoppingCart = shoppingCartRepository.findByIdAccount(detailInvoice.getIdCustomer());
        if (detailShoppingCart != null) {
            detailShoppingCart.setTotalShoppingCart(0);
            shoppingCartRepository.save(detailShoppingCart);

            List<ShoppingCartDetail> itemsShoppingCart = shoppingCartDetailRepository.getAllShoppingCart(detailShoppingCart.getId());
            for (ShoppingCartDetail shoppingCartDetail : itemsShoppingCart) {
                shoppingCartDetail.setStatus(0);
                shoppingCartDetailRepository.save(shoppingCartDetail);
            }
        }

        if (detailInvoice.getCustomerPoints() != 0) {
            detailAccount.setAccumulatedPoints(0);
            accountRepository.save(detailAccount);
        }

        Double rewardPoints = detailInvoice.getTotalInvoiceAmount().doubleValue() / 12500;
        Integer addPoints = gender.roundingNumber(rewardPoints);
        if (detailInvoice.getCustomerPoints() != 0) {
            detailAccount.setAccumulatedPoints(0);
            accountRepository.save(detailAccount);

            detailAccount.setAccumulatedPoints(addPoints);

        } else {
            int newPoints = detailAccount.getAccumulatedPoints() + addPoints;
            detailAccount.setAccumulatedPoints(newPoints);

        }
        accountRepository.save(detailAccount);
        gender.updateRankByAccumulatedPointsAccount(detailAccount);

        return new ResponseEntity<>("0", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InvoiceResponse> detailInvoiceById(Long id) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);
        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            Account account = accountRepository.findByCodeInvoice(invoice.getCodeInvoice());
            List<InvoiceDetail> invoiceDetails = invoiceDetailRepository.findAllByIdInvoice(id);
            InvoiceResponse invoiceResponse = new InvoiceResponse(invoice, account, invoiceDetails);
            invoiceResponse.setId(id);
            return ResponseEntity.ok(invoiceResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public String bankingVnPay(Long idInvoice,
                               HttpServletRequest request,
                               HttpSession session) {
        Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
        session.setAttribute("idInvoiceClient", idInvoice);
        String vnPayUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        assert detailInvoice != null;
        return "redirect:" + gender.createPaymentVnPay(detailInvoice, vnPayUrl);
    }
}