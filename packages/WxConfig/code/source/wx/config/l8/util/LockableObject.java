package wx.config.l8.util;

import java.util.concurrent.locks.StampedLock;
import java.util.function.Function;
import java.util.function.Supplier;


public class LockableObject<O> {
	@FunctionalInterface
	public static interface Runnable<T> {
		public void run(T pObject) throws Exception;
	}
	@FunctionalInterface
	public static interface Callable<S,T> {
		public S call(T pObject) throws Exception;
	}
	private O object;
	private final StampedLock lock;

	public LockableObject(O pObject) {
		object = pObject;
		lock = new StampedLock();
	}

	public void run(Runnable<O> pRunnable) {
		final long l = lock.readLock();
		try {
			pRunnable.run(object);
		} catch (Throwable t) {
			throw Exceptions.show(t);
		} finally {
			lock.unlock(l);
		}
	}

	public <T> T call(Callable<T,O> pCallable) {
		final long l = lock.readLock();
		try {
			return pCallable.call(object);
		} catch (Throwable t) {
			throw Exceptions.show(t);
		} finally {
			lock.unlock(l);
		}
	}

	public void runExclusive(Runnable<O> pRunnable) {
		final long l = lock.writeLock();
		try {
			pRunnable.run(object);
		} catch (Throwable t) {
			throw Exceptions.show(t);
		} finally {
			lock.unlock(l);
		}
	}

	public <T> T callExlusive(Callable<T,O> pCallable) {
		final long l = lock.writeLock();
		try {
			return pCallable.call(object);
		} catch (Throwable t) {
			throw Exceptions.show(t);
		} finally {
			lock.unlock(l);
		}
	}

	public void replace(O pObject) {
		final long l = lock.writeLock();
		try {
			object = pObject;
		} catch (Throwable t) {
			throw Exceptions.show(t);
		} finally {
			lock.unlock(l);
		}
	}

	public O get(Function<O,O> pCloner) {
		return call((o) -> pCloner.apply(o));
	}
}
