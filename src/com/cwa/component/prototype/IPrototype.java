package com.cwa.component.prototype;

public interface IPrototype extends ISpreadPrototype,IWritable{
	public int getKeyId();
	public byte[] toBytes();
}
