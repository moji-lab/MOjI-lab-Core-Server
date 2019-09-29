package com.moji.server.service;

import com.moji.server.domain.Address;
import com.moji.server.model.DefaultRes;
import com.moji.server.repository.AddressRepository;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final TourApiService tourApiService;

    // 생성자 의존성 주입
    public AddressService(final AddressRepository addressRepository,
                          final TourApiService tourApiService){
        this.addressRepository = addressRepository;
        this.tourApiService = tourApiService;
    }

    // 기록하기 핵심 주소 저장

    public DefaultRes saveBoardAddress(final Address address){
        try{
            if(tourApiService.getSubAddressByGoogleMapGeoCodingApi(address.getLat(),address.getLng()).isPresent()){
                String subAddress = tourApiService.getSubAddressByGoogleMapGeoCodingApi(address.getLat(),address.getLng()).get();
                address.setSubAddress(subAddress);
            }
            else{
                return DefaultRes.res(StatusCode.NOT_FOUND, "해당 위도, 경도와 일치하는 주소가 없습니다.");
            }
            addressRepository.save(address);
            return DefaultRes.res(StatusCode.CREATED, "핵심 주소가 등록 되었습니다.");
        }
        catch(Exception e){
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, "데이터베이스 에러");
        }
    }

}
