package com.project.apt.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
	private static final int testProductQuantity1 = 10;
	private static final int testProductQuantity2 = 5;
	public static final String noProductFound = "No such product existing in the database.";
	
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
		InOrder inOrder = inOrder(productRepository, productView);
		inOrder.verify(productRepository).addProduct(newProduct);
		inOrder.verify(productView).productAdded(newProduct);
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
		InOrder inOrder = inOrder(productRepository, productView);
		inOrder.verify(productRepository).alterProductQuantity(existingProduct, testProductQuantity2);
		inOrder.verify(productView).productEdited(existingProduct);
		inOrder.verify(productView).showError("Product already present, updating quantity instead.", existingProduct, "info");
	}
	
	@Test
	public void testRemoveProductWhenProductIsPresent() {
		Product existingProduct = new Product(testProductName1, testProductQuantity1);
		Product productToRemove = new Product(testProductName1, testProductQuantity2);
		when(productRepository.findByName(testProductName1)).thenReturn(existingProduct);
		productController.removeProduct(productToRemove);
		InOrder inOrder = inOrder(productRepository, productView);
		inOrder.verify(productRepository).removeProduct(productToRemove);
		inOrder.verify(productView).productDeleted(productToRemove);
	}
	
	@Test
	public void testRemoveProductWhenProductIsNotPresent() {
		Product productToRemove = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(null);
		productController.removeProduct(productToRemove);
		verify(productView).showError(noProductFound, productToRemove, "error");
	}
	
	@Test
	public void testUpdateProductNameWhenProductIsPresent() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(productToUpdate);
		productController.updateProductName(productToUpdate, testProductName2);
		InOrder inOrder = inOrder(productRepository, productView);
		inOrder.verify(productRepository).alterProductName(productToUpdate, testProductName2);
		inOrder.verify(productView).productEdited(productToUpdate);
	}
	
	@Test
	public void testUpdateProductNameWhenProductIsNotPresent() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(null);
		productController.updateProductName(productToUpdate, testProductName2);
		verify(productView).showError(noProductFound, productToUpdate, "error");
	}
	
	@Test
	public void testUpdateProductNameWhenProductWithSameNameAsTheNewOneIsPresent() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		Product existingProduct = new Product(testProductName2, testProductQuantity2);
		when(productRepository.findByName(testProductName2)).thenReturn(existingProduct);
		productController.updateProductName(productToUpdate, testProductName2);
		verify(productView).showError("Database contains already a product with selected name", productToUpdate, "error");
	}
	
	@Test
	public void testUpdateProductQuantityWhenProductIsPresent() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(productToUpdate);
		productController.updateProductQuantity(productToUpdate, testProductQuantity2);
		InOrder inOrder = inOrder(productRepository, productView);
		inOrder.verify(productRepository).alterProductQuantity(productToUpdate, testProductQuantity2);
		inOrder.verify(productView).productEdited(productToUpdate);
	}
	
	@Test
	public void testUpdateProductQuantityWhenProductIsNotPresent() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		when(productRepository.findByName(testProductName1)).thenReturn(null);
		productController.updateProductQuantity(productToUpdate, testProductQuantity2);
		verify(productView).showError(noProductFound, productToUpdate, "error");
	}
}
