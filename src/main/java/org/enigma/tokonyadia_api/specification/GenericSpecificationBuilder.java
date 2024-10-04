package org.enigma.tokonyadia_api.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class GenericSpecificationBuilder<T> {

    private final List<Specification<T>> specifications = new ArrayList<>();

    // Tambahkan kondisi kesetaraan dengan pemeriksaan nol internal
    public GenericSpecificationBuilder<T> withEqual(String field, Object value) {
        if (value != null) {
            specifications.add((root, query, builder) ->
                    builder.equal(root.get(field), value)
            );
        }
        return this;
    }

    // Tambahkan kondisi LIKE dengan cek kosong internal
    public GenericSpecificationBuilder<T> withLike(String field, String value) {
        if (StringUtils.hasText(value)) {
            specifications.add((root, query, builder) ->
                    builder.like(builder.lower(root.get(field)), "%" + value.toLowerCase() + "%")
            );
        }
        return this;
    }

    // Tambahkan kondisi lebih besar dari atau sama dengan dengan pemeriksaan nol internal
    public GenericSpecificationBuilder<T> withGreaterThanOrEqualTo(String field, Number value) {
        if (value != null) {
            specifications.add((root, query, builder) ->
                    builder.ge(root.get(field), value)
            );
        }
        return this;
    }

    // Tambahkan kondisi kurang dari atau sama dengan dengan pemeriksaan nol internal
    public GenericSpecificationBuilder<T> withLessThanOrEqualTo(String field, Number value) {
        if (value != null) {
            specifications.add((root, query, builder) ->
                    builder.le(root.get(field), value)
            );
        }
        return this;
    }

    // Bangun Spesifikasi dengan menggabungkan semua predikat yang ditambahkan
    public Specification<T> build() {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Specification<T> spec : specifications) {
                predicates.add(spec.toPredicate(root, query, builder));
            }
            return predicates.isEmpty() ? builder.conjunction() : builder.or(predicates.toArray(new Predicate[0]));
        };
    }
}