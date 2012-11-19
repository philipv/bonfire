package com.bonfire.task;

import org.junit.Assert;
import org.junit.Test;

import com.bonfire.BaseTest;
import com.bonfire.data.Position;

public class PositionProcessTaskTest extends BaseTest{
	@Test
	public void testNormalValue(){
		for(int i=1;i<=10;i++){
			Runnable task = factoryUtility.createTaskFromConsole(new Position("USD", new Double(i*10)), positions);
			task.run();
		}
		assertAggregatedResult("USD", 550.0);
		Runnable task = factoryUtility.createTaskFromConsole(new Position("USD", new Double(-550.0)), positions);
		task.run();
		assertMissingResult("USD");
		Assert.assertEquals(0, positions.size());
		assertMissingResult("INR");
	}
}
