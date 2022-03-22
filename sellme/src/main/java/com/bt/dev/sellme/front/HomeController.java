package com.bt.dev.sellme.front;

import com.bt.dev.sellme.cart.Cart;
import com.bt.dev.sellme.cart.CartRepository;
import com.bt.dev.sellme.cartitem.CartItem;
import com.bt.dev.sellme.item.Item;
import com.bt.dev.sellme.item.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

	private ItemRepository itemRepository;
	private CartRepository cartRepository;
	
	public HomeController(ItemRepository itemRepository, CartRepository cartRepository){
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
	}
	
	@GetMapping ("/")
	public String home(Model model){
		
		model.addAttribute("items", this.itemRepository.findAll());
		model.addAttribute("cart", this.cartRepository.findById("My Cart").orElseGet(()->new Cart("My Cart")));
	 return "home";
	}
	
	@PostMapping("/add/{id}")
	public String addToCart(@PathVariable Integer id){
		
		Cart cart = this.cartRepository.findById("My Cart").orElseGet(()->new Cart("My Cart"));
		
		cart.getCartItems().stream()
				.filter(cartItem -> cartItem.getItem().getId().equals(id))
				.findAny()
				.map(cartItem -> {
					cartItem.increment();
					return cart;
				})
				.orElseGet(()->{
				Item item = this.itemRepository.findById(id)
						.orElseThrow(()->new IllegalStateException("Can't seem to find Item type "+id));
				cart.getCartItems().add(new CartItem(item,cart));
				return cart;
				
				});
		this.cartRepository.save(cart);
		return "redirect:/";
	}
}
