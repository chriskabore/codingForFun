package com.bt.dev.sellme.item;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Item {
	
	private @Id
	@GeneratedValue
	Integer id;
	private String name;
	private String description;
	private int price;
	
	public Item(String name, String description, int price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}
	
	public Item(Integer itemId, String itemName, String itemDescription, int itemPrice ){
		this.id = itemId;
		this.name = itemName;
		this.description = itemDescription;
		this.price = itemPrice;
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
	
	
	
	@Override
	public String toString() {
		return "Item{" + "id=" + id + ", name='" + name + '\'' + ", description='"+ description +'\''+ ", price=" + price + '}';
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Item item = (Item) o;
		return (item.price == price) && Objects.equals(id, item.id) && Objects.equals(name, item.name)
				&& Objects.equals(description, item.description);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, name, description, price);
	}
	
}
