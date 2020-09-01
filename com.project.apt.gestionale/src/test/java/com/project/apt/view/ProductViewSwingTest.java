package com.project.apt.view;

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

@RunWith(GUITestRunner.class)
public class ProductViewSwingTest extends AssertJSwingJUnitTestCase {
	
	private FrameFixture window;
	
	private ProductViewSwing productViewSwing;
	
	@Mock
	private ProductController productController;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		GuiActionRunner.execute(() -> {
			productViewSwing = new ProductViewSwing();
			productViewSwing.setProductController(productController);
			return productViewSwing;
		});
		window = new FrameFixture(robot(), productViewSwing);
		window.show();
	}
	
	@Test
	public void testControlsInitialState() {
		window.label(JLabelMatcher.withName("productListLabel"));
		
	}

}
