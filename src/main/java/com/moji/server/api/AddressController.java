package com.moji.server.api;

import com.moji.server.domain.Address;
import com.moji.server.model.DefaultRes;
import com.moji.server.service.AddressService;
import com.moji.server.service.TourApiService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class AddressController {

    private final AddressService addressService;
    private final TourApiService tourApiService;

    public AddressController(final AddressService addressService,
                             final TourApiService tourApiServices){
        this.addressService = addressService;
        this.tourApiService = tourApiServices;
    }

    @PostMapping("/addresses")
    public ResponseEntity<DefaultRes> saveBoardAddress(@RequestBody Address address) {
        try {
            return new ResponseEntity<>(addressService.saveBoardAddress(address), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 핵심 주소 검색(Tour API로 검색)

    @GetMapping("/addresses")
    public ResponseEntity<DefaultRes> getTourApiAddresses(@RequestParam("keyword") final String keyword) {
        try {
            return new ResponseEntity<>(tourApiService.getAddressByKeyword(keyword), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
