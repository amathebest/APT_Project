package com.project.apt.view;

import java.util.List;

import javax.swing.JFrame;

import com.project.apt.controller.ProductController;
import com.project.apt.model.Product;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Insets;

public class ProductViewSwing extends JFrame implements ProductView {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JPanel contentPane_1;
	
	private transient ProductController productController;
	
	public void setProductController(ProductController productController) {
		this.productController = productController;
	}
	
	public ProductViewSwing() {
		setTitle("Product Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane_1 = new JPanel();
		contentPane_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane_1);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		contentPane_1.setLayout(gridBagLayout);
		
		JLabel lblProductList = new JLabel("Product List");
		lblProductList.setName("productListLabel");
		GridBagConstraints gbc_lblProductList = new GridBagConstraints();
		gbc_lblProductList.insets = new Insets(0, 0, 0, 5);
		gbc_lblProductList.gridx = 0;
		gbc_lblProductList.gridy = 0;
		contentPane_1.add(lblProductList, gbc_lblProductList);
		
	}

	@Override
	public void listProducts(List<Product> products) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showError(String message, Product product, String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void productAdded(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void productEdited(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void productDeleted(Product product) {
		// TODO Auto-generated method stub
		
	}


}
