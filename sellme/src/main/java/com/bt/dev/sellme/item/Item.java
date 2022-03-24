package com.bt.dev.sellme.item;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Item {
	
	private @Id
	@GeneratedValue
	Integer id;
	private String name;
	private String description;
	private int price;
	
	public Item(String name, String description, int i) {
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	

	
	protected Item() {}
	
	public Item(String name, int price) {
		this.name = name;
		this.price = price;
	}
	
}
