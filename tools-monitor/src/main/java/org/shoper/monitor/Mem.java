package org.shoper.monitor;
/**
 * 内存信息...
 * @author ShawnShoper
 *
 */
public class Mem {
	private long total;
	private long use;
	private long free;
	private double usedPercent;
	private double freePercent;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getUse() {
		return use;
	}
	public void setUse(long use) {
		this.use = use;
	}
	public long getFree() {
		return free;
	}
	public void setFree(long free) {
		this.free = free;
	}
	public double getUsedPercent() {
		return usedPercent;
	}
	public void setUsedPercent(double usedPercent) {
		this.usedPercent = usedPercent;
	}
	public double getFreePercent() {
		return freePercent;
	}
	public void setFreePercent(double freePercent) {
		this.freePercent = freePercent;
	}
	@Override
	public String toString() {
		return "Mem [total=" + total + ", use=" + use + ", free=" + free
				+ ", usedPercent=" + usedPercent + ", freePercent="
				+ freePercent + "]";
	}
	
}
