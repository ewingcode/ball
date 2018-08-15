package com.ewing.order.core.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

/**
 * 序列化工具类
 * 
 * @author tanson lam
 * @create 2016年7月16日
 */
public class SerializingTranscoder {
 
	protected final static Logger logger =  CacheLogger.logger;
	/**
	 * 反序化
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static Object deserialize(byte[] data) throws Exception {
		try {
			return new ObjectInputStream(new ByteArrayInputStream(data)) {

				@Override
				protected Class<?> resolveClass(ObjectStreamClass desc)
						throws IOException, ClassNotFoundException {
					return Class.forName(desc.getName());
				}
			}.readObject();
		} catch (Exception e) {
			logger.error("Could not deserialize", e);
			throw e;
		}
	}

	/**
	 * 序列化
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static byte[] serialize(Object object) throws IOException {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			new ObjectOutputStream(bos).writeObject(object);
			return bos.toByteArray();
		} catch (IOException e) {
			logger.error("Could not serialize", e);
			throw e;
		}
	}

	/**
	 * 序列化: list列表
	 * 
	 * @throws Exception
	 * 
	 */
	protected static <T> byte[] serializeList(List<T> tList) throws Exception {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream(byteOut);
		try {
			// 列表长度
			stream.writeInt(tList.size());

			for (T t : tList) {
				byte[] b = serialize(t);
				stream.writeInt(b.length);
				stream.write(serialize(t));
			}

			byte[] buffer = byteOut.toByteArray();
			return buffer;
		} catch (Exception e) {
			logger.error("Could not serialize", e);
			throw e;
		} finally {
			try {
				stream.close();
				byteOut.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
	}

	/**
	 * 反序列化: list列表
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> deSerializeList(byte[] buff) throws Exception {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(buff);
		DataInputStream stream = new DataInputStream(byteIn);
		List<T> tList = new ArrayList<T>();
		try {
			// 读取列表长度
			int len = stream.readInt();
			for (int i = 0; i < len; i++) {
				int blen = stream.read();
				byte[] b = new byte[blen];
				stream.read(b);
				tList.add((T) deserialize(b));
			}

			return tList;
		} catch (Exception e) {
			logger.error("Could not deSerializeList", e);
			throw e;
		} finally {
			try {
				stream.close();
				byteIn.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
	}
}
