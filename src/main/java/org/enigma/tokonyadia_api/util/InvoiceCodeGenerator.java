package org.enigma.tokonyadia_api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InvoiceCodeGenerator {
    public static String generateInvoiceCode(String storeName, String orderId) {
        String prefix = "INV";

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = currentDate.format(formatter);

        String uniqueNumber = String.format("%s", orderId.toUpperCase());

        return String.format("%s/%s/%s/%s", prefix, datePart, storeName.toUpperCase().replace(" ", "-"), uniqueNumber.substring(0, 8));
    }
}
