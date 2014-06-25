package com.simulator.processor;

import java.util.concurrent.Future;

public interface IAsyncProcessor<T, R> {

	Future<R> process(T marketIdentity);

	void shutdown();

}
