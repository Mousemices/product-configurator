package com.festo.product_configurator.specification;

import com.festo.product_configurator.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public final class ProductSpecification {

    private ProductSpecification() {}

    public static Specification<Product>
    hasCategoryIgnoreCase(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(
                                root.get("category")
                        ),
                        category.toLowerCase(Locale.ROOT)
                );
    }

    public static Specification<Product>
    nameContainsIgnoreCase(String search) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("name")
                        ),
                        "%"
                                + search.toLowerCase(Locale.ROOT)
                                + "%"
                );
    }

    public static Specification<Product>
    priceLessThanEqual(Double maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.<Double>get("price"),
                        maxPrice
                );
    }
}
