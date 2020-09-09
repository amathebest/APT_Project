package com.project.apt.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.project.apt.model.Product;
import com.project.apt.repository.ProductRepository;
import com.project.apt.view.ProductView;

public class ProductControllerTest {
	private static final String testProductName1 = "test1";
	private static final String testProductName2 = "test2";
	private static final int testProductQuantity1 = 15;
	private static final int testProductQuantity2 = 5;
	
	private static final String INFO_KEY = "info";
	private static final String ERROR_KEY = "error";
	
	public static final String noProductFound = "No such product existing in the database";
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private ProductView productView;
	
	@InjectMocks
	private ProductController productController;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testFindAllProducts() {
		List<Product> products = asList(new Product());
		when(productRepository.findAllProducts()).thenReturn(products);
		productController.allProducts();
		verify(productView).listProducts(products);
	}
	
	@Test
	public void testAddProductWhenProductWithSameNameDoesNotAlreadyExist() {
		Product newProduct = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(null);
		productController.addProduct(newProduct);
		InOrder inOrder = inOrder(productRepository, productView, productView);
		inOrder.verify(productRepository).addProduct(newProduct);
		inOrder.verify(productView).productAdded(newProduct);
		inOrder.verify(productView).showError("Product added", newProduct, INFO_KEY);
	}
	
	@Test
	public void testAddProductWhenProductWithSameNameExistsShouldNotCreateNewEntry() {
		Product existingProduct = new Product(testProductName1, testProductQuantity1);
		Product newProduct = new Product(testProductName1, testProductQuantity2);
		when(productRepository.findByName(testProductName1)).thenReturn(existingProduct);
		productController.addProduct(newProduct);
		verify(productRepository, Mockito.times(0)).addProduct(newProduct);
		verify(productView, Mockito.times(0)).productAdded(newProduct);
	}
	
	@Test
	public void testAddProductWhenProductWithSameNameExistsShouldUpdateQuantityInstead() {
		Product existingProduct = new Product(testProductName1, testProductQuantity1);
		Product newProduct = new Product(testProductName1, testProductQuantity2);
		when(productRepository.findByName(testProductName1)).thenReturn(existingProduct);
		productController.addProduct(newProduct);
		Product updatedProduct = new Product(testProductName1, testProductQuantity1 + testProductQuantity2);
		InOrder inOrder = inOrder(productRepository, productView, productView);
		inOrder.verify(productRepository).alterProductQuantity(existingProduct, existingProduct.getQuantity() + testProductQuantity2);
		inOrder.verify(productView).productEdited(existingProduct, updatedProduct);
		inOrder.verify(productView).showError("Updating quantity", updatedProduct, INFO_KEY);
	}
	
	@Test
	public void testRemoveProductWhenProductIsPresent() {
		Product existingProduct = new Product(testProductName1, testProductQuantity1);
		Product productToRemove = new Product(testProductName1, testProductQuantity2);
		when(productRepository.findByName(testProductName1)).thenReturn(existingProduct);
		productController.removeProduct(productToRemove);
		InOrder inOrder = inOrder(productRepository, productView, productView);
		inOrder.verify(productRepository).removeProduct(productToRemove);
		inOrder.verify(productView).productDeleted(productToRemove);
		inOrder.verify(productView).showError("Product removed", productToRemove, INFO_KEY);
	}
	
	@Test
	public void testRemoveProductWhenProductIsNotPresent() {
		Product productToRemove = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(null);
		productController.removeProduct(productToRemove);
		verify(productView).showError(noProductFound, productToRemove, ERROR_KEY);
	}
	
	@Test
	public void testUpdateProductNameWhenProductIsPresent() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(productToUpdate);
		productController.updateProductName(productToUpdate, testProductName2);
		Product updatedProduct = new Product(testProductName2, productToUpdate.getQuantity());
		InOrder inOrder = inOrder(productRepository, productView, productView);
		inOrder.verify(productRepository).alterProductName(productToUpdate, testProductName2);
		inOrder.verify(productView).productEdited(productToUpdate, updatedProduct);
		inOrder.verify(productView).showError("Changing product name", updatedProduct, INFO_KEY);
	}
	
	@Test
	public void testUpdateProductNameWhenProductIsNotPresent() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(null);
		productController.updateProductName(productToUpdate, testProductName2);
		verify(productView).showError(noProductFound, productToUpdate, ERROR_KEY);
	}
	
	@Test
	public void testUpdateProductNameWhenProductWithSameNameAsTheNewOneIsPresent() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		Product existingProduct = new Product(testProductName2, testProductQuantity2);
		when(productRepository.findByName(testProductName1)).thenReturn(productToUpdate);
		when(productRepository.findByName(testProductName2)).thenReturn(existingProduct);
		productController.updateProductName(productToUpdate, testProductName2);
		verify(productView).showError("Database contains already a product with selected name", productToUpdate, ERROR_KEY);
	}
	
	@Test
	public void testUpdateProductQuantityWhenProductIsPresent() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(productToUpdate);
		productController.updateProductQuantity(productToUpdate, testProductQuantity2);
		Product updatedProduct = new Product(productToUpdate.getName(), testProductQuantity2);
		InOrder inOrder = inOrder(productRepository, productView, productView);
		inOrder.verify(productRepository).alterProductQuantity(productToUpdate, testProductQuantity2);
		inOrder.verify(productView).productEdited(productToUpdate, updatedProduct);
		inOrder.verify(productView).showError("Changing product quantity", updatedProduct, INFO_KEY);
	}
	
	@Test
	public void testUpdateProductQuantityWhenProductIsNotPresent() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(null);
		productController.updateProductQuantity(productToUpdate, testProductQuantity2);
		verify(productView).showError(noProductFound, productToUpdate, ERROR_KEY);
	}
}
