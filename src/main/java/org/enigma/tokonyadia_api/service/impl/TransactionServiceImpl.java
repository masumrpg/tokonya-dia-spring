package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.dto.response.ProductResponse;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.Transaction;
import org.enigma.tokonyadia_api.entity.TransactionDetail;
import org.enigma.tokonyadia_api.repository.TransactionRepository;
import org.enigma.tokonyadia_api.service.ProductService;
import org.enigma.tokonyadia_api.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ProductService productService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, ProductService productService) {
        this.transactionRepository = transactionRepository;
        this.productService = productService;
    }

    // TODO make this
//    public Transaction createTransaction(Transaction transaction, List<TransactionDetail> details) {
//        // Validasi produk dan stok
//        for (TransactionDetail detail : details) {
//            ProductResponse productResponse = productService.getById(detail.getId());
//            Product.builder()
//                    .id(productResponse.getId())
//                    .build();
//
//            if (product.getStock() < detail.getQuantity()) {
//                throw new RuntimeException("Stock not sufficient");
//            }
//            product.setStock(product.getStock() - detail.getQuantity());
//            productRepository.save(product);
//        }

        // Simpan transaksi
//        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
//        transaction.setTransactionDetails(details);
//        return transactionRepository.save(transaction);
//    }

    @Override
    public Transaction create(Transaction transaction) {

        return null;
    }

    @Override
    public Transaction getById(String id) {
        return null;
    }

    @Override
    public Transaction update(String id, Transaction transaction) {
        return null;
    }

    @Override
    public String delete(String id) {
        return "";
    }

    @Override
    public List<Transaction> getAll() {
        return List.of();
    }
}
