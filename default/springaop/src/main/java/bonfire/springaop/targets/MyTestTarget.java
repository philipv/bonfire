package bonfire.springaop.targets;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyTestTarget {
	
	private static final Logger logger = LoggerFactory.getLogger(MyTestTarget.class.getCanonicalName());
	
	public void transfer(){
		logger.info("transfer called");
	}
	
	public String getName(){
		return "TestName";
	}
	
	public Integer getAge(String id) throws SQLException{
		throw new SQLException("Couldn't find age for id " + id);
	}
	
	public String echo(String input){
		return "MyTestTarget - " + input;
	}
	
}
