package com.datn.datn_mangostore.config;

import com.datn.datn_mangostore.bean.*;
import com.datn.datn_mangostore.reponse.PriceRange;
import com.datn.datn_mangostore.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public Gender(AccountRepository accountRepository,
                  JavaMailSender mailSender, InvoiceRepository invoiceRepository, ProductDetailRepository productDetailRepository, SizeRepository sizeRepository, ColorRepository colorRepository, ShoppingCartDetailRepository shoppingCartDetailRepository, ShoppingCartRepository shoppingCartRepository) {
        this.accountRepository = accountRepository;
        this.mailSender = mailSender;
        this.invoiceRepository = invoiceRepository;
        this.productDetailRepository = productDetailRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
        this.shoppingCartRepository = shoppingCartRepository;
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

    public Map<String, Integer> countProductsBySize() {
        List<Size> sizes = sizeRepository.findAll();
        Map<String, Integer> productDetailCountBySize = new HashMap<>();
        for (Size size : sizes) {
            Integer count = productDetailRepository.countProductDetailBySize(size.getId());
            productDetailCountBySize.put(size.getNameSize(), count);
        }
        return productDetailCountBySize;
    }

    public Map<String, Integer> countProductsByColor() {
        List<Color> colors = colorRepository.findAll();
        Map<String, Integer> productDetailCountByColor = new HashMap<>();
        for (Color color : colors) {
            Integer count = productDetailRepository.countProductDetailByColor(color.getId());
            productDetailCountByColor.put(color.getNameColor(), count);
        }
        return productDetailCountByColor;
    }

    public void updateTotalShoppingCart(ShoppingCart shoppingCart) {
        List<ShoppingCartDetail> shoppingCartDetails = shoppingCartDetailRepository.getAllShoppingCart(shoppingCart.getId());
        Integer total = shoppingCartDetails.stream()
                .mapToInt(ShoppingCartDetail::getCapitalSum)
                .sum();
        shoppingCart.setTotalShoppingCart(total);
        shoppingCartRepository.save(shoppingCart);
    }
}
