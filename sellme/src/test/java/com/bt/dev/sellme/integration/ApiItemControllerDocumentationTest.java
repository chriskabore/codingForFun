package com.bt.dev.sellme.integration;

import com.bt.dev.sellme.item.InventoryService;
import com.bt.dev.sellme.item.Item;
import com.bt.dev.sellme.item.ItemRepository;
import com.bt.dev.sellme.item.ItemRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@WebMvcTest(controllers = ItemRestController.class)
@AutoConfigureRestDocs
public class ApiItemControllerDocumentationTest {
	
	private WebTestClient client;
	
	@MockBean
	InventoryService inventoryService;
	
	@MockBean
	ItemRepository itemRepository;

	@BeforeEach
	public void setUp(@Autowired MockMvc mockMvc, @Autowired RestDocumentationContextProvider restDocumentation){
		this.client = MockMvcWebTestClient
				.bindTo(mockMvc)
				.filter(documentationConfiguration(restDocumentation))
				.build();
	}
	@Test
	public void findingAllItems (){
		when(itemRepository.findAll()).thenReturn(
				List.of(new Item(1, "reveil alarme", "montre alarme", 2000)));

		this.client.get().uri("/api/items")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(document("findAll", preprocessResponse(prettyPrint())));
	}


	@Test
	void postNewItem() {
		when(inventoryService.saveItem(any())).thenReturn(
				new Item(1, "Alf alarm clock", "nothing important", 2000));

		this.client.post().uri("/api/items")
				.bodyValue(new Item("Alf alarm clock", "nothing important", 2000))
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Item.class)
				.consumeWith(document("post-new-item", preprocessResponse(prettyPrint())));
	}


	@PutMapping("/api/items/{id}")
	public ResponseEntity<?> updateItem(
										 @RequestBody Item item,
										 @PathVariable Integer id) {

		Item newItem = new Item(id, item.getName(), item.getDescription(),
				item.getPrice());

		this.inventoryService.saveItem(newItem);

		return ResponseEntity.created(URI.create("/api/items/" + id)).build();
	}
	
	
}
