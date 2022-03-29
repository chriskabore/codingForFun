package com.bt.dev.sellme.integration;

import com.bt.dev.sellme.cart.Cart;
import com.bt.dev.sellme.cart.CartService;
import com.bt.dev.sellme.item.InventoryService;
import com.bt.dev.sellme.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerSliceTest {

	private WebTestClient client;
	
	@MockBean
	InventoryService inventoryService;
	@MockBean
	CartService cartService;
	
	@BeforeEach
	public void setUp(@Autowired MockMvc mockMvc){
		this.client = MockMvcWebTestClient.bindTo(mockMvc).build();
	}
	
	@Test
	public void homePage(){
		when(inventoryService.getInventory()).thenReturn(Arrays.asList(
				new Item(1,"name1", "desc1",2000),
				new Item(2,"name2", "desc2",3000)
		));
		
		when(cartService.getCart("My Cart"))
				.thenReturn(Optional.of(new Cart("My Cart")));
		
		client.get().uri("/").exchange()
				.expectStatus().isOk()
				.expectBody(String.class)
				.consumeWith(exchangeResult -> {
					assertThat(
							exchangeResult.getResponseBody()).contains("action=\"/add/1\"");
					assertThat(
							exchangeResult.getResponseBody()).contains("action=\"/add/2\"");
				});
	}
}
