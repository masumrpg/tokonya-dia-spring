package org.enigma.tokonyadia_api.service;

public interface PermissionEvaluationService {
    boolean hasAccessToCustomerAndSeller(String personId, String userId);
}