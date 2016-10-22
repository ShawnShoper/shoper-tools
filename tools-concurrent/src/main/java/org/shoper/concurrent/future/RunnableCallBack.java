package org.shoper.concurrent.future;
/**
 * call back
 * 
 * @author ShawnShoper
 *
 */
public abstract class RunnableCallBack
{
	protected boolean preDo()
	{
		return true;
	};
	protected void success()
	{

	}
	protected void fail(Exception e)
	{
		e.printStackTrace();
	};
	protected void done()
	{
	};
}
