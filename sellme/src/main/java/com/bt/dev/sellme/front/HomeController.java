package com.bt.dev.sellme.front;

import com.bt.dev.sellme.cart.Cart;
import com.bt.dev.sellme.cart.CartService;
import com.bt.dev.sellme.item.InventoryService;
import com.bt.dev.sellme.item.Item;
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
	public String home(Model model){
		
		model.addAttribute("items", this.inventoryService.getInventory());
		model.addAttribute("cart", this.cartService.getCart("My Cart").orElseGet(()->new Cart("My Cart")));
		
		this.inventoryService.getInventory().forEach(System.out::println);
	 return "home";
	}
	
	
	@PostMapping("/add/{id}")
	public String addToCart(@PathVariable Integer id){
		this.cartService.addToCart("My Cart",id);
		return "redirect:/";
	}
	
	@DeleteMapping("/remove/{id}")
	public String removeFromCart(@PathVariable Integer id) {
		this.inventoryService.removeOneFromCart("My Cart", id);
		return "redirect:/";
	}
	
	@PostMapping
	public String createItem(@RequestBody Item newItem) {
		this.inventoryService.saveItem(newItem);
		return "redirect:/";
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteItem(@PathVariable Integer id) {
		this.inventoryService.deleteItem(id);
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
}
