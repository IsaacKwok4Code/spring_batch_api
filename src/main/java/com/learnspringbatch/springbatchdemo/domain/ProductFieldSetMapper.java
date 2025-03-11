package com.learnspringbatch.springbatchdemo.domain;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ProductFieldSetMapper implements FieldSetMapper<Product> {
	
	// based on the CSV data
	@Override
	public Product mapFieldSet(FieldSet fieldSet) throws BindException {
		Product product = new Product();
		product.setProductId(fieldSet.readInt("product_id"));
		product.setProductName(fieldSet.readString("product_name"));
		product.setProductCategory(fieldSet.readString("product_category"));
		product.setProductPrice(fieldSet.readInt("product_price"));
		return product;
	}
}
