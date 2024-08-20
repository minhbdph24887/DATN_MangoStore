package com.datn.datn_mangostore.config;

import com.datn.datn_mangostore.bean.*;
import com.datn.datn_mangostore.reponse.PriceRange;
import com.datn.datn_mangostore.repository.*;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Configuration
public class Gender {
    private final AccountRepository accountRepository;
    private final JavaMailSender mailSender;
    private final InvoiceRepository invoiceRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final RankRepository rankRepository;
    private final ResourceLoader resourceLoader;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final RoleRepository roleRepository;

    public Gender(AccountRepository accountRepository,
                  JavaMailSender mailSender,
                  InvoiceRepository invoiceRepository,
                  ProductDetailRepository productDetailRepository,
                  ShoppingCartDetailRepository shoppingCartDetailRepository,
                  ShoppingCartRepository shoppingCartRepository,
                  RankRepository rankRepository,
                  ResourceLoader resourceLoader,
                  InvoiceDetailRepository invoiceDetailRepository,
                  RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.mailSender = mailSender;
        this.invoiceRepository = invoiceRepository;
        this.productDetailRepository = productDetailRepository;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.rankRepository = rankRepository;
        this.resourceLoader = resourceLoader;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.roleRepository = roleRepository;
    }

    public String generateVerificationCode() {
        int code = (int) ((Math.random() * 900000) + 100000);
        return String.valueOf(code);
    }

    public void saveVerificationCode(String email,
                                     String verificationCode) {
        Account account = accountRepository.detailAccountByEmail(email);
        if (account != null) {
            account.setVeryCode(verificationCode);
            accountRepository.save(account);
        }
    }

