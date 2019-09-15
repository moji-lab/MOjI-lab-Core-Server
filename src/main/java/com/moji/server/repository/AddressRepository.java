package com.moji.server.repository;

import com.moji.server.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<List<Address>> findAllByPlaceContaining(final String place);
}
