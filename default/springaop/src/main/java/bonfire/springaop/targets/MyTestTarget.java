package bonfire.springaop.targets;

public class MyTestTarget {
	public void transfer(){
		System.out.println("bonfire.springaop.targets.MyTestTarget.transfer called");
	}
	
	public String getName(){
		return "TestName";
	}
	
}
