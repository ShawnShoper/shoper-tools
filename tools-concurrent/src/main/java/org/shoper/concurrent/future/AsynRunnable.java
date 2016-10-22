package org.shoper.concurrent.future;

/**
 * 自定义支持异步回调的Callable
 * 
 * @author ShawnShoper
 *
 */
public abstract class AsynRunnable implements Runnable
{
	private RunnableCallBack callBack;
	public AsynRunnable setCallback(RunnableCallBack callback)
	{
		this.callBack = callback;
		return this;
	}

	public abstract void call() throws Exception;
	@Override
	public void run()
	{
		try
		{
			if (callBack != null)
				if (!callBack.preDo())
					callBack.fail(new RuntimeException(
							"Initializer Exception...init failed..."));
			call();
			if (callBack != null)
				callBack.success();
		} catch (Exception e)
		{
			if (callBack != null)
				callBack.fail(e);
			else
			{
				e.printStackTrace();
			}
		} finally
		{
			if (callBack != null)
				callBack.done();
		}
	}
}