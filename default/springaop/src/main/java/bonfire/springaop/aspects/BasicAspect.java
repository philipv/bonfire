package bonfire.springaop.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class BasicAspect {
	@Pointcut("execution(* transfer(..))")// the pointcut expression
	public void anyOldTransfer() {}
}
