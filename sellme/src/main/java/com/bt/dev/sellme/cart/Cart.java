package com.bt.dev.sellme.cart;

import com.bt.dev.sellme.cartitem.CartItem;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
	
	private @Id
	String id;
	private @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true) List<CartItem> cartItems;
	
	protected Cart() {}
	
	public Cart(String id) {
		this(id, new ArrayList<>());
	}
	
	public Cart(String id, List<CartItem> cartItems) {
		this.id = id;
		this.cartItems = cartItems;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public List<CartItem> getCartItems() {
		return cartItems;
	}
	
	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}
}
