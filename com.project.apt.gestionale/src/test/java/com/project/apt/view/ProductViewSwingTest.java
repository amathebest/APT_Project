package com.project.apt.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.apt.controller.ProductController;
import com.project.apt.model.Product;

@RunWith(GUITestRunner.class)
public class ProductViewSwingTest extends AssertJSwingJUnitTestCase {
	
	private static final String testProductName1 = "test1";
	private static final String testProductName2 = "test2";
	private static final int testProductQuantity1 = 10;
	private static final int testProductQuantity2 = 5;
	
	private FrameFixture window;
	
	private ProductViewSwing productView;
	
	@Mock
	private ProductController productController;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		GuiActionRunner.execute(() -> {
			productView = new ProductViewSwing();
			productView.setProductController(productController);
			return productView;
		});
		window = new FrameFixture(robot(), productView);
		window.show();
	}
	
	@Test
	public void testControlsInitialState() {
		// left side
		window.list("productList");
		// right side
		window.label(JLabelMatcher.withName("lblAddNewProduct"));
		window.textBox("nameTextBox").requireEnabled();
		window.textBox("quantityTextBox").requireEnabled();
		window.button(JButtonMatcher.withName("removeProductButton")).requireDisabled();
		window.button(JButtonMatcher.withName("addProductButton")).requireDisabled();
		window.label(JLabelMatcher.withName("lblEditExistingProduct"));
		window.textBox("editPropertiesTextBox").requireEnabled();
		window.radioButton("nameEditRadioButton").requireEnabled();
		window.radioButton("quantityEditRadioButton").requireEnabled();
		window.button(JButtonMatcher.withName("editProductButton")).requireDisabled();
		window.label(JLabelMatcher.withName("lblInfoMessages"));
		window.label(JLabelMatcher.withName("lblMessage"));
	}
	
	@Test
	public void testAddProductButtonShouldBeEnabledWhenBothFieldsGetFilled() {
		window.textBox("nameTextBox").enterText(testProductName1);
		window.textBox("quantityTextBox").enterText(String.valueOf(testProductQuantity1));
		window.button(JButtonMatcher.withName("addProductButton")).requireEnabled();
	}
	
	@Test
	public void testAddProductButtonShouldBeDisabledWhenAtLeastOneOfTheFieldsIsEmpty() {
		window.textBox("nameTextBox").enterText(testProductName1);
		window.textBox("quantityTextBox").enterText(" ");
		window.button(JButtonMatcher.withName("addProductButton")).requireDisabled();

		window.textBox("nameTextBox").setText("");
		window.textBox("quantityTextBox").setText("");
		
		window.textBox("nameTextBox").enterText(" ");
		window.textBox("quantityTextBox").enterText(String.valueOf(testProductQuantity1));
		window.button(JButtonMatcher.withName("addProductButton")).requireDisabled();
	}
	
	@Test
	public void testAddProductButtonShouldBeDisabledWhenQuantityInsertedIsNotInteger() {
		window.textBox("nameTextBox").enterText(testProductName1);
		window.textBox("quantityTextBox").enterText("string");
		window.button(JButtonMatcher.withName("addProductButton")).requireDisabled();
	}
	
	@Test
	public void testRemoveProductButtonShouldBeEnabledWhenOneProductIsSelectedOnTheList() {
		GuiActionRunner.execute(
			() -> productView.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.list("productList").selectItem(0);
		window.button(JButtonMatcher.withName("removeProductButton")).requireEnabled();
		window.list("productList").clearSelection();
		window.button(JButtonMatcher.withName("removeProductButton")).requireDisabled();
	}
	
	@Test
	public void testRemoveProductButtonShouldBeDisabledWhenNoProductIsSelected() {
		GuiActionRunner.execute(
			() -> productView.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.button(JButtonMatcher.withName("removeProductButton")).requireDisabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeEnabledWhenNameIsSelectedAndProductIsSelectedAndFieldIsFilledWithString() {
		GuiActionRunner.execute(
			() -> productView.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.list("productList").selectItem(0);
		window.radioButton("nameEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(testProductName2);
		window.button(JButtonMatcher.withName("editProductButton")).requireEnabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeDisabledWhenNameIsSelectedAndProductIsNotSelectedAndFieldIsFilledWithString() {
		GuiActionRunner.execute(
			() -> productView.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.radioButton("nameEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(testProductName2);
		window.button(JButtonMatcher.withName("editProductButton")).requireDisabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeDisabledWhenNameIsSelectedAndProductIsSelectedAndFieldIsFilledWithInteger() {
		GuiActionRunner.execute(
			() -> productView.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.list("productList").selectItem(0);
		window.radioButton("nameEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(String.valueOf(testProductQuantity2));
		window.button(JButtonMatcher.withName("editProductButton")).requireDisabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeDisabledWhenNameIsSelectedAndProductIsNotSelectedAndFieldIsFilledWithInteger() {
		GuiActionRunner.execute(
			() -> productView.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.radioButton("nameEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(String.valueOf(testProductQuantity2));
		window.button(JButtonMatcher.withName("editProductButton")).requireDisabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeEnabledWhenQuantityIsSelectedAndProductIsSelectedAndFieldIsFilledWithInteger() {
		GuiActionRunner.execute(
			() -> productView.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.list("productList").selectItem(0);
		window.radioButton("quantityEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(String.valueOf(testProductQuantity2));
		window.button(JButtonMatcher.withName("editProductButton")).requireEnabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeDisabledWhenQuantityIsSelectedAndProductIsNotSelectedAndFieldIsFilledWithInteger() {
		GuiActionRunner.execute(
			() -> productView.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.radioButton("quantityEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(String.valueOf(testProductQuantity2));
		window.button(JButtonMatcher.withName("editProductButton")).requireDisabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeDisabledWhenQuantityIsSelectedAndProductIsSelectedAndFieldIsFilledWithString() {
		GuiActionRunner.execute(
			() -> productView.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.list("productList").selectItem(0);
		window.radioButton("quantityEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(testProductName1);
		window.button(JButtonMatcher.withName("editProductButton")).requireDisabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeDisaledWhenQuantityIsSelectedAndProductIsNotSelectedAndFieldIsFilledWithString() {
		GuiActionRunner.execute(
			() -> productView.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.radioButton("quantityEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(testProductName1);
		window.button(JButtonMatcher.withName("editProductButton")).requireDisabled();
	}
	
	@Test
	public void testListProductsShouldDisplayAllProductsInTheProductList() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		Product product2 = new Product(testProductName2, testProductQuantity2);
		GuiActionRunner.execute(
			() -> productView.listProducts(Arrays.asList(product1, product2))
		);
		String[] listProducts = window.list().contents();
		assertThat(listProducts).containsExactly(product1.toString(), product2.toString());
	}
	
	@Test
	public void testShowErrorShouldShowInfoOnTheDedicatedErrorLabel() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productView.showError("Error message", product1, "error")
		);
		window.label("lblMessage").requireText("Error message: " + product1.getName());
	}
	
	@Test
	public void testShowErrorShowsInfoInRedIfMessageIsError() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productView.showError("Error message", product1, "error")
		);
		window.label("lblMessage").requireText("Error message: " + product1.getName());
		window.label("lblMessage").foreground().requireEqualTo(Color.RED);
	}
	
	@Test
	public void testShowErrorShowsInfoInBlackIfMessageIsInfo() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productView.showError("Error message", product1, "info")
		);
		window.label("lblMessage").requireText("Error message: " + product1.getName());
		window.label("lblMessage").foreground().requireEqualTo(Color.BLACK);
	}
	
	@Test
	public void testProductAddedShouldAddProductToTheListAndCleanupErrorMessage() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productView.productAdded(product1)
		);
		String[] listProducts = window.list().contents();
		assertThat(listProducts).containsExactly(product1.toString());
		window.label("lblMessage").requireText(" ");
		window.textBox("nameTextBox").requireText("");
		window.textBox("quantityTextBox").requireText("");
	}
	
	@Test
	public void testProductDeletedShouldRemoveProductFromTheListAndCleanupErrorMessage() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		Product product2 = new Product(testProductName2, testProductQuantity2);
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Product> listProductModel = productView.getListProductModel();
				listProductModel.addElement(product1);
				listProductModel.addElement(product2);
			}
		);
		GuiActionRunner.execute(
			() -> productView.productDeleted(product2)
		);
		String[] listProducts = window.list().contents();
		assertThat(listProducts).containsExactly(product1.toString());
		window.label("lblMessage").requireText(" ");		
	}
	
	@Test
	public void testProductEditedShouldEditProductOnTheListAndCleanupErrorMessage() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		Product product2 = new Product(testProductName2, testProductQuantity1);
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Product> listProductModel = productView.getListProductModel();
				listProductModel.addElement(product1);
			}
		);
		GuiActionRunner.execute(
			() -> productView.productEdited(product1, product2)
		);
		String[] listProducts = window.list().contents();
		assertThat(listProducts).containsExactly(product2.toString());
		window.label("lblMessage").requireText(" ");
		window.textBox("editPropertiesTextBox").requireText("");
	}
	
	// Controller delegation tests
	
	@Test
	public void testAddProductButtonShouldDelegateToControllerAddProduct() {
		window.textBox("nameTextBox").enterText(testProductName1);
		window.textBox("quantityTextBox").enterText(String.valueOf(testProductQuantity1));
		window.button("addProductButton").click();
		verify(productController).addProduct(new Product(testProductName1, testProductQuantity1));
	}
	
	@Test
	public void testRemoveProductButtonShouldDelegateToControllerRemoveProduct() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Product> listProductModel = productView.getListProductModel();
				listProductModel.addElement(product1);
			}
		);
		window.list("productList").selectItem(0);
		window.button("removeProductButton").click();
		verify(productController).removeProduct(product1);
	}
	
	@Test
	public void testEditProductButtonShouldDelegateToControllerAlterProductNameIfNameIsSelected() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Product> listProductModel = productView.getListProductModel();
				listProductModel.addElement(product1);
			}
		);
		window.list("productList").selectItem(0);
		window.radioButton("nameEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(testProductName2);
		window.button("editProductButton").click();
		verify(productController).updateProductName(product1, testProductName2);
	}
	
	@Test
	public void testEditProductButtonShouldDelegateToControllerAlterProductQuantityIfQuantityIsSelected() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Product> listProductModel = productView.getListProductModel();
				listProductModel.addElement(product1);
			}
		);
		window.list("productList").selectItem(0);
		window.radioButton("quantityEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(String.valueOf(testProductQuantity2));
		window.button("editProductButton").click();
		verify(productController).updateProductQuantity(product1, testProductQuantity2);
	}
}
