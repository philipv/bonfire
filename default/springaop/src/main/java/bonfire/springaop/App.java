package bonfire.springaop;

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
        BeanFactory factory = new ClassPathXmlApplicationContext("classpath:basic.xml");
        MyTestTarget myTestTarget = (MyTestTarget)factory.getBean("myBasicTarget");
        myTestTarget.transfer();
        
        MySaveTarget mySaveTarget = (MySaveTarget)factory.getBean("mySaveTarget");
        mySaveTarget.save();
    }
}
