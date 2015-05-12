package com.bonfire.executor.distibutor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bonfire.executor.work.Work;
import com.bonfire.executor.worker.EventWorker;
import com.bonfire.executor.worker.Worker;
import com.bonfire.injector.DependencyInjector;

public class EventDistributor<T extends Work> implements Worker<T>{
	
	int cores = 1;
	List<EventWorker<? super T>> workers = new ArrayList<>();
	
	public EventDistributor(int cores, DependencyInjector injector) {
		this.cores = cores;
		for(int i=0;i<this.cores;i++){
			workers.add(injector.createWorker());
		}
	}
	
	@Override
	public void submit(T work) {
		workers.get(generateHash(work)).submit(work);		
	}


	public int generateHash(T work) {
		return work.getKey().hashCode()%cores;
	}

	@Override
	public void submit(Date executionTime, T work) {
		workers.get(generateHash(work)).submit(executionTime, work);
	}

	@Override
	public void scheduleUntil(T work, long initialDelay, long execIntervalMs,
			long cancelTime) {
		workers.get(generateHash(work)).scheduleUntil(work, initialDelay, execIntervalMs, cancelTime);
	}

	@Override
	public void scheduleUntil(T work, Date startTime, long execIntervalMs,
			Date endTime) {
		workers.get(generateHash(work)).scheduleUntil(work, startTime, execIntervalMs, endTime);
	}

	@Override
	public void destroy() {
		for(EventWorker<? super T> worker:workers){
			worker.destroy();
		}
	}

}
