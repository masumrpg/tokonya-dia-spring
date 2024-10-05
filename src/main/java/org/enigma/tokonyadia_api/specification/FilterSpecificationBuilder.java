package org.enigma.tokonyadia_api.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class FilterSpecificationBuilder<T> {

    private final List<Specification<T>> specifications = new ArrayList<>();
    private final List<Specification<T>> likeEqualSpecifications = new ArrayList<>();

    // Tambahkan kondisi LIKE dengan cek kosong internal
    public FilterSpecificationBuilder<T> withLike(String field, String value) {
        if (StringUtils.hasText(value)) {
            Specification<T> specification = (root, query, builder) ->
                    builder.like(builder.lower(root.get(field)), "%" + value.toLowerCase() + "%");
            likeEqualSpecifications.add(specification);
        }
        return this; // untuk mengembalikan object ini sehingga bisa digunakan method chaining
    }

    // Tambahkan kondisi kesetaraan dengan pemeriksaan nol internal
    public FilterSpecificationBuilder<T> withEqual(String field, Object value) {
        if (value != null) {
            Specification<T> specification = (root, query, builder) ->
                    builder.equal(root.get(field), value);
            likeEqualSpecifications.add(specification);
        }
        return this;
    }

    // Tambahkan kondisi lebih besar dari atau sama dengan dengan pemeriksaan nol internal
    public FilterSpecificationBuilder<T> withGreaterThanOrEqualTo(String field, Number value) {
        if (value != null) {
            Specification<T> specification = (root, query, builder) ->
                    builder.ge(root.get(field), value);
            specifications.add(specification);
        }
        return this;
    }

    // Tambahkan kondisi kurang dari atau sama dengan dengan pemeriksaan nol internal
    public FilterSpecificationBuilder<T> withLessThanOrEqualTo(String field, Number value) {
        if (value != null) {
            Specification<T> specification = (root, query, builder) ->
                    builder.le(root.get(field), value);
            specifications.add(specification);
        }
        return this;
    }

    // Bangun Spesifikasi dengan menggabungkan semua predikat yang ditambahkan
    public Specification<T> build() {
        return (root, query, builder) -> {
            List<Predicate> likeEqualPredicates = new ArrayList<>();
            for (Specification<T> spec : likeEqualSpecifications) {
                likeEqualPredicates.add(spec.toPredicate(root, query, builder));
            }

            List<Predicate> predicates = new ArrayList<>();
            for (Specification<T> spec : specifications) {
                predicates.add(spec.toPredicate(root, query, builder));
            }

            if (predicates.isEmpty() && likeEqualPredicates.isEmpty()) {
                return builder.conjunction();
            }

            if (predicates.isEmpty()) {
                return builder.or(likeEqualPredicates.toArray(new Predicate[0]));
            }

            predicates.add(likeEqualSpecifications.get(0).toPredicate(root, query, builder));

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}