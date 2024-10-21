package org.enigma.tokonyadia_api.client;

import org.enigma.tokonyadia_api.config.FeignClientConfig;
import org.enigma.tokonyadia_api.dto.request.MidtransPaymentRequest;
import org.enigma.tokonyadia_api.dto.response.MidtransSnapResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "midtrans", url = "${midtrans.app.url}", configuration = FeignClientConfig.class)
public interface MidtransAppClient {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/snap/v1/transactions"
    )
    MidtransSnapResponse createSnapTransaction(
            @RequestBody MidtransPaymentRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String headerAuthorization
    );
}