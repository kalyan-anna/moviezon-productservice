
package com.moviezon.product.rest;

import com.moviezon.product.exception.ProductNotFoundException;
import com.moviezon.product.repository.Product;
import com.moviezon.product.repository.ProductRepository;
import com.moviezon.product.rest.dto.ProductDetailResponse;
import com.moviezon.product.rest.dto.ProductOverview;
import com.moviezon.product.rest.dto.ProductSearchResponse;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import static com.moviezon.util.RestUtility.*;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:5000")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired private ProductRepository productRepository;

    @Autowired private Mapper mapper;

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ProductSearchResponse searchProducts(@RequestParam(name = "searchTerm", required = false)String searchTerm, HttpServletRequest request) {
        if(isEmpty(searchTerm)) {
            return mapToSearchResponse(productRepository.findAll(), searchTerm, getBaseUrl(request));
        } else {
            return mapToSearchResponse(productRepository.findByTitleContaining(searchTerm), searchTerm, getBaseUrl(request));
        }
    }


    @RequestMapping(value = "/{productId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ProductDetailResponse product(@PathVariable("productId") Integer productId, HttpServletRequest request) {
        log.info("request for productId {}",  productId);
        Product product = productRepository.findByProductId(productId);
        if(product == null) {
            throw new ProductNotFoundException();
        }
        return mapProductResponse(product, getBaseUrl(request));
    }

    private ProductSearchResponse mapToSearchResponse(List<Product> products, String searchTerm, String baseURL) {
        List<ProductOverview> productOverviews = new ArrayList<ProductOverview>();
        for(Product product : products) {
            ProductOverview productOverview = mapper.map(product, ProductOverview.class);
            productOverview.add(linkTo(methodOn(ProductController.class).product(productOverview.getProductId(), null)).withRel("product"));
            productOverview.add(new Link(baseURL + "/img/" + productOverview.getImageFilename(), "image"));
            productOverviews.add(productOverview);
        }
        ProductSearchResponse response = new ProductSearchResponse();
        response.add(linkTo(methodOn(ProductController.class).searchProducts(searchTerm, null)).withSelfRel());
        response.setProducts(productOverviews);
        return response;
    }

    private ProductDetailResponse mapProductResponse(Product product, String baseURL) {
        ProductDetailResponse response = mapper.map(product, ProductDetailResponse.class);
        response.add(linkTo(methodOn(ProductController.class).product(product.getProductId(), null)).withSelfRel());
        response.add(new Link(baseURL + "/img/" + product.getImageFilename(), "image"));
        return  response;
    }
}