    public void sendEmail(String to,
                          String subject,
                          String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        String content = "Code để bạn đặt lại mật khẩu là: " + verificationCode;
        message.setText(content);
        mailSender.send(message);
    }

    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss");
        return now.format(formatter);
    }

    public String generateCode() {
        int leftLimit = 48;
        int rightLimit = 90;
        int targetStringLength = 10;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateNameInvoice() {
        String maxInvoiceCode = invoiceRepository.getMaxInvoiceCode();
        if (maxInvoiceCode != null) {
            int maxInvoiceNumber = Integer.parseInt(maxInvoiceCode.substring(2));
            return "HD" + String.format("%04d", maxInvoiceNumber + 1);
        } else {
            return "HD0001";
        }
    }

    public Integer roundingNumber(Double number) {
        return Math.toIntExact(Math.round(number));
    }

    public String createPaymentVnPay(Invoice invoice,
                                     String vnPayUrl) {
        long amount = invoice.getTotalPayment() * 100;
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        vnPayUrl += VnPayConfig.vnp_Returnurl;
        vnp_Params.put("vnp_ReturnUrl", vnPayUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VnPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    public int orderReturn(HttpServletRequest request) {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII);
            fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VnPayConfig.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public PriceRange getPriceRangeByIdProduct(Long idProduct) {
        List<Object[]> prices = productDetailRepository.findAllPriceByIdProduct(idProduct);
        if (prices != null && !prices.isEmpty()) {
            Object[] priceRangeArray = prices.get(0);
            Integer priceMin = (Integer) priceRangeArray[0];
            Integer priceMax = (Integer) priceRangeArray[1];
            return new PriceRange(priceMin, priceMax);
        }
        return null;
    }

    public Map<Long, PriceRange> getPriceRangMap() {
        Map<Long, PriceRange> priceRangeMap = new HashMap<>();
        for (ProductDetail productDetail : productDetailRepository.findAll()) {
            Long idProduct = productDetail.getProduct().getId();
            PriceRange priceRange = getPriceRangeByIdProduct(idProduct);
            priceRangeMap.put(idProduct, priceRange);
        }
        return priceRangeMap;
    }

    public void updateTotalShoppingCart(ShoppingCart shoppingCart) {
        List<ShoppingCartDetail> shoppingCartDetails = shoppingCartDetailRepository.getAllShoppingCart(shoppingCart.getId());
        Integer total = shoppingCartDetails.stream()
                .mapToInt(ShoppingCartDetail::getCapitalSum)
                .sum();
        shoppingCart.setTotalShoppingCart(total);
        shoppingCartRepository.save(shoppingCart);
    }

    public void updateRankByAccumulatedPointsAccount(Account detailAccount) {
        List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
        itemsRank.sort((rank1, rank2) -> rank2.getMaximumScore().compareTo(rank1.getMaximumScore()));
        for (Rank rank : itemsRank) {
            if (detailAccount.getAccumulatedPoints() >= rank.getMinimumScore() && detailAccount.getAccumulatedPoints() < rank.getMaximumScore()) {
                detailAccount.setRank(rank);
                break;
            } else if (detailAccount.getAccumulatedPoints() >= rank.getMaximumScore()) {
                detailAccount.setRank(itemsRank.getFirst());
                break;
            }
        }
        accountRepository.save(detailAccount);
    }

    public void createInvoicePDF(String est, Invoice invoice) throws IOException {
        PdfWriter writer = new PdfWriter(est);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        Resource fontResource = resourceLoader.getResource("classpath:static/font/Roboto-Light.ttf");
        PdfFont font;
        try (InputStream fontStream = fontResource.getInputStream()) {
            font = PdfFontFactory.createFont(fontStream.readAllBytes(), PdfEncodings.IDENTITY_H, true);
        }

        document.setFontProvider(new FontProvider());
        document.setFont(font);

        Resource resource = resourceLoader.getResource("classpath:static/images/layout/logoMangoStore1.png");
        try (InputStream is = resource.getInputStream()) {
            ImageData data = ImageDataFactory.create(is.readAllBytes());
            Image img = new Image(data).scaleToFit(100, 100);
            img.setHorizontalAlignment(HorizontalAlignment.LEFT);
            document.add(img);
        }

        Paragraph storeName = new Paragraph("Cửa Hàng Quần Áo Công Sở Nam MangoStore")
                .setFont(font)
                .setFontSize(16)
                .setBold()
                .setMarginLeft(120);
        document.add(storeName);

        Paragraph invoiceTitle = new Paragraph("Hóa Đơn Thanh Toán")
                .setFont(font)
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(invoiceTitle);

        document.add(new Paragraph("Mã hóa đơn: " + invoice.getCodeInvoice()));
        Account detailAccount = null;
        if (invoice.getIdCustomer() != null) {
            detailAccount = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
        }

        if (detailAccount != null) {
            document.add(new Paragraph("Tên người mua hàng: " + detailAccount.getFullName()));
        } else {
            document.add(new Paragraph("Tên người mua hàng: Không có thông tin khách hàng"));
        }

        document.add(new Paragraph("Địa chỉ: FPT PolyTechnic College Kieu Mai Campus, Kieu Mai Street, Phuc Dien, Tu Liem District, Hanoi City"));
        document.add(new Paragraph("Ngày thanh toán: " + invoice.getInvoicePaymentDate().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"))));
        document.add(new Paragraph("Tên người bán hàng: " + invoice.getAccount().getFullName()));

        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 1, 1, 1, 1}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell("STT");
        table.addHeaderCell("Tên Sản Phẩm");
        table.addHeaderCell("Đơn Vị Tính");
        table.addHeaderCell("Số Lượng");
        table.addHeaderCell("Đơn Giá");
        table.addHeaderCell("Thành Tiền");

        List<InvoiceDetail> findAllInvoiceDetailByInvoice = invoiceDetailRepository.findAllByIdInvoiceOffline(invoice.getId());
        int stt = 1;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (InvoiceDetail detail : findAllInvoiceDetailByInvoice) {
            ProductDetail productDetail = detail.getProductDetail();
            String productName = productDetail.getProduct().getNameProduct();
            String productSize = productDetail.getSize().getNameSize();
            String productColor = productDetail.getColor().getNameColor();
            String productInfo = String.format("%s (Size: %s, Color: %s)", productName, productSize, productColor);

            table.addCell(String.valueOf(stt++));
            table.addCell(productInfo);
            table.addCell("VND");
            table.addCell(String.valueOf(detail.getQuantity()));
            table.addCell(currencyFormat.format(detail.getPrice()));
            table.addCell(currencyFormat.format((long) detail.getQuantity() * detail.getPrice()));
        }

        document.add(table);

        document.add(new Paragraph("Cộng tiền hàng: " + currencyFormat.format(invoice.getTotalInvoiceAmount())));
        int reducedVoucher = 0;
        if (invoice.getVoucher() != null) {
            reducedVoucher = invoice.getVoucher().getReducedValue();
        }
        int discountAmount = reducedVoucher + invoice.getCustomerPoints() * 1000;
        document.add(new Paragraph("Số Tiền Được Giảm (Điểm Quy Đổi + Tiền Giảm Voucher): " + currencyFormat.format(discountAmount)));
        document.add(new Paragraph("Tổng tiền thanh toán: " + currencyFormat.format(invoice.getTotalPayment())));

        Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        signatureTable.setWidth(UnitValue.createPercentValue(100));

        Cell buyerCell = new Cell()
                .add(new Paragraph("Người mua hàng").setFont(font));

        if (detailAccount != null) {
            buyerCell.add(new Paragraph("\n(Ký, ghi rõ họ tên)\n\n\n\n" + detailAccount.getFullName()).setFont(font));
        } else {
            buyerCell.add(new Paragraph("\n(Ký, ghi rõ họ tên)\n\n\n\n"));
        }

        buyerCell.setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER);

        Cell sellerCell = new Cell()
                .add(new Paragraph("Người bán hàng").setFont(font))
                .add(new Paragraph("\n(Ký, ghi rõ họ tên)\n\n\n\n" + invoice.getAccount().getFullName()).setFont(font))
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER);

        signatureTable.addCell(buyerCell);
        signatureTable.addCell(sellerCell);

        document.add(new Paragraph("\n\n"));
        document.add(signatureTable);

        document.close();
    }

    public Account checkMenuAdmin(Model model,
                                  HttpSession session,
                                  String titleWebsite) {
        model.addAttribute("titleWebsite", titleWebsite);
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return null;
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount == null) {
                return null;
            } else {
                if (detailAccount.getStatus() == 0) {
                    session.invalidate();
                    return null;
                } else {
                    model.addAttribute("profile", detailAccount);

                    LocalDateTime checkDate = LocalDateTime.now();
                    int hour = checkDate.getHour();
                    if (hour >= 5 && hour < 10) {
                        model.addAttribute("dates", "Buổi Sáng");
                    } else if (hour >= 10 && hour < 13) {
                        model.addAttribute("dates", "Buổi Trưa");
                    } else if (hour >= 13 && hour < 18) {
                        model.addAttribute("dates", "Buổi Chiều");
                    } else {
                        model.addAttribute("dates", "Buổi Tối");
                    }

                    Role detailRole = roleRepository.getRoleByEmail(email);
                    if (detailRole.getName().equals("ADMIN")) {
                        model.addAttribute("checkIndexAccount", true);
                        model.addAttribute("checkMenuAdmin", true);
                        model.addAttribute("addProfile", new Account());
                        model.addAttribute("addClient", new Account());
                    } else {
                        model.addAttribute("checkIndexAccount", false);
                        model.addAttribute("checkMenuAdmin", false);
                    }
                    return detailAccount;
                }
            }
        }
    }

    public Account checkMenuClient(Model model,
                                   HttpSession session,
                                   String titleWebsite) {
        model.addAttribute("titleWebsite", titleWebsite);
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return null;
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
            return detailAccount;
        }
    }

    public Role findRoleByAccount(HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        return roleRepository.getRoleByEmail(email);
    }
}