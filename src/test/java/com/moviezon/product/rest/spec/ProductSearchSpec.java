package com.moviezon.product.rest.spec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviezon.ProductServiceApplication;
import com.moviezon.product.ProductTestConfiguration;
import com.moviezon.product.rest.dto.ProductSearchResponse;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({ProductServiceApplication.class, ProductTestConfiguration.class})
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("test")
public class ProductSearchSpec {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext webApplicationContext;

    @Autowired private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void withNoSearchTerm_ShouldFetchAllProducts() throws Exception {
        Integer expectedTotalCount = jdbcTemplate.queryForObject("select count(*) from product", Integer.class);
        mockMvc.perform(get("/product/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("products", hasSize(expectedTotalCount)));
    }

    @Test
    public void emptySearchTerm_ShouldFetchAllProducts() throws Exception {
        Integer expectedTotalCount = jdbcTemplate.queryForObject("select count(*) from product", Integer.class);
        mockMvc.perform(get("/product/search?searchTerm="))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("products", hasSize(expectedTotalCount)));
    }

    @Test
    public void withValidSearchTeam_ShouldFetchProductsWithMatchingTitle() throws Exception {
        String searchTerm = "Revenant";
        Integer expectedMatchingCount = jdbcTemplate.queryForObject("select count(*) from product where title like ?", new Object[]{"%" + searchTerm + "%"}, Integer.class);
        mockMvc.perform(get("/product/search?searchTerm=" + searchTerm))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("products", hasSize(expectedMatchingCount)));
    }

    @Test
    public void withInvalidSearchTeam_ShouldFetchNoProducts() throws Exception {
        String searchTerm = "dummy";
        Integer expectedCount = jdbcTemplate.queryForObject("select count(*) from product where title like ?",  new Object[]{"%" + searchTerm + "%"}, Integer.class);
        mockMvc.perform(get("/product/search?searchTerm=" + searchTerm))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("products", hasSize(0)));
    }

    @Test
    public void allFieldsInSearchResponseShouldHaveValue() throws Exception {
        String response = mockMvc.perform(get("/product/search"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ProductSearchResponse responseObj = new ObjectMapper().readValue(response, ProductSearchResponse.class);
        assertThat(responseObj.getProducts(), (everyItem(hasProperty("productId", not(isEmptyOrNullString())))));
        assertThat(responseObj.getProducts(), (everyItem(hasProperty("title", not(isEmptyOrNullString())))));
        assertThat(responseObj.getProducts(), (everyItem(hasProperty("rating", not(isEmptyOrNullString())))));
        assertThat(responseObj.getProducts(), (everyItem(hasProperty("duration", not(isEmptyOrNullString())))));
        assertThat(responseObj.getProducts(), (everyItem(hasProperty("year", notNullValue()))));
        assertThat(responseObj.getProducts(), (everyItem(hasProperty("genre", not(isEmptyOrNullString())))));
        assertThat(responseObj.getProducts(), (everyItem(hasProperty("price", notNullValue()))));
    }
}
