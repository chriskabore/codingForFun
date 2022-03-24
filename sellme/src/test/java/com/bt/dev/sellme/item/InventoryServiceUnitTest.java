package com.bt.dev.sellme.item;

import com.bt.dev.sellme.cart.Cart;
import com.bt.dev.sellme.cart.CartRepository;
import com.bt.dev.sellme.cartitem.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class) // @ExtendWith is JUnit 5’s API to register custom test handlers.
// SpringExtension is Spring Framework’s hook for Spring-specific test features.”
public class InventoryServiceUnitTest {
	
  InventoryService inventoryService;
  @MockBean private CartRepository cartRepository;
  @MockBean private ItemRepository itemRepository;
  
  
  @BeforeEach
  public void setUP(){
  	// definition of test data
	  Item testItem =  new Item(1,"LG Web OLED 55 pouces", "Televiseur LG", 250000);
	  CartItem testCartItem = new CartItem(testItem,null);
	  Cart testCart = new Cart("My Cart", Collections.singletonList(testCartItem));
	  testCartItem.setCart(testCart);
	  
	  //define mock interactions defined by collaborators
	  when(cartRepository.findById(anyString())).thenReturn(Optional.empty());
	  when(itemRepository.findById(anyInt())).thenReturn(Optional.of(testItem));
	  when(cartRepository.save(any(Cart.class))).thenReturn(testCart);
	  inventoryService = new InventoryService(itemRepository,cartRepository);
	  
  }
  
  @Test
  public void addItemToEmptyCartShouldProduceOneCartItem(){
  	
    Cart cart = inventoryService.addItemToCart("My Cart", 1);
    Item expectedItem = new Item(1,"LG Web OLED 55 pouces", "Televiseur LG", 250000);
    
    assertThat(cart.getCartItems()).extracting(CartItem::getQuantity).containsExactlyInAnyOrder(1);
    assertThat(cart.getCartItems()).extracting(CartItem::getItem)
		    .containsExactly(expectedItem);
  }
	 
	 
  
}
