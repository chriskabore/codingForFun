package com.bt.dev.sellme.front;

import com.bt.dev.sellme.cart.Cart;
import com.bt.dev.sellme.cart.CartService;
import com.bt.dev.sellme.item.InventoryService;
import com.bt.dev.sellme.item.Item;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

	private InventoryService inventoryService;
	private CartService cartService;
	
	
	public HomeController(InventoryService inventoryService, CartService cartService){
		this.cartService = cartService;
		this.inventoryService = inventoryService;
	}
	
	@GetMapping ("/")
	public String home(Authentication auth, Model model){
		
		model.addAttribute("items", this.inventoryService.getInventory());
		model.addAttribute("cart", this.cartService.getCart(cartName(auth)).orElseGet(()->new Cart(cartName(auth))));
		model.addAttribute("auth", auth);
		this.inventoryService.getInventory().forEach(System.out::println);
	 	return "home";
	}


	
	@PostMapping("/add/{id}")
	public String addToCart(Authentication auth,@PathVariable Integer id){
		this.cartService.addToCart(cartName(auth),id);
		return "redirect:/";
	}
	
	@DeleteMapping("/remove/{id}")
	public String removeFromCart(Authentication auth,@PathVariable Integer id) {
		this.inventoryService.removeOneFromCart(cartName(auth), id);
		return "redirect:/";
	}
	
	@PostMapping("/create")
	public String createItem(@RequestBody Item newItem, Model model) {
		Item savedItem = this.inventoryService.saveItem(newItem);
		model.addAttribute("savedItem", savedItem);
		return "redirect:/";
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteItem(@PathVariable Integer id) {
		try{
			this.inventoryService.deleteItem(id);
		}catch(Exception e){
			System.err.println("Could not delete Item:"+id);
		}

		return "redirect:/";
	}
	
	@GetMapping("/search")
	public String search(@RequestParam(required = false)String name,
	                     @RequestParam(required = false) String description,
	                     @RequestParam(required = false ) boolean useAnd,
	                     Model model){
		
		model.addAttribute("results", inventoryService.searchByExample(name, description, useAnd));
		return "home";
	}

	private static String cartName(Authentication auth) {
		return auth.getName() + "'s Cart";
	}
}
