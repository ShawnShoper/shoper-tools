package org.shoper.concurrent.future;


import org.omg.CORBA.SystemException;

import java.util.concurrent.Callable;

/**
 * 自定义支持异步回调的Callable
 * 
 * @author ShawnShoper
 *
 * @param <T>
 */
public abstract class AsynCallable<T, K> implements Callable<T>
{
	private FutureCallback<T, K> callBack;
	public AsynCallable<T, K> setCallback(FutureCallback<T, K> callback)
	{
		this.callBack = callback;
		return this;
	}

	public abstract T run(K param) throws Exception;
	@Override
	public T call() throws Exception
	{
		T object = null;
		K k = null;
		try
		{
			if (callBack != null)
			{
				try
				{
					k = callBack.preDo();
				} catch (Exception e)
				{
					callBack.fail(new RuntimeException(
							"Initializer Exception...init failed..."));
					return object;
				}
			}
			object = run(k);
			if (callBack != null)
				callBack.success(object);
		} catch (Exception e)
		{
			if (callBack != null)
				callBack.fail(e);
			else
			{
				e.printStackTrace();
				throw e;
			}
		} finally
		{
			if (callBack != null)
				callBack.done();
		}
		return object;
	}
}