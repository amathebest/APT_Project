package com.project.apt.model;

import java.util.Objects;

public class Product {
	private String name;
	private int quantity;
	
	public Product() {}
	
	public Product(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Product other = (Product) obj;
		return Objects.equals(name, other.name);
	}
	
	@Override
	public String toString() {
		return "Product [Name=" + name + ", quantity=" + quantity + "]";
	}
}
