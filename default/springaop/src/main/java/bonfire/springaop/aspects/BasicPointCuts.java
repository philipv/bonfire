package bonfire.springaop.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class BasicPointCuts {
	@Pointcut("execution(* transfer*(..))")// the pointcut expression
	public void anyTransfer() {}
	
	@Pointcut("within(bonfire.springaop.targets.MySaveTarget)")
	public void anyMySaveTargetMethod(){}
	
	@Pointcut("execution(* bonfire.springaop.targets.*.get*(..))")
	public void anyGetter(){}
	
}
