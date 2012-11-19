package com.bonfire.observer;


import org.junit.Assert;
import org.junit.Test;

import com.bonfire.BaseTest;
import com.bonfire.data.Position;

public class PositionListenerTest extends BaseTest {
	PositionListener positionListener = new PositionListener(positions); 
	@Test
	public void testPositionUpdate(){
		positionListener.update(null, new Position("USD", 123.4));
		assertAggregatedResult("USD", 123.4);
	}
	
	@Test
	public void testWrongUpdate(){
		positionListener.update(null, "USD 123.4");
		assertMissingResult("USD");
		Assert.assertEquals(0, positions.size());
	}
}
