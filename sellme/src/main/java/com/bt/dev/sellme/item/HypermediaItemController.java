package com.bt.dev.sellme.item;

import org.springframework.hateoas.*;
import org.springframework.hateoas.mediatype.alps.Alps;
import org.springframework.hateoas.mediatype.alps.Type;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.mediatype.alps.Alps.alps;
import static org.springframework.hateoas.mediatype.alps.Alps.descriptor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
public class HypermediaItemController {

    private InventoryService inventoryService;

    public HypermediaItemController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping("/hypermedia")
    RepresentationModel<?> root() {
        HypermediaItemController controller =
                methodOn(HypermediaItemController.class);

        Link selfLink = linkTo(controller.root()).withSelfRel();

        Link itemsAggregateLink =
                linkTo(controller.findAll())
                        .withRel(IanaLinkRelations.ITEM);

        return new RepresentationModel<>(Links.of(selfLink, itemsAggregateLink));
    }

    @GetMapping("/hypermedia/items")
    CollectionModel<EntityModel<Item>> findAll() {

        List<EntityModel<Item>> entityModels = StreamSupport.stream(this.inventoryService.getInventory().spliterator(), false)
                .map(item -> findOne(item.getId()))
                .collect(Collectors.toList());

        return CollectionModel.of(
                entityModels,
                linkTo(methodOn(HypermediaItemController.class).findAll()).withSelfRel());
    }


    @GetMapping("/hypermedia/items/{id}")
    EntityModel<Item> findOne(@PathVariable Integer id) {
        HypermediaItemController controller = methodOn(HypermediaItemController.class);

        Link selfLink = linkTo(controller.findOne(id)).withSelfRel();

        Link aggregateLink = linkTo(controller.findAll())
                .withRel(IanaLinkRelations.ITEM);

        return this.inventoryService.getItem(id)
                .map(item -> EntityModel.of(item, selfLink, aggregateLink))
                .orElseThrow(() -> new IllegalStateException("Couldn't find item " + id));
    }

    @GetMapping("/hypermedia/items/{id}/affordances")
    EntityModel<Item> findOneWithAffordances(@PathVariable Integer id) {
        HypermediaItemController controller =
                methodOn(HypermediaItemController.class);

        Link selfLink = linkTo(controller.findOne(id)).withSelfRel()
                .andAffordance(afford(controller.updateItem(null, id)));

        Link aggregateLink = linkTo(controller.findAll()).withRel(IanaLinkRelations.ITEM);

        return this.inventoryService.getItem(id)
                .map(item -> EntityModel.of(item, selfLink, aggregateLink))
                .orElseThrow(() -> new IllegalStateException("Could not find item " + id));
    }

    @PostMapping("/hypermedia/items")
    ResponseEntity<?> addNewItem(@RequestBody EntityModel<Item> itemEntity) {
        Item content = itemEntity.getContent();
        Item savedItem = this.inventoryService.saveItem(content);

        return ResponseEntity.created(
                findOne(savedItem.getId()).getRequiredLink(IanaLinkRelations.SELF).toUri())
                .build();
    }

    @PutMapping("/hypermedia/items/{id}")
    public ResponseEntity<?> updateItem(@RequestBody EntityModel<Item> itemEntity,
                                        @PathVariable Integer id) {
        Item content = itemEntity.getContent();
        Item newItem = new Item(id, content.getName(),
                content.getDescription(), content.getPrice());

        this.inventoryService.saveItem(newItem);

        return ResponseEntity.noContent()
                .location(findOne(id).getRequiredLink(IanaLinkRelations.SELF).toUri()).build();
    }

    @GetMapping(value = "/hypermedia/items/profile", produces = MediaTypes.ALPS_JSON_VALUE)
    public Alps profile() {
        return alps()
                .descriptor(Collections.singletonList(descriptor()
                        .id(Item.class.getSimpleName() + "-repr")
                        .descriptor(Arrays.stream(
                                Item.class.getDeclaredFields())
                                .map(field -> descriptor()
                                        .name(field.getName())
                                        .type(Type.SEMANTIC)
                                        .build())
                                .collect(Collectors.toList()))
                        .build()))
                .build();
    }
}
