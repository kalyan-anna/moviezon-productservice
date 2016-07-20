package com.moviezon.product.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearchResponse extends ResourceSupport {

    private List<ProductOverview> products;

    public List<ProductOverview> getProducts() {
        return products;
    }

    public void setProducts(List<ProductOverview> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
