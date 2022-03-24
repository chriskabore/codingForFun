package com.bt.dev.sellme.item;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ItemUnitTest {

	@Test
	public void itemBasicsShouldWork (){
		Item testIem = new Item(1,"LG Web OLED 55 pouces", "Televiseur LG", 250000);
		// assert that getters work
		assertThat(testIem.getId()).isEqualTo(1);
		assertThat(testIem.getName()).isEqualTo("LG Web OLED 55 pouces");
		assertThat(testIem.getDescription()).isEqualTo("Televiseur LG");
		assertThat(testIem.getPrice()).isEqualTo(250000);
		
	}
}
