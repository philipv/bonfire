package bonfire.springaop.aspects;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class BasicAdvice {
	@Before("bonfire.springaop.aspects.BasicAspect.anyOldTransfer()")
	//@Before("execution(* transfer(..))")
	public void before() {
		System.out.println("bonfire.springaop.aspects.BasicAdvice.before");
	}
	
	@After("bonfire.springaop.aspects.BasicAspect.anyOldTransfer()")
	public void after() {
		System.out.println("bonfire.springaop.aspects.BasicAdvice.after");
	}
	
	
}
