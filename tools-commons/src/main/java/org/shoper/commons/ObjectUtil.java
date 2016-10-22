package org.shoper.commons;

import org.apache.commons.lang3.SerializationException;

import java.io.*;

/**
 * Object util..
 * 
 * @author ShawnShoper
 *
 */
public class ObjectUtil
{
	/**
	 * 对象深度克隆....
	 * 
	 * @param srcObj
	 * @param clazz
	 * @return
	 */
	public static <T> T depthClone(Object srcObj, Class<T> clazz)
	{
		Object cloneObj = null;
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(out);
			oo.writeObject(srcObj);

			ByteArrayInputStream in = new ByteArrayInputStream(
					out.toByteArray());
			ObjectInputStream oi = new ObjectInputStream(in);
			cloneObj = oi.readObject();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return clazz.cast(cloneObj);
	}
	/**
	 * 序列化对象<br>
	 * Created by ShawnShoper 2016年5月19日
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Serializable object)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		byte[] dataBytes = null;
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(baos);
			out.writeObject(object);
			dataBytes = baos.toByteArray();
		} catch (IOException ex)
		{
			throw new SerializationException(ex);
		} finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
				if (baos != null)
					baos.close();
			} catch (IOException ex)
			{
			}
		}
		return dataBytes;
	}
	/**
	 * 反序列化 <br>
	 * Created by ShawnShoper 2016年6月2日
	 * 
	 * @param objectData
	 * @return
	 */
	public static <K> K deserialize(byte[] objectData, Class<K> k)
	{
		if (objectData == null)
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
		return deserialize(bais, k);
	}

	/**
	 * 反序列化 <br>
	 * Created by ShawnShoper 2016年6月2日
	 * 
	 * @param inputStream
	 * @return
	 */
	public static <K> K deserialize(InputStream inputStream, Class<K> k)
	{
		if (inputStream == null)
		{
			throw new IllegalArgumentException(
					"The InputStream must not be null");
		}
		ObjectInputStream in = null;
		try
		{
			// stream closed in the finally
			in = new ObjectInputStream(inputStream);
			return (K) in.readObject();

		} catch (ClassNotFoundException ex)
		{
			throw new SerializationException(ex);
		} catch (IOException ex)
		{
			throw new SerializationException(ex);
		} finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			} catch (IOException ex)
			{
				// ignore close exception
			}
		}
	}
}
