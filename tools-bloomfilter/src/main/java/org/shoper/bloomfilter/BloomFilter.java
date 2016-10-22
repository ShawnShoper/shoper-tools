package org.shoper.bloomfilter;

import java.util.BitSet;

/**
 *
 * @author ShawnShoper
 * @date Feb 9, 2015
 * @description Bloom过滤器
 * @version v1.0
 * @since
 */
public class BloomFilter
{
	// 种子数大小
	private int defaultSize;
	private int basic;
	private BitSet bits;

	/**
	 * @description 初始化一个BloomFilter,并默认1024<<10大小
	 */
	public BloomFilter()
	{
		// 初始化一个Bitset
		this(2 << 20);
	}

	/**
	 * @description 初始化一个BloomFilter
	 * @param size
	 *            初始化的bitset大小
	 */
	public BloomFilter(int size)
	{
		super();
		this.defaultSize = size;
		this.basic = size - 1;
		this.bits = new BitSet(defaultSize);
	}

	/**
	 *
	 * @author ShawnShoper
	 * @date Feb 9, 2015
	 * @description 传入需要计算hash的key，然后分解成8位计算hash
	 * @param key
	 *            需要计算hash的key
	 * @return 返回一个计算完hashcode的8长的数组
	 */
	private int[] lrandom(String key)
	{
		int[] randomsum = new int[]{hashCode(key, 1), hashCode(key, 2),
				hashCode(key, 3), hashCode(key, 4), hashCode(key, 5),
				hashCode(key, 6), hashCode(key, 7), hashCode(key, 8)};
		return randomsum;
	}

	/**
	 * @author ShawnShoper
	 * @date Feb 9, 2015
	 * @description 计算hashcode
	 * @param key
	 *            需要计算的key
	 * @param Q
	 *            计算位码
	 * @return 根据位码计算得到的hashcode
	 */
	private int hashCode(String key, int Q)
	{
		int h = 0;
		int off = 0;
		char val[] = key.toCharArray();
		int len = key.length();
		for (int i = 0; i < len; i++)
		{
			h = (30 + Q) * h + val[off++];
		}
		return changeInteger(h);
	}

	/**
	 * @author ShawnShoper
	 * @date Feb 9, 2015
	 * @description 把计算好的hashcode与basic进行位运算得到有个新值
	 * @param h
	 * @return
	 */
	private int changeInteger(int h)
	{
		return basic & h;
	}

	/**
	 *
	 * @author ShawnShoper
	 * @date Feb 9, 2015
	 * @description 判断该key是否之前在bitset中添加
	 * @param key
	 * @return
	 */
	public boolean exist(String key)
	{
		int keyCode[] = lrandom(key);
		if (bits.get(keyCode[0]) && bits.get(keyCode[1]) && bits.get(keyCode[2])
				&& bits.get(keyCode[3]) && bits.get(keyCode[4])
				&& bits.get(keyCode[5]) && bits.get(keyCode[6])
				&& bits.get(keyCode[7]))
		{
			return true;
		}
		return false;
	}

	/**
	 * @author ShawnShoper
	 * @date Feb 9, 2015
	 * @description 向bitset中添加key,如果存在return
	 * @param key
	 *            需要添加的key
	 */
	public void add(String key)
	{
		// 检查是否之前存在该key。如果存在return
		if (exist(key))
			return;
		int keyCode[] = lrandom(key);
		bits.set(keyCode[0]);
		bits.set(keyCode[1]);
		bits.set(keyCode[2]);
		bits.set(keyCode[3]);
		bits.set(keyCode[4]);
		bits.set(keyCode[5]);
		bits.set(keyCode[6]);
		bits.set(keyCode[7]);
	}

	/**
	 *
	 * @author ShawnShoper
	 * @date Feb 9, 2015
	 * @description 通过指定的字符串传入并清除相关数据，使bitset对应的下标为false
	 * @param key
	 *            指定的字符串
	 * @return 如果存在该key返回true，不存在返回false
	 */
	public boolean remove(String key)
	{
		if (exist(key))
		{
			int keyCode[] = lrandom(key);
			bits.clear(keyCode[0]);
			bits.clear(keyCode[1]);
			bits.clear(keyCode[2]);
			bits.clear(keyCode[3]);
			bits.clear(keyCode[4]);
			bits.clear(keyCode[5]);
			bits.clear(keyCode[6]);
			bits.clear(keyCode[7]);
			return true;
		}
		return false;
	}

}
