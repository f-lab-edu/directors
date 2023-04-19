package com.directors.presentation.region.response;

import com.directors.domain.region.Address;

import java.util.List;

public record NearestAddressResponse(
        List<Address> address
) {
}
