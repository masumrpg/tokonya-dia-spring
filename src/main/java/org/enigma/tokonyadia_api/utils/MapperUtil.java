package org.enigma.tokonyadia_api.utils;

import org.enigma.tokonyadia_api.dto.response.*;
import org.enigma.tokonyadia_api.entity.*;

import java.util.List;

public class MapperUtil {
    public static CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .store(product.getStore())
                .build();
    }

    public static StoreResponse toStoreResponse(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .siup(store.getSiup())
                .address(store.getAddress())
                .phoneNumber(store.getPhoneNumber())
                .build();
    }

    public static TransactionDetailResponse toTransactionDetailResponse(TransactionDetail transactionDetail) {
        return TransactionDetailResponse.builder()
                .name(transactionDetail.getProduct().getName())
                .price(transactionDetail.getProduct().getPrice())
                .quantity(transactionDetail.getQuantity())
                .build();
    }

    public static TransactionResponse toTransactionResponse(Transaction transaction, List<TransactionDetailResponse> transactionDetailResponse) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .customerName(transaction.getCustomer().getName())
                .customerPhoneNumber(transaction.getCustomer().getPhoneNumber())
                .storeName(transaction.getTransactionDetails().get(0).getProduct().getStore().getName())
                .transactionDate(transaction.getTransactionDate())
                .transactionsDetails(transactionDetailResponse)
                .build();
    }
}
