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

@Aspect
public class BasicAdvice {
	@Before("bonfire.springaop.aspects.BasicPointCuts.anyTransfer()")
	//@Before("execution(* transfer(..))")
	public void beforeExecution() {
		System.out.println("bonfire.springaop.aspects.BasicAdvice.before");
	}
	
	@After("bonfire.springaop.aspects.BasicPointCuts.anyTransfer()")
	public void afterExecution() {
		System.out.println("bonfire.springaop.aspects.BasicAdvice.after");
	}
	
	@Before("bonfire.springaop.aspects.BasicPointCuts.anyMySaveTargetMethod()")
	public void beforeWithin() {
		System.out.println("bonfire.springaop.aspects.BasicAdvice.beforeWithin");
	}
	
	@AfterReturning(pointcut="bonfire.springaop.aspects.BasicPointCuts.anyGetter()",
			returning="retVal")
	public void afterReturning(String retVal){
		System.out.println("bonfire.springaop.aspects.BasicAdvice.afterReturning: Got value : " + retVal);
	}
	
	@AfterThrowing(throwing="ex", 
			pointcut="bonfire.springaop.aspects.BasicPointCuts.anyGetter()")
	public void afterThrowing(JoinPoint joinPoint, SQLException ex){
		System.out.println("bonfire.springaop.aspects.BasicAdvice.afterThrowing: Thrown exception " + ex.toString());
		System.out.println("bonfire.springaop.aspects.BasicAdvice.afterThrowing: Another way to get the input parameters : " + Arrays.toString(joinPoint.getArgs()));
	}
	
	@Around("bonfire.springaop.aspects.BasicPointCuts.echoMethod() && args(input)")
	public String around(ProceedingJoinPoint pjp, String input) throws Throwable {
		System.out.println("bonfire.springaop.aspects.BasicAdvice.around : Input :" + Arrays.toString(pjp.getArgs()));
		System.out.println("bonfire.springaop.aspects.BasicAdvice.around : Another way to get the input parameters : " + input);
		String retVal = (String)pjp.proceed();
		System.out.println("bonfire.springaop.aspects.BasicAdvice.around : Output :" + retVal);
		return retVal;
	}
	
}
