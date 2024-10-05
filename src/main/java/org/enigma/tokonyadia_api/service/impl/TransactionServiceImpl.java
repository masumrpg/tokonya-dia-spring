package org.enigma.tokonyadia_api.service.impl;

import jakarta.transaction.Transactional;
import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import org.enigma.tokonyadia_api.dto.request.TransactionRequest;
import org.enigma.tokonyadia_api.dto.response.CustomerResponse;
import org.enigma.tokonyadia_api.dto.response.ProductResponse;
import org.enigma.tokonyadia_api.dto.response.TransactionDetailResponse;
import org.enigma.tokonyadia_api.dto.response.TransactionResponse;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.Transaction;
import org.enigma.tokonyadia_api.entity.TransactionDetail;
import org.enigma.tokonyadia_api.repository.TransactionDetailRepository;
import org.enigma.tokonyadia_api.repository.TransactionRepository;
import org.enigma.tokonyadia_api.service.CustomerService;
import org.enigma.tokonyadia_api.service.ProductService;
import org.enigma.tokonyadia_api.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionDetailRepository transactionDetailRepository, ProductService productService, CustomerService customerService) {
        this.transactionRepository = transactionRepository;
        this.transactionDetailRepository = transactionDetailRepository;
        this.productService = productService;
        this.customerService = customerService;
    }

    // TODO make this
    @Transactional
    @Override
    public void create(TransactionRequest request) {
        List<TransactionDetail> transactionDetailsList = new ArrayList<>();

        // verif customer
        CustomerResponse customerResponse = customerService.getById(request.getCustomerId());
        Customer customer = toCustomer(customerResponse);

        Transaction newTransaction = Transaction.builder()
                .customer(customer)
                .build();

        transactionRepository.saveAndFlush(newTransaction);

        // save transaction details
        for (TransactionDetailRequest transactionsDetail : request.getTransactionsDetails()) {
            // find product untuk mengecek stock product
            ProductResponse productResponse = productService.getById(transactionsDetail.getProductId());
            Product product = toProduct(productResponse);
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
                    .transaction(newTransaction)
                    .product(product)
                    .quantity(transactionsDetail.getQuantity())
                    .price(product.getPrice())
                    .build();
            transactionDetailsList.add(transactionDetail);
        }
        transactionDetailRepository.saveAllAndFlush(transactionDetailsList);
    }

    @Override
    public TransactionResponse getById(String id) {
        List<TransactionDetailResponse> transactionResponseList = new ArrayList<>();
        Optional<Transaction> byId = transactionRepository.findById(id);
        if (byId.isPresent()) {
            Transaction transaction = byId.get();
            for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
                transactionResponseList.add(toTransactionDetailResponse(transactionDetail));
            }
            return toTransactionResponse(transaction, transactionResponseList);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
    }

    @Override
    public List<Transaction> getAll() {
        return List.of();
    }

    // =================================================================================================================

    private Customer toCustomer(CustomerResponse customerResponse) {
        return Customer.builder()
                .id(customerResponse.getId())
                .name(customerResponse.getName())
                .address(customerResponse.getAddress())
                .phoneNumber(customerResponse.getPhoneNumber())
                .email(customerResponse.getEmail())
                .build();
    }

    private Product toProduct(ProductResponse productResponse) {
        return Product.builder()
                .id(productResponse.getId())
                .name(productResponse.getName())
                .description(productResponse.getDescription())
                .price(productResponse.getPrice())
                .stock(productResponse.getStock())
                .store(productResponse.getStore())
                .build();
    }

    private TransactionDetailResponse toTransactionDetailResponse(TransactionDetail transactionDetail) {
        return TransactionDetailResponse.builder()
                .name(transactionDetail.getProduct().getName())
                .price(transactionDetail.getProduct().getPrice())
                .quantity(transactionDetail.getQuantity())
                .build();
    }

    private TransactionResponse toTransactionResponse(Transaction transaction, List<TransactionDetailResponse> transactionDetailResponse) {
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
