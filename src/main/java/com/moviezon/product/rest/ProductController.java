
package com.moviezon.product.rest;

import com.moviezon.product.repository.Product;
import com.moviezon.product.repository.ProductRepository;
import com.moviezon.product.rest.dto.ProductOverview;
import com.moviezon.product.rest.dto.ProductResponse;
import com.moviezon.product.rest.dto.ProductSearchResponse;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import static com.moviezon.util.RestUtility.*;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

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
    public ProductResponse product(@PathVariable("productId") Integer productId) {
        return new ProductResponse();
    }

    private ProductSearchResponse mapToSearchResponse(List<Product> products, String searchTerm, String baseURL) {
        List<ProductOverview> productOverviews = new ArrayList<ProductOverview>();
        for(Product product : products) {
            ProductOverview productOverview = mapper.map(product, ProductOverview.class);
            productOverview.add(linkTo(methodOn(ProductController.class).product(productOverview.getProductId())).withRel("product"));
            productOverview.add(new Link(baseURL + "/img/" + productOverview.getImageFilename(), "image"));
            productOverviews.add(productOverview);
        }
        ProductSearchResponse response = new ProductSearchResponse();
        response.add(linkTo(methodOn(ProductController.class).searchProducts(searchTerm, null)).withSelfRel());
        response.setProducts(productOverviews);
        return response;
    }

}
