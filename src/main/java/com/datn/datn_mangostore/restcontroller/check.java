package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class check {
    @Autowired
    private ColorService colorService;

    @GetMapping("/colorsExistCreat/{name}")
    public ResponseEntity<Integer> checkColorExistence(@PathVariable String name) {
        try {
            Integer result = colorService.findByColorCreateExit(name);
            if (result == 1) {
                return ResponseEntity.ok(1); // Màu tồn tại
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Màu không tồn tại
            } else {
                return ResponseEntity.ok(0); // Trường hợp khác (nếu cần thiết)
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ và trả về lỗi 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/colorsExistUpdate/{name}")
    public ResponseEntity<Integer> checkColorExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeColor
    ) {

        try {
            Integer result = colorService.findByColorUpdateExit(name, codeColor);
            if (result == 1) {
                return ResponseEntity.ok(1); // Màu tồn tại và codeColor trùng khớp (Update)
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Chỉ có name tồn tại, nhưng codeColor khác (Name đã tồn tại)
            } else if (result == 3) {
                return ResponseEntity.ok(3); // Name chưa tồn tại (Create mới)
            } else {
                return ResponseEntity.ok(0); // Trường hợp khác (nếu cần thiết)
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ và trả về lỗi 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
