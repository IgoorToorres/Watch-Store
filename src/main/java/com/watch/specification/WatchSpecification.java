package com.watch.specification;

import com.watch.dto.WatchFilterRequest;
import com.watch.entity.CaseMaterial;
import com.watch.entity.CrystalType;
import com.watch.entity.MovementType;
import com.watch.entity.Watch;
import org.springframework.data.jpa.domain.Specification;

public final class WatchSpecification {

    private WatchSpecification() {
    }

    public static Specification<Watch> withFilters(WatchFilterRequest filters) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (filters == null) {
                return predicate;
            }

            if (hasText(filters.search())) {
                String search = "%" + filters.search().trim().toLowerCase() + "%";

                var brandPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("brand")),
                        search
                );

                var modelPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("model")),
                        search
                );

                var referencePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("reference")),
                        search
                );

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.or(
                                brandPredicate,
                                modelPredicate,
                                referencePredicate
                        )
                );
            }

            if (hasText(filters.brand())) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("brand")),
                                filters.brand().trim().toLowerCase()
                        )
                );
            }

            if (hasText(filters.movementType())) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("movementType"),
                                MovementType.fromApi(filters.movementType())
                        )
                );
            }

            if (hasText(filters.caseMaterial())) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("caseMaterial"),
                                CaseMaterial.fromApi(filters.caseMaterial())
                        )
                );
            }

            if (hasText(filters.crystalType())) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("crystalType"),
                                CrystalType.fromApi(filters.crystalType())
                        )
                );
            }

            if (filters.waterResistanceMin() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("waterResistanceM"),
                                filters.waterResistanceMin()
                        )
                );
            }

            if (filters.waterResistanceMax() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("waterResistanceM"),
                                filters.waterResistanceMax()
                        )
                );
            }

            if (filters.priceMin() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("priceInCents"),
                                filters.priceMin()
                        )
                );
            }

            if (filters.priceMax() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("priceInCents"),
                                filters.priceMax()
                        )
                );
            }

            if (filters.diameterMin() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("diameterMm"),
                                filters.diameterMin()
                        )
                );
            }

            if (filters.diameterMax() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("diameterMm"),
                                filters.diameterMax()
                        )
                );
            }

            return predicate;
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}