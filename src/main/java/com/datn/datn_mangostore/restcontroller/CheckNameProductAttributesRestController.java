package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/mangostore/admin/")
public class CheckNameProductAttributesRestController {
    private final ColorService colorService;
    private final ProductService productService;
    private final MaterialService materialService;
    private final SizeService sizeService;
    private final CategoryService categoryService;
    private final OriginService originService;

    public CheckNameProductAttributesRestController(ColorService colorService,
                                                    ProductService productService,
                                                    MaterialService materialService,
                                                    SizeService sizeService,
                                                    CategoryService categoryService,
                                                    OriginService originService) {
        this.colorService = colorService;
        this.productService = productService;
        this.materialService = materialService;
        this.sizeService = sizeService;
        this.categoryService = categoryService;
        this.originService = originService;
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


    @GetMapping("/materialsExistCreate/{name}")
    public ResponseEntity<Integer> checkMaterialExistence(@PathVariable String name) {
        try {
            Integer result = materialService.findByMaterialCreateExit(name);
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

    @GetMapping("/materialsExistUpdate/{name}")
    public ResponseEntity<Integer> checkMaterialExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeMaterial
    ) {
        try {
            Integer result = materialService.findByMaterialUpdateExit(name, codeMaterial);
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

    @GetMapping("/sizesExistCreate/{name}")
    public ResponseEntity<Integer> checkSizeExistence(@PathVariable String name) {
        try {
            Integer result = sizeService.findBySizeCreateExit(name);
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

    @GetMapping("/sizesExistUpdate/{name}")
    public ResponseEntity<Integer> checkSizeExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeSize
    ) {
        try {
            Integer result = sizeService.findBySizeUpdateExit(name, codeSize);
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

    @GetMapping("/categoriesExistCreate/{name}")
    public ResponseEntity<Integer> checkCategoryExistence(@PathVariable String name) {
        try {
            Integer result = categoryService.findByCategoryCreateExit(name);
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

    @GetMapping("/categoriesExistUpdate/{name}")
    public ResponseEntity<Integer> checkCategoryExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeCategory
    ) {
        try {
            Integer result = categoryService.findByCategoryUpdateExit(name, codeCategory);
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

    @GetMapping("/originsExistCreate/{name}")
    public ResponseEntity<Integer> checkOriginExistence(@PathVariable String name) {
        try {
            Integer result = originService.findByOriginCreateExit(name);
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

    @GetMapping("/originsExistUpdate/{name}")
    public ResponseEntity<Integer> checkOriginExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeOrigin
    ) {
        try {
            Integer result = originService.findByOriginUpdateExit(name, codeOrigin);
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

    @GetMapping("/productsExistCreate/{name}")
    public ResponseEntity<Integer> checkProductExistence(@PathVariable String name) {
        try {
            Integer result = productService.findByProductCreateExit(name);
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

    @GetMapping("/productsExistUpdate/{name}")
    public ResponseEntity<Integer> checkProductExistence2(
            @PathVariable String name,
            @RequestParam(required = false) String codeProduct
    ) {
        try {
            Integer result = productService.findByProductUpdateExit(name, codeProduct);
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
