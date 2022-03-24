package com.bt.dev.sellme.cart;

import com.bt.dev.sellme.cartitem.CartItem;
import com.bt.dev.sellme.item.Item;
import com.bt.dev.sellme.item.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
	
	private final ItemRepository itemRepository;
	private final CartRepository cartRepository;
	
	public CartService (CartRepository cartRepository , ItemRepository itemRepository){
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
	}
	
	public Optional<Cart> getCart(String cartId) {
		return this.cartRepository.findById(cartId);
	}
	
	public Cart addToCart(String cartId, Integer itemId){
		Cart cart = this.cartRepository.findById(cartId)
				.orElseGet(()->new Cart(cartId));
		
		cart.getCartItems().stream()
				.filter(cartItem -> cartItem.getItem().getId().equals(itemId))
				.findAny()
				.map(cartItem -> {
					cartItem.increment();
					return cart;
				})
				.orElseGet(()->{
					Item item = this.itemRepository.findById(itemId)
							.orElseThrow(()->new IllegalStateException("Can't seem to find Item type "+itemId));
					cart.getCartItems().add(new CartItem(item,cart));
					return cart;
					
				});
	  return this.cartRepository.save(cart);
	}
}
