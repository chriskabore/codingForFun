package com.bt.dev.sellme.item;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
	private ItemRepository itemRepository;
	private ItemByExampleRepository itemByExampleRepository;
	
	public InventoryService(ItemRepository itemRepository, ItemByExampleRepository itemByExampleRepository){
		this.itemRepository = itemRepository;
		this.itemByExampleRepository = itemByExampleRepository;
	}
	
	public Iterable<Item> searchByExample(String name, String description, boolean useAnd){
		Item item = new Item(name,description, 0);
		ExampleMatcher matcher = (useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny())
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				.withIgnoreCase()
				.withIgnorePaths("price");
		
		Example<Item> probe = Example.of(item, matcher);
		return itemByExampleRepository.findAll(probe);
	}
}
