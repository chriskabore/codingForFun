package com.bt.dev.sellme.front;

import com.bt.dev.sellme.cart.Cart;
import com.bt.dev.sellme.cart.CartRepository;
import com.bt.dev.sellme.cart.CartService;
import com.bt.dev.sellme.item.InventoryService;
import com.bt.dev.sellme.item.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	private ItemRepository itemRepository;
	private CartRepository cartRepository;
	private CartService cartService;
	private InventoryService inventoryService;
	
	public HomeController(ItemRepository itemRepository, CartRepository cartRepository, CartService cartService,
	                      InventoryService inventoryService){
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
		this.cartService = cartService;
		this.inventoryService = inventoryService;
	}
	
	@GetMapping ("/")
	public String home(Model model){
		
		model.addAttribute("items", this.itemRepository.findAll());
		model.addAttribute("cart", this.cartRepository.findById("My Cart").orElseGet(()->new Cart("My Cart")));
	 return "home";
	}
	
	@PostMapping("/add/{id}")
	public String addToCart(@PathVariable Integer id){
		this.cartService.addToCart("My Cart",id);
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
