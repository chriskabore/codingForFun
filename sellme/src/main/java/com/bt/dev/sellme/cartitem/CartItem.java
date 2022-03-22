package com.bt.dev.sellme.cartitem;

import com.bt.dev.sellme.cart.Cart;
import com.bt.dev.sellme.item.Item;

import javax.persistence.*;

@Entity
public class CartItem {
	
	private @Id
	@GeneratedValue
	Integer id;
	private @ManyToOne(fetch = FetchType.LAZY)
	Cart cart;
	private @ManyToOne(fetch = FetchType.LAZY)
	Item item;
	private int quantity;
	
	protected CartItem() {}
	
	public CartItem(Item item, Cart cart) {
		this.item = item;
		this.cart = cart;
		this.quantity = 1;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void increment() {
		this.quantity++;
	}
	
	public void decrement() {
		this.quantity--;
	}
}
