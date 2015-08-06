package com.cwa.component.prototype;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 可读写接口
 * 
 * @author Administrator
 * 
 */
public interface IWritable {
	void write(OutputStream out) throws IOException;

	void readFields(InputStream in) throws IOException;
}
