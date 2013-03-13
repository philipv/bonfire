package bonfire.springaop.targets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MySaveTarget {
	private static final Logger logger = LoggerFactory.getLogger(MySaveTarget.class.getCanonicalName());
	
	public void save(){
		logger.info("save called");
	}
}
