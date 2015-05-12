package com.bonfire.executor.worker;

import java.util.Date;

public interface Worker<T extends Runnable> {

	public abstract void submit(T work);

	public abstract void submit(Date executionTime, T work);

	public abstract void scheduleUntil(T work, long initialDelay,
			long execIntervalMs, long cancelTime);

	public abstract void scheduleUntil(T work, Date startTime,
			long execIntervalMs, Date endTime);

	public abstract void destroy();

}