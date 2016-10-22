package org.shoper.commons;

import org.shoper.commons.DateUtil.TimeElapse;

public class TimeUtil
{
	public static class DiffTime
	{
		private long lastTime;
		public DiffTime reset()
		{
			lastTime = 0L;
			return this;
		}
		public DiffTime set(long time)
		{
			lastTime = time - lastTime;
			return this;
		}
		public TimeElapse elapse2str(long time)
		{
			return DateUtil.TimeToStr(lastTime);
		}
		public TimeElapse elapse2str()
		{
			return DateUtil.TimeToStr(System.currentTimeMillis() - lastTime);
		}
		public long elapse2time()
		{
			return System.currentTimeMillis() - lastTime;
		}
	}
}
