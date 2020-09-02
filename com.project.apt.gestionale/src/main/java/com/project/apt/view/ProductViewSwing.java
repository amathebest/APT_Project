package com.project.apt.view;

import java.util.List;

import javax.swing.JFrame;

import com.project.apt.controller.ProductController;
import com.project.apt.model.Product;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;

public class ProductViewSwing extends JFrame implements ProductView {
	
	private static final long serialVersionUID = 1L;
	
	private transient ProductController productController;

	private JList<Product> listProduct;
	private DefaultListModel<Product> listProductModel;
	
	public void setProductController(ProductController productController) {
		this.productController = productController;
	}
	
	public ProductViewSwing() {
		setTitle("Product Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		setLayout(new BorderLayout());
		
		JPanel mainContent = new JPanel();
		mainContent.setLayout(new GridLayout(0, 2));
		mainContent.add(createLeftPanel());
		mainContent.add(createRightPanel());
		add(mainContent);

		pack();
        setVisible(true);
		
		
		
		
		
		
		
		
		
	}
	
	protected JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel content = new JPanel(new GridBagLayout());
        JPanel left = new JPanel(new GridLayout(0, 2));
        
        GridBagConstraints gbc_leftSide = new GridBagConstraints();
        gbc_leftSide.insets = new Insets(0, 0, 5, 5);
        gbc_leftSide.anchor = GridBagConstraints.EAST;
        gbc_leftSide.gridx = 0;
        gbc_leftSide.gridy = 0;
        
        

		JLabel lblProductList = new JLabel("Product list");
		lblProductList.setName("productListLabel");
		left.add(lblProductList);
		
		JScrollPane dataDisplay = new JScrollPane();
		listProductModel = new DefaultListModel<>();
		listProduct = new JList<>(listProductModel);
		listProduct.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listProduct.setName("productList");
		dataDisplay.setViewportView(listProduct);
        
        left.add(dataDisplay);
        
        content.add(left, gbc_leftSide);
        panel.add(content);
        
		return panel;
	}
	
	protected JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel content = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc_RightSide = new GridBagConstraints();
        gbc_RightSide.insets = new Insets(0, 0, 5, 5);
        gbc_RightSide.anchor = GridBagConstraints.EAST;
        gbc_RightSide.gridx = 0;
        gbc_RightSide.gridy = 0;
        
        
        
        
		
		return panel;
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
