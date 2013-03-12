package bonfire.springaop.targets;

import java.sql.SQLException;

import org.apache.log4j.Logger;

public class MyTestTarget {
	
	private static final Logger logger = Logger.getLogger(MyTestTarget.class.getCanonicalName());
	
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
