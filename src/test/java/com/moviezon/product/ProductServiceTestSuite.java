package com.moviezon.product;

import com.moviezon.product.rest.spec.ProctSpec;
import com.moviezon.product.rest.spec.ProductSearchSpec;
import com.moviezon.product.rest.test.HttpMethodTest;
import com.moviezon.product.rest.test.ProductSearchHateoasTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ProductSearchSpec.class,
        HttpMethodTest.class,
        ProductSearchHateoasTest.class,
        ProctSpec.class
})
public class ProductServiceTestSuite {
}
