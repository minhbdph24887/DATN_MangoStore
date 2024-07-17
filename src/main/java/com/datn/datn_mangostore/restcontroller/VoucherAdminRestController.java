package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.NameRankRequest;
import com.datn.datn_mangostore.service.RankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/admin/")
public class VoucherAdminRestController {
    private final RankService rankService;

    public VoucherAdminRestController(RankService rankService) {
        this.rankService = rankService;
    }

    @PostMapping(value = "rank/check-add")
    public ResponseEntity<String> checkAddRank(@RequestBody NameRankRequest request){
        return rankService.checkAddRank(request);
    }

    @PostMapping(value = "rank/check-update")
    public ResponseEntity<String> checkUpdateRank(@RequestBody NameRankRequest request){
        return rankService.checkUpdateRank(request);
    }
}
