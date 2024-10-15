package org.enigma.tokonyadia_api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InvoiceNumberGenerator {
    public static String generateInvoiceNumber(String storeCode, String invoiceId) {
        String prefix = "INV";

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = currentDate.format(formatter);

        String uniqueNumber = String.format("%s", invoiceId.toUpperCase());

        return String.format("%s/%s/%s/%s", prefix, datePart, storeCode.toUpperCase(), uniqueNumber);
    }
}
