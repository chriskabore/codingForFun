package com.bt.dev.sellme.integration;

import com.bt.dev.sellme.cart.Cart;
import com.bt.dev.sellme.cart.CartService;
import com.bt.dev.sellme.item.InventoryService;
import com.bt.dev.sellme.item.Item;
import com.bt.dev.sellme.item.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerSliceTest {


	private WebTestClient client;

	@MockBean
	ItemRepository repository;
	
	@MockBean
	InventoryService inventoryService;
	@MockBean
	CartService cartService;


	
	@BeforeEach
	public void setUp(@Autowired MockMvc mockMvc){
		this.client = MockMvcWebTestClient.bindTo(mockMvc).build();
	}
	
	@Test
	@WithMockUser(username = "ada")
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

	@Test
	void verifyLoginPageBlocksAccess() {
		this.client.get().uri("/") //
				.exchange() //
				.expectStatus().isUnauthorized();
	}

	@Test
	@WithMockUser(username = "ada")
	void verifyLoginPageWorks() {
		this.client.get().uri("/") //
				.exchange() //
				.expectStatus().isOk();
	}

	@Test
	@WithMockUser(username = "alice", roles = { "SOME_OTHER_ROLE" })
	void addingInventoryWithoutProperRoleFails() {
		this.client.post().uri("/")
				.exchange()
				.expectStatus().isForbidden();
	}

	@Test
	@WithMockUser(username = "bob", roles = { "INVENTORY" })
	void addingInventoryWithProperRoleSucceeds() throws InterruptedException {


		this.client //
				.post().uri("/create") //
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("{" +
						"\"name\": \"iPhone 11\", " + //
						"\"description\": \"upgrade\", " + //
						"\"price\": 850000" + //
						"}") //
				.exchange() //
				.expectStatus().isFound();

		when(inventoryService.getItemByName(anyString()))
				.thenReturn(Optional.of(new Item(5, "iPhone 11","upgrade",850000)));


		assertThat(this.inventoryService.getItemByName("iPhone 11")).hasValueSatisfying(item -> {
			assertThat(item.getDescription()).isEqualTo("upgrade");
			assertThat(item.getPrice()).isEqualTo(850000);
		});

	}

	@Test
	@WithMockUser(username = "carol", roles = { "SOME_OTHER_ROLE" })
	void deletingInventoryWithoutProperRoleFails() {
		this.client.delete().uri("/some-item") //
				.exchange() //
				.expectStatus().isForbidden();
	}

	@Test
	@WithMockUser(username = "dan", roles = { "INVENTORY" })
	void deletingInventoryWithProperRoleSucceeds() {
		when(inventoryService.getItemByName(anyString()))
				.thenReturn(Optional.of(new Item(5, "Reveil Alarme","reveil",3000)));
		Integer id = this.inventoryService.getItemByName("Reveil Alarme") //
				.map(Item::getId) //
				.orElseThrow(() -> new IllegalStateException("Could not find Reveil Alarme"));

		this.client //
				.delete().uri("/delete/{id}", ""+id) //
				.exchange() //
				.expectStatus().isFound();

		when(inventoryService.getItemByName("Reveil Alarme"))
				.thenReturn(Optional.ofNullable(null));

		assertThat(this.inventoryService.getItemByName("Reveil Alarme")).isEmpty();
	}
}
