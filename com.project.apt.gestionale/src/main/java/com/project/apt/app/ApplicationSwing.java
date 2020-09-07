package com.project.apt.app;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.awt.EventQueue;
import java.util.concurrent.Callable;

import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import com.project.apt.controller.ProductController;
import com.project.apt.repository.ProductRepositoryMongo;
import com.project.apt.view.ProductViewSwing;

@Command(mixinStandardHelpOptions = true)
public class ApplicationSwing implements Callable <Void>{
	
	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "school";

	@Option(names = { "--db-collection" }, description = "Collection name")
	private String collectionName = "student";
	
	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoClient client = new MongoClient(new ServerAddress(mongoHost, mongoPort));
				ProductRepositoryMongo productRepository = new ProductRepositoryMongo(client, databaseName, collectionName);
				ProductViewSwing productView = new ProductViewSwing();
				ProductController productController = new ProductController(productView, productRepository);
				productView.setProductController(productController);
				productView.setVisible(true);
				productController.allProducts();
			} catch (Exception e) {
				LoggerFactory.getLogger(getClass()).error("Got an exception:", e);
			}
		});
		return null;
	}
	
	public static void main(String[] args) {
		CommandLine.call(new ApplicationSwing(), args);
	}

}
