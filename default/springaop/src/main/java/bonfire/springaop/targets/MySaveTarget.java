package bonfire.springaop.targets;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class MySaveTarget {
	private static final Logger logger = LoggerFactory.getLogger(MySaveTarget.class.getCanonicalName());
	
	@Autowired
	private List<String> testTarget;
	
	public void save(){
		logger.info("save called with field {}", testTarget);
	}
	
}
