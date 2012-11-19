package com.bonfire.task;

import org.junit.Test;

import com.bonfire.BaseTest;

public class EvaluationTaskTest extends BaseTest{

	@Test
	public void testEmptyPositions(){
		EvaluationTask evaluationTask = new EvaluationTask(positions);
		evaluationTask.run();
	}
	
	@Test
	public void testAggregatedPositions(){
		positions.put("USD", 23.45);
		positions.put("HKD", 1764.6);
		EvaluationTask evaluationTask = new EvaluationTask(positions);
		evaluationTask.run();
	}
}
