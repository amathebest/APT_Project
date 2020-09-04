package com.project.apt.view;

import static org.assertj.core.api.Assertions.*;

import java.awt.Color;
import java.net.InetSocketAddress;

import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.InetSocketAddress;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

import com.project.apt.controller.ProductController;
import com.project.apt.model.Product;
import com.project.apt.repository.ProductRepositoryMongo;

@RunWith(GUITestRunner.class)
public class ProductViewSwingIT extends AssertJSwingJUnitTestCase {
	
	private static MongoServer server;
	private static InetSocketAddress serverAddress;
	
	private MongoClient mongoClient;
	
	private FrameFixture window;
	private ProductViewSwing productViewSwing;
	private ProductController productController;
	private ProductRepositoryMongo productRepositoryMongo;
	
	private static final String testProductName1 = "test1";
	private static final String testProductName2 = "test2";
	private static final int testProductQuantity1 = 10;
	private static final int testProductQuantity2 = 5;
	
	private static final String DB_GESTIONALE = "gestionale";
	private static final String PRODUCT_COLLECTION = "product";
	
	@BeforeClass
	public static void setupMongoServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}
	
	@AfterClass
	public static void shutdownMongoServer() {
		server.shutdown();
	}
	
	@Override
	protected void onSetUp() {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		productRepositoryMongo = new ProductRepositoryMongo(mongoClient, DB_GESTIONALE, PRODUCT_COLLECTION);
		for (Product product : productRepositoryMongo.findAllProducts()) {
			productRepositoryMongo.removeProduct(product);
		}
		GuiActionRunner.execute(() -> {
			productViewSwing = new ProductViewSwing();
			productController = new ProductController(productViewSwing, productRepositoryMongo);
			productViewSwing.setProductController(productController);
			return productViewSwing;
		});
		window = new FrameFixture(robot(), productViewSwing);
		window.show();
	}
	
	@Override
	protected void onTearDown() {
		mongoClient.close();
	}
	
	@Test
	public void testAllProducts() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		Product product2 = new Product(testProductName2, testProductQuantity2);
		productRepositoryMongo.addProduct(product1);
		productRepositoryMongo.addProduct(product2);
		GuiActionRunner.execute(
			() -> productController.allProducts()
		);
		assertThat(window.list("productList").contents()).containsExactly(product1.toString(), product2.toString());
	}
	
	@Test
	public void testAddProductButtonWhenNoProductWithSameNameExists() {
		window.textBox("nameTextBox").enterText(testProductName1);
		window.textBox("quantityTextBox").enterText(String.valueOf(testProductQuantity1));
		Product productToAdd = new Product(testProductName1, testProductQuantity1);
		window.button("addProductButton").click();
		assertThat(window.list("productList").contents()).containsExactly(productToAdd.toString());
	}
	
	@Test
	public void testAddProductButtonWhenProductWithSameNameAlreadyExistShouldIncreaseQuantityInstead() {
		Product existingProduct = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productController.addProduct(existingProduct)
		);
		window.textBox("nameTextBox").enterText(testProductName1);
		window.textBox("quantityTextBox").enterText(String.valueOf(testProductQuantity2));
		window.button("addProductButton").click();
		Product productWithUpdatedQuantity = new Product(testProductName1, testProductQuantity1 + testProductQuantity2);
		assertThat(window.list("productList").contents()).containsExactly(productWithUpdatedQuantity.toString());
		window.label("lblMessage").requireText("Product already present, updating quantity instead: " + productWithUpdatedQuantity);
		window.label("lblMessage").foreground().requireEqualTo(Color.BLACK);
	}
	
	@Test
	public void testRemoveProductButtonWhenProductExists() {
		Product productToRemove = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productController.addProduct(productToRemove)
		);
		window.list("productList").selectItem(0);
		window.button("removeProductButton").click();
		assertThat(window.list("productList").contents()).isEmpty();
	}
	
	@Test
	public void testRemoveProductButtonWhenProductDoesnNotExistInTheDatabase() {
		Product productToRemove = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productViewSwing.getListProductModel().addElement(productToRemove)
		);
		window.list("productList").selectItem(0);
		window.button("removeProductButton").click();
		window.label("lblMessage").requireText("No such product existing in the database: " + productToRemove);
		window.label("lblMessage").foreground().requireEqualTo(Color.RED);
	}
	
	@Test
	public void testEditProductButtonWhenNameIsSelectedShouldChangeProductNameInTheList() {
		Product productToEdit = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productController.addProduct(productToEdit)
		);
		window.list("productList").selectItem(0);
		window.textBox("editPropertiesTextBox").enterText(testProductName2);
		window.radioButton("nameEditRadioButton").click();
		window.button("editProductButton").click();
		Product productWithUpdatedName = new Product(testProductName2, testProductQuantity1);
		assertThat(window.list("productList").contents()).containsExactly(productWithUpdatedName.toString());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
