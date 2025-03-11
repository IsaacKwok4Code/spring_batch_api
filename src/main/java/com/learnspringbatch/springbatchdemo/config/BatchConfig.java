package com.learnspringbatch.springbatchdemo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learnspringbatch.springbatchdemo.reader.ProductNameItemReader;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	// scheduler -> job launcher -> job 1,2,3 -> job instance for each -> job execution
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	// Bean for custom decider and listener
	
	@Bean
	public ItemReader<String> itemReader() {
		List<String> productList = new ArrayList<>();
		productList.add("Product_1");
		productList.add("Product_2");
		productList.add("Product_3");
		productList.add("Product_4");
		productList.add("Product_5");
		return new ProductNameItemReader(productList);
	}
	
	@Bean
	public Step stepOne() {
		return this.stepBuilderFactory.get("chunkBasedStep1")
				.<String,String>chunk(3) // process 3 items for 1 chuck
				.reader(itemReader())
				.writer(new ItemWriter<String>() {

					@Override
					public void write(List<? extends String> items) throws Exception {
						System.out.println("Chunk-processing Started");
						items.forEach(System.out::println);
						System.out.println("Chunk-processing Ended");
					}
				}).build();
	}
	
	@Bean
	public Job firstJob() {
		return this.jobBuilderFactory.get("job1")
				.start(stepOne())
				.build();
	}

//	@Bean
//	public Step stepOne() {
//		return this.stepBuilderFactory.get("step1").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				System.out.println("step1 executed!!");
//				return RepeatStatus.FINISHED;
//			}
//		}).build();
//	}
//	
//	@Bean
//	public Step stepTwo() {
//		return this.stepBuilderFactory.get("step2").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				boolean isStepTwoSuccess = true;
//				if(isStepTwoSuccess) {
//					System.out.println("step2 executed!!");
//				}
//				return RepeatStatus.FINISHED;
//			}
//		}).build();
//	}
//	
//	@Bean
//	public Step stepThree() {
//		return this.stepBuilderFactory.get("step3").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				System.out.println("step3 executed!!");
//				return RepeatStatus.FINISHED;
//			}
//		}).build();
//	}
	
//	@Bean
//	public Step stepFour() {
//		return this.stepBuilderFactory.get("step4").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				System.out.println("step4 executed!!");
//				return RepeatStatus.FINISHED;
//			}
//		}).build();
//	}

	// .preventRestart - stop re-execute
	// sequence stepping
	// .start(stepOne()).on("COMPLETED").to(stepTwo())
	// 					.form(stepTwo()).on("COMPLETED").to(stepThree())
	//					.end()
	
	//conditional stepping
	// .form(stepTwo()).on("COMPLETED").to(stepThree())
	// .form(stepTwo()).on("FAILED").to(stepFour())
	
	// use.on("*") mean catch method
	
	// listener package for custom condition
	
	// step-by-step batch
//	@Bean
//	public Job firstJob() {
//		return this.jobBuilderFactory.get("job1")
//				.preventRestart()
//				.start(stepOne())
//				.next(stepTwo())
//				.next(stepThree())
//				.build();
//	}
}
