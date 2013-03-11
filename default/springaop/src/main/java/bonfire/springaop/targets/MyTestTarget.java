package bonfire.springaop.targets;

import java.sql.SQLException;

public class MyTestTarget {
	public void transfer(){
		System.out.println("bonfire.springaop.targets.MyTestTarget.transfer called");
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
