package bonfire.springaop.aspects;

import java.sql.SQLException;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class BasicAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(BasicAdvice.class.getCanonicalName());
	
	@Before("bonfire.springaop.aspects.BasicPointCuts.anyTransfer()")
	//@Before("execution(* transfer(..))")
	public void beforeExecution() {
		logger.info("before called");
	}
	
	@After("bonfire.springaop.aspects.BasicPointCuts.anyTransfer()")
	public void afterExecution() {
		logger.info("after called");
	}
	
	@Before("bonfire.springaop.aspects.BasicPointCuts.anyMySaveTargetMethod()")
	public void beforeWithin() {
		logger.info("beforeWithin called");
	}
	
	@AfterReturning(pointcut="bonfire.springaop.aspects.BasicPointCuts.anyGetter()",
			returning="retVal")
	public void afterReturning(String retVal){
		logger.info("Got value : {}", retVal);
	}
	
	@AfterThrowing(throwing="ex", 
			pointcut="bonfire.springaop.aspects.BasicPointCuts.anyGetter()")
	public void afterThrowing(JoinPoint joinPoint, SQLException ex){
		logger.info("Thrown exception {}", ex.toString());
		logger.info("Another way to get the input parameters : {}", Arrays.toString(joinPoint.getArgs()));
	}
	
	@Around("bonfire.springaop.aspects.BasicPointCuts.echoMethod() && args(input)")
	public String around(ProceedingJoinPoint pjp, String input) throws Throwable {
		logger.info("Input : {}", Arrays.toString(pjp.getArgs()));
		logger.info("Another way to get the input parameters : {}", input);
		String retVal = (String)pjp.proceed();
		logger.info("Output : {}", retVal);
		return retVal;
	}
	
}
