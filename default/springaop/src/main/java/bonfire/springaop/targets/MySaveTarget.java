package bonfire.springaop.targets;

import org.apache.log4j.Logger;

public class MySaveTarget {
	private static final Logger logger = Logger.getLogger(MySaveTarget.class.getCanonicalName());
	
	public void save(){
		logger.info("save called");
	}
}
