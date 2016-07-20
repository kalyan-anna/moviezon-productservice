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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({ProductServiceApplication.class, ProductTestConfiguration.class})
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("test")
public class HttpMethodTest {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void callingSearchWithHttpPost_ShouldReturnErrorStatus() throws Exception {
        mockMvc.perform(post("/product/search"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void callingSearchWithHttpPut_ShouldReturnErrorStatus() throws Exception {
        mockMvc.perform(put("/product/search"))
                .andExpect(status().is4xxClientError());
    }
}
