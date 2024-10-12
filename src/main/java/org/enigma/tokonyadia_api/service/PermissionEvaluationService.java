package org.enigma.tokonyadia_api.service;

public interface PermissionEvaluationService {
    boolean hasAccessToCustomer(String customerId, String userId);
}