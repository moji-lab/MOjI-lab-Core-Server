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

    // 생성자 의존성 주입
    public AddressService(final AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    // 기록하기 핵심 주소 저장
    public DefaultRes saveBoardAddress(final Address address){
        try{
            addressRepository.save(address);
            return DefaultRes.res(StatusCode.CREATED, "핵심 주소가 등록 되었습니다.");
        }
        catch(Exception e){
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, "핵심 주소 등록에 실패하였습니다.");
        }
    }

    // 기록하기 핵심 주소 조회
    public DefaultRes getBoardAddresses(final String keyword){
        Optional<List<Address>> addressList = addressRepository.findAllByPlaceContaining(keyword);
        if(addressList.isPresent()){
            if(addressList.get().size() == 0){ return DefaultRes.res(StatusCode.NOT_FOUND, "핵심 주소를 찾을 수 없습니다."); }
        }
        return addressList.map(value -> DefaultRes.res(StatusCode.OK, "핵심 주소 조회 성공", value)).orElseGet(() -> DefaultRes.res(StatusCode.DB_ERROR, "데이터베이스 에러"));
    }
}
