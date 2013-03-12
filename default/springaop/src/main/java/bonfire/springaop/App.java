package bonfire.springaop;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import bonfire.springaop.targets.MySaveTarget;
import bonfire.springaop.targets.MyTestTarget;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger logger = Logger.getLogger(App.class.getCanonicalName());
	public static void main( String[] args )
    {
        try {
        	BeanFactory factory = new ClassPathXmlApplicationContext("classpath:basic.xml");
        	MyTestTarget myTestTarget = (MyTestTarget)factory.getBean("myBasicTarget");
        	myTestTarget.transfer();
        	logger.info("----------------------------------------------------");
        	MySaveTarget mySaveTarget = (MySaveTarget)factory.getBean("mySaveTarget");
        	mySaveTarget.save();
        	logger.info("----------------------------------------------------");
        	myTestTarget.getName();
        	logger.info("----------------------------------------------------");
        	myTestTarget.echo("hello");
        	logger.info("----------------------------------------------------");
			myTestTarget.getAge("Test");
		} catch (Exception e) {
		}
        
    }
}
