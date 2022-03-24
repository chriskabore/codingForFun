package com.bt.dev.sellme.integration;

import com.bt.dev.sellme.item.Item;
import com.bt.dev.sellme.item.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaSliceTest {
	
	@Autowired
	ItemRepository itemRepository;
	
	@Test
	public void itemRepositorySavesItems(){
	 Item testItem = new Item("nom", "description", 2000);
	 Item savedItem = itemRepository.save(testItem);
		
		assertThat(savedItem.getId()).isNotNull();
		assertThat(savedItem.getName()).isEqualTo("nom");
		assertThat(savedItem.getDescription()).isEqualTo("description");
		assertThat(savedItem.getPrice()).isEqualTo(2000);
	}
}
