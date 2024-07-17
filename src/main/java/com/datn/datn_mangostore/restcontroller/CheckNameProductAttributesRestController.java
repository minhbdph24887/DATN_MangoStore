package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/mangostore/admin/")
public class CheckNameProductAttributesRestController {
// vinh3

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

    @Autowired
    private MaterialService materialService;

    @GetMapping("/materialsExistCreate/{name}")
    public ResponseEntity<Integer> checkMaterialExistence(@PathVariable String name) {
        try {
            Integer result = materialService.findByMaterialCreateExit(name);
            if (result == 1) {
                return ResponseEntity.ok(1); // Chất liệu tồn tại
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Chất liệu không tồn tại
            } else {
                return ResponseEntity.ok(0); // Trường hợp khác (nếu cần thiết)
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ và trả về lỗi 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/materialsExistUpdate/{name}")
    public ResponseEntity<Integer> checkMaterialExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeMaterial
    ) {
        try {
            Integer result = materialService.findByMaterialUpdateExit(name, codeMaterial);
            if (result == 1) {
                return ResponseEntity.ok(1); // Chất liệu tồn tại và codeMaterial trùng khớp (Update)
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Chỉ có name tồn tại, nhưng codeMaterial khác (Name đã tồn tại)
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


    @Autowired
    private SizeService sizeService;

    @GetMapping("/sizesExistCreate/{name}")
    public ResponseEntity<Integer> checkSizeExistence(@PathVariable String name) {
        try {
            Integer result = sizeService.findBySizeCreateExit(name);
            if (result == 1) {
                return ResponseEntity.ok(1); // Kích thước tồn tại
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Kích thước không tồn tại
            } else {
                return ResponseEntity.ok(0); // Trường hợp khác (nếu cần thiết)
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ và trả về lỗi 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sizesExistUpdate/{name}")
    public ResponseEntity<Integer> checkSizeExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeSize
    ) {
        try {
            Integer result = sizeService.findBySizeUpdateExit(name, codeSize);
            if (result == 1) {
                return ResponseEntity.ok(1); // Kích thước tồn tại và codeSize trùng khớp (Update)
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Chỉ có name tồn tại, nhưng codeSize khác (Name đã tồn tại)
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

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categoriesExistCreate/{name}")
    public ResponseEntity<Integer> checkCategoryExistence(@PathVariable String name) {
        try {
            Integer result = categoryService.findByCategoryCreateExit(name);
            if (result == 1) {
                return ResponseEntity.ok(1); // Danh mục tồn tại
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Danh mục không tồn tại
            } else {
                return ResponseEntity.ok(0); // Trường hợp khác (nếu cần thiết)
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ và trả về lỗi 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/categoriesExistUpdate/{name}")
    public ResponseEntity<Integer> checkCategoryExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeCategory
    ) {
        try {
            Integer result = categoryService.findByCategoryUpdateExit(name, codeCategory);
            if (result == 1) {
                return ResponseEntity.ok(1); // Danh mục tồn tại và codeCategory trùng khớp (Update)
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Chỉ có name tồn tại, nhưng codeCategory khác (Name đã tồn tại)
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

    @Autowired
    private OriginService originService;

    @GetMapping("/originsExistCreate/{name}")
    public ResponseEntity<Integer> checkOriginExistence(@PathVariable String name) {
        try {
            Integer result = originService.findByOriginCreateExit(name);
            if (result == 1) {
                return ResponseEntity.ok(1); // Origin exists
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Origin does not exist
            } else {
                return ResponseEntity.ok(0); // Other cases (if needed)
            }
        } catch (Exception e) {
            // Handle exception and return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/originsExistUpdate/{name}")
    public ResponseEntity<Integer> checkOriginExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeOrigin
    ) {
        try {
            Integer result = originService.findByOriginUpdateExit(name, codeOrigin);
            if (result == 1) {
                return ResponseEntity.ok(1); // Origin exists and codeOrigin matches (Update)
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Only name exists, but codeOrigin is different (Name already exists)
            } else if (result == 3) {
                return ResponseEntity.ok(3); // Name does not exist (Create new)
            } else {
                return ResponseEntity.ok(0); // Other cases (if needed)
            }
        } catch (Exception e) {
            // Handle exception and return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Autowired
    private ProductService productService;

    @GetMapping("/productsExistCreate/{name}")
    public ResponseEntity<Integer> checkProductExistence(@PathVariable String name) {
        try {
            Integer result = productService.findByProductCreateExit(name);
            if (result == 1) {
                return ResponseEntity.ok(1); // Product exists
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Product does not exist
            } else {
                return ResponseEntity.ok(0); // Other cases (if needed)
            }
        } catch (Exception e) {
            // Handle exception and return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/productsExistUpdate/{name}")
    public ResponseEntity<Integer> checkProductExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeProduct
    ) {
        try {
            Integer result = productService.findByProductUpdateExit(name, codeProduct);
            if (result == 1) {
                return ResponseEntity.ok(1); // Product exists and codeProduct matches (Update)
            } else if (result == 2) {
                return ResponseEntity.ok(2); // Only name exists, but codeProduct is different (Name already exists)
            } else if (result == 3) {
                return ResponseEntity.ok(3); // Name does not exist (Create new)
            } else {
                return ResponseEntity.ok(0); // Other cases (if needed)
            }
        } catch (Exception e) {
            // Handle exception and return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
