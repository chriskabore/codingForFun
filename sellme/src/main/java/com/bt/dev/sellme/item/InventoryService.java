package com.bt.dev.sellme.item;

import com.bt.dev.sellme.cart.Cart;
import com.bt.dev.sellme.cart.CartRepository;
import com.bt.dev.sellme.cartitem.CartItem;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {
	
	private ItemRepository itemRepository;
	private CartRepository cartRepository;
	private ItemByExampleRepository itemByExampleRepository;
	
	public InventoryService (ItemRepository itemRepository, CartRepository cartRepository){
		this.itemRepository = itemRepository;
		this.cartRepository = cartRepository;
	
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
	
	
	
	public Cart addItemToCart(String cartId, int itemId) {
		
		Cart cart = cartRepository.findById(cartId).orElseGet(()-> new Cart("My cart"));
		
		cart.getCartItems().stream()
				.filter(cartItem -> cartItem.getItem().getId().equals(itemId))
				.findAny()
				.map(cartItem -> {
					cartItem.increment();
					return cart;
				}).orElseGet(()->{
				Item item = itemRepository.findById(itemId)
						.orElseThrow(()-> new IllegalStateException("Can't find item type: "+itemId));
				cart.getCartItems().add(new CartItem(item,cart));
				return cart;
		});
		
		return this.cartRepository.save(cart);
	}
	
	public Optional<Item> getItem(Integer itemId) {
		return this.itemByExampleRepository.findById(itemId);
	}
	
	public Iterable<Item> getInventory() {
		return this.itemRepository.findAll();
	}
	
	public Item saveItem(Item newItem) {
		return this.itemRepository.save(newItem);
	}
	
	public void deleteItem(Integer id) throws Exception {
		Optional<Item> itemToDelete = this.itemRepository.findById(id);
		if(itemToDelete.isPresent()){
			this.itemRepository.delete(itemToDelete.get());
		}else {
			throw new Exception("Can't find Item  "+ id );
		}
		
	}
	
	public Cart removeOneFromCart(String cartId, Integer itemId) {
		
		Cart cart = this.cartRepository.findById(cartId) //
				.orElseGet(() -> new Cart("My Cart")); // <3>
		
		cart.getCartItems().stream() //
				.filter(cartItem -> cartItem.getItem().getId().equals(itemId)) //
				.findAny() //
				.ifPresent(CartItem::decrement);
		
		cart.getCartItems().removeIf(cartItem -> cartItem.getQuantity() <= 0);
		
		return this.cartRepository.save(cart);
	}
}
