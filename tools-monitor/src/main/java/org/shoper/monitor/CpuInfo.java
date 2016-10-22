package org.shoper.monitor;
/**
 * cpu info
 * 
 * @author ShawnShoper
 *
 */
public class CpuInfo
{
	private int totalSockets;
	private int coresPerSocket;
	private int cacheSize;
	private String model;
	private int totalCores;
	private String vendor;
	private int mhz;
	public int getTotalSockets()
	{
		return totalSockets;
	}
	public void setTotalSockets(int totalSockets)
	{
		this.totalSockets = totalSockets;
	}
	public int getCoresPerSocket()
	{
		return coresPerSocket;
	}
	public void setCoresPerSocket(int coresPerSocket)
	{
		this.coresPerSocket = coresPerSocket;
	}
	public int getCacheSize()
	{
		return cacheSize;
	}
	public void setCacheSize(int cacheSize)
	{
		this.cacheSize = cacheSize;
	}
	public String getModel()
	{
		return model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public int getTotalCores()
	{
		return totalCores;
	}
	public void setTotalCores(int totalCores)
	{
		this.totalCores = totalCores;
	}
	public String getVendor()
	{
		return vendor;
	}
	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}
	public int getMhz()
	{
		return mhz;
	}
	public void setMhz(int mhz)
	{
		this.mhz = mhz;
	}
}
