package com.bt.dev.sellme.item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
public class ItemRestController {
	
	private InventoryService inventoryService;
	
	public ItemRestController(InventoryService inventoryService){
		this.inventoryService =inventoryService;
	}
	
	@GetMapping("/api/items")
	public Iterable<Item> getAllItems(){
	  return inventoryService.getInventory();
	}
	
	@GetMapping("/api/items/{id}")
	public Optional<Item> getItemWithId(@PathVariable("id")Integer itemId){
		return inventoryService.getItem(itemId);
	}
	
	@PostMapping("/api/items")
	public ResponseEntity<?> addNewItem(@RequestBody Item itemToBeAdded){
		Item savedItem = this.inventoryService.saveItem(itemToBeAdded);
		return ResponseEntity.created(URI.create("/api/items"+ savedItem.getId())).body(savedItem);
	}
	
	@PutMapping("/api/items/{id}")
	public ResponseEntity<?> updateItem(@RequestBody Item itemToBeUpdated, @PathVariable("id") Integer itemId){
		Optional<Item> itemData = this.inventoryService.getItem(itemId);
		if(itemData.isPresent()){
			this.inventoryService.saveItem(new Item(itemId, itemToBeUpdated.getName(), itemToBeUpdated.getDescription(), itemToBeUpdated.getPrice()));
			return ResponseEntity.created(URI.create("/api/items" + itemId)).build();
		}else{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
	}
	
	@DeleteMapping("/api/items/{id}")
	public ResponseEntity<?> deleteItem(@PathVariable("id") Integer itemId){
		try{
			this.inventoryService.deleteItem(itemId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
