package com.moviezon.product.rest.test;

import com.moviezon.ProductServiceApplication;
import com.moviezon.product.ProductTestConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({ProductServiceApplication.class, ProductTestConfiguration.class})
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("test")
public class ProductSearchHateoasTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void searchResponseShouldHaveSelfReferencingLink() throws Exception {
        String searchPath = "/product/search?searchTerm=The";
        mockMvc.perform(get(searchPath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("products", hasSize(greaterThan(0))))
                .andExpect(jsonPath("_links.self.href", not(isEmptyOrNullString())))
                .andExpect(jsonPath("_links.self.href", containsString(searchPath)));
    }

    @Test
    public void eachSearchResultShouldHaveLinkReferencingToProductAndImage() throws Exception {
        mockMvc.perform(get("/product/search?searchTerm=The"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("products", hasSize(greaterThan(0))))
                .andExpect(jsonPath("products[0]._links.product.href", containsString("/product/")))
                .andExpect(jsonPath("products[0]._links.image.href", not(isEmptyOrNullString())));
    }
}
