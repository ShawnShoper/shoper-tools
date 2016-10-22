package org.shoper.concurrent.future;
/**
 * Future回调
 * 
 * @author ShawnShoper
 *
 * @param <T>
 */
public interface FutureCallback<T, K>
{
	public default K preDo() throws Exception
	{
		return null;
	};
	public default void success(T result)
	{
	};
	public default void fail(Exception e)
	{
		e.printStackTrace();
	};
	public default void done()
	{
	};
}
