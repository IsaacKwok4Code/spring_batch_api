package com.learnspringbatch.springbatchdemo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.learnspringbatch.springbatchdemo.domain.Product;
import com.learnspringbatch.springbatchdemo.domain.ProductFieldSetMapper;
import com.learnspringbatch.springbatchdemo.reader.ProductNameItemReader;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public ItemReader<String> itemReader() {
		List<String> productList = new ArrayList<>();
		productList.add("Product 1");
		productList.add("Product 2");
		productList.add("Product 3");
		productList.add("Product 4");
		productList.add("Product 5");
		productList.add("Product 6");
		productList.add("Product 7");
		productList.add("Product 8");
		return new ProductNameItemReader(productList);
	}
	
	@Bean
	public ItemReader<Product> flatFileItemReader() {
		FlatFileItemReader<Product> itemReader = new FlatFileItemReader<>();
		// skip for title row
		itemReader.setLinesToSkip(1);
		// data source
		itemReader.setResource(new ClassPathResource("/data/Product_Details.csv"));
		// read line mapper
		DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();
		// read field mapper from line mapper
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		// based on column name
		lineTokenizer.setNames("product_id", "product_name", "product_category", "product_price");
		
		lineMapper.setLineTokenizer(lineTokenizer);
		// the object mapper is custom code in domain package
		lineMapper.setFieldSetMapper(new ProductFieldSetMapper());
		itemReader.setLineMapper(lineMapper);
		return itemReader;
	}
	
	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("chunkBasedStep1")
				.<Product,Product>chunk(3)
				.reader(flatFileItemReader())
				.writer(new ItemWriter<Product>() {

					@Override
					public void write(List<? extends Product> items) throws Exception {
						System.out.println("Chunk-processing Started");
						// print the result for each step
						items.forEach(System.out::println);
						System.out.println("Chunk-processing Ended");
					}
				}).build();
	}
	
	@Bean
	public Job firstJob() {
		return this.jobBuilderFactory.get("job1")
				.start(step1())
				.build();
	}
}
