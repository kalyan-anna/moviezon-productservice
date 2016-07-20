package com.moviezon.product.rest.spec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviezon.ProductServiceApplication;
import com.moviezon.product.ProductTestConfiguration;
import com.moviezon.product.rest.dto.ProductDetailResponse;
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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
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
public class ProctSpec {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void validProductId_ShouldReturnProductDetails() throws Exception {
        String json = mockMvc.perform(get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();
        ProductDetailResponse responseObj = new ObjectMapper().readValue(json, ProductDetailResponse.class);
        assertThat(responseObj, (hasProperty("productId", not(isEmptyOrNullString()))));
        assertThat(responseObj, (hasProperty("title", not(isEmptyOrNullString()))));
        assertThat(responseObj, (hasProperty("synopsis", not(isEmptyOrNullString()))));
        assertThat(responseObj, (hasProperty("rating", not(isEmptyOrNullString()))));
        assertThat(responseObj, (hasProperty("duration", not(isEmptyOrNullString()))));
        assertThat(responseObj, (hasProperty("year", notNullValue())));
        assertThat(responseObj, (hasProperty("cast", notNullValue())));
        assertThat(responseObj, (hasProperty("director", notNullValue())));
        assertThat(responseObj, (hasProperty("genre", not(isEmptyOrNullString()))));
        assertThat(responseObj, (hasProperty("imageFilename", not(isEmptyOrNullString()))));
        assertThat(responseObj, (hasProperty("category", not(isEmptyOrNullString()))));
        assertThat(responseObj, (hasProperty("price", notNullValue())));
    }

    @Test
    public void invalidProductId_ShouldReturn404NotFoundStatus() throws Exception {
        mockMvc.perform(get("/product/99999999"))
                .andExpect(status().isNotFound());
    }
}
