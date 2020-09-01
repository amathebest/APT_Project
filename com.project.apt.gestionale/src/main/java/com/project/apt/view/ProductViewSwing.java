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
	
	private ProductController productController;
	
	public void setProductController(ProductController productController) {
		this.productController = productController;
	}
	
	public ProductViewSwing() {
		setTitle("Product View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		contentPane.setLayout(gridBagLayout);
		
		JLabel lblProductManager = new JLabel("Product Manager");
		lblProductManager.setName("productManagerLabel");
		GridBagConstraints gbc_lblProductManager = new GridBagConstraints();
		gbc_lblProductManager.insets = new Insets(0, 0, 0, 5);
		gbc_lblProductManager.gridx = 6;
		gbc_lblProductManager.gridy = 0;
		contentPane.add(lblProductManager, gbc_lblProductManager);
		
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
