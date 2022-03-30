package com.bt.dev.sellme.item;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
	
	List<Item> findByNameContaining(String partialName);

	Optional<Item> findByName(String name);


	
	
}
