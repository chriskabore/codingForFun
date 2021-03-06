package com.bt.dev.sellme.integration;

import com.bt.dev.sellme.item.HypermediaItemController;
import com.bt.dev.sellme.item.InventoryService;
import com.bt.dev.sellme.item.Item;
import com.bt.dev.sellme.item.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@WebMvcTest(controllers = HypermediaItemController.class)
@AutoConfigureRestDocs
public class HypermediaItemControllerDocumentationTest {

    private WebTestClient webTestClient;

    @MockBean
    InventoryService service;

    @MockBean
    ItemRepository repository;

    @BeforeEach
    void setUp(@Autowired MockMvc mockMvc, @Autowired RestDocumentationContextProvider restDocumentation) {
        this.webTestClient = MockMvcWebTestClient
                .bindTo(mockMvc)
                .filter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void findingAllItems() {
        when(repository.findAll())
                .thenReturn(Arrays.asList(
                        new Item("Alf alarm clock",
                                "nothing I really need", 1000)));
        when(repository.findById((Integer) null))
                .thenReturn(Optional.of(
                        new Item(1, "Alf alarm clock",
                                "nothing I really need", 1000)));

        this.webTestClient.get().uri("/hypermedia/items")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("findAll-hypermedia",
                        preprocessResponse(prettyPrint())));
    }


    void postNewItem() {
        this.webTestClient.post().uri("/hypermedia/items")
                .body(new Item(1, "Alf alarm clock",
                        "nothing I really need", 1000), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();
    }

    @Test
    void findOneItem() {
        when(repository.findById(1)).thenReturn(Optional.of(
                new Item(1, "Alf alarm clock", "nothing I really need", 1000)));

        this.webTestClient.get().uri("/hypermedia/items/1")
                .accept(MediaTypes.HAL_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("findOne-hypermedia", preprocessResponse(prettyPrint()),
                        links(
                                linkWithRel("self").description("Canonical link to this `Item`"),
                                linkWithRel("item").description("Link back to the aggregate root"))));
    }

    @Test
    void findProfile() {
        this.webTestClient.get().uri("/hypermedia/items/profile")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("profile",
                        preprocessResponse(prettyPrint())));
    }


}
