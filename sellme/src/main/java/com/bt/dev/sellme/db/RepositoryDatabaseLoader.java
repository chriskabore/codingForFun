package com.bt.dev.sellme.db;

import com.bt.dev.sellme.item.Item;
import com.bt.dev.sellme.item.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RepositoryDatabaseLoader {
	@Bean
	CommandLineRunner initialize(ItemRepository repository){
		return args -> {
			repository.save(new Item("Reveil Alarme",3000));
			repository.save(new Item("Peluche lapin",4000));
		};
	}
}
