package org.shoper.commons;

import java.io.Serializable;

public class SerializableObject<T> implements Serializable
{

	private static final long serialVersionUID = -7735253286844892242L;
	private T object;
	public T getObject()
	{
		return object;
	}
	public void setObject(T t)
	{
		this.object = t;
	}

}
