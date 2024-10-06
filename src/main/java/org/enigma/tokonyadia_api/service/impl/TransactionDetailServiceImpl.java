package org.enigma.tokonyadia_api.service.impl;

import lombok.AllArgsConstructor;
import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.Transaction;
import org.enigma.tokonyadia_api.entity.TransactionDetail;
import org.enigma.tokonyadia_api.repository.TransactionDetailRepository;
import org.enigma.tokonyadia_api.service.ProductService;
import org.enigma.tokonyadia_api.service.TransactionDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class TransactionDetailServiceImpl implements TransactionDetailService {
    private final ProductService productService;
    private final TransactionDetailRepository transactionDetailRepository;

    @Override
    public List<TransactionDetail> create(Transaction transaction, List<TransactionDetailRequest> request) {
        List<TransactionDetail> transactionDetailsList = new ArrayList<>();

        for (TransactionDetailRequest transactionsDetail : request) {
            // find product untuk mengecek stock product
            Product product = productService.getOneById(transactionsDetail.getProductId());
            if (product.getStock() < transactionsDetail.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid quantity");
            }

            // update product
            productService.update(
                    product.getId(), ProductRequest.builder()
                            .name(product.getName())
                            .description(product.getDescription())
                            .price(product.getPrice())
                            .stock(product.getStock() - transactionsDetail.getQuantity())
                            .storeId(product.getStore().getId())
                            .build()
            );

            TransactionDetail transactionDetail = TransactionDetail.builder()
                    .transaction(transaction)
                    .product(product)
                    .quantity(transactionsDetail.getQuantity())
                    .price(product.getPrice())
                    .build();
            transactionDetailsList.add(transactionDetail);
        }
        transactionDetailRepository.saveAllAndFlush(transactionDetailsList);
        return transactionDetailsList;
    }

    @Override
    public TransactionDetail getById(String id) {
        return null;
    }

    @Override
    public List<TransactionDetail> getAll() {
        return List.of();
    }
}
