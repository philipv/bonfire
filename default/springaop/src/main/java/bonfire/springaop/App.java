package bonfire.springaop;

import java.sql.SQLException;

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
    public static void main( String[] args )
    {
        try {
        	BeanFactory factory = new ClassPathXmlApplicationContext("classpath:basic.xml");
        	MyTestTarget myTestTarget = (MyTestTarget)factory.getBean("myBasicTarget");
        	myTestTarget.transfer();
        	System.out.println("----------------------------------------------------");
        	MySaveTarget mySaveTarget = (MySaveTarget)factory.getBean("mySaveTarget");
        	mySaveTarget.save();
        	System.out.println("----------------------------------------------------");
        	myTestTarget.getName();
        	System.out.println("----------------------------------------------------");
			myTestTarget.getAge("Test");
		} catch (Exception e) {
		}
        
    }
}
