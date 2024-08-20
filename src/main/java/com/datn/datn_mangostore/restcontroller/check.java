package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.service.ColorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class check {
    private final ColorService colorService;

    public check(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping("/colorsExistCreat/{name}")
    public ResponseEntity<Integer> checkColorExistence(@PathVariable String name) {
        try {
            Integer result = colorService.findByColorCreateExit(name);
            if (result == 1) {
                return ResponseEntity.ok(1);
            } else if (result == 2) {
                return ResponseEntity.ok(2);
            } else {
                return ResponseEntity.ok(0);
            }
        } catch (Exception e) {
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
                return ResponseEntity.ok(1);
            } else if (result == 2) {
                return ResponseEntity.ok(2);
            } else if (result == 3) {
                return ResponseEntity.ok(3);
            } else {
                return ResponseEntity.ok(0);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
