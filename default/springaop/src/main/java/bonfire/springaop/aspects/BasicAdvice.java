package bonfire.springaop.aspects;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
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
	
}
