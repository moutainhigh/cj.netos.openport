package cj.studio.openport;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import cj.studio.ecm.EcmException;
import cj.ultimate.gson2.com.google.gson.Gson;
import cj.ultimate.gson2.com.google.gson.GsonBuilder;

/**
 * 许可方法的返回值要么为空，要么必须为该类型
 * 
 * @author caroceanjofers
 *
 * @param <T>
 */
public final class ResponseClient<T> {
	int status;
	String message;
	String dataText;
	String dataType;
	String[] dataElementTypes;
	long begintime;
	long endtime;
	public ResponseClient() {
		begintime=System.currentTimeMillis();
	}
	public ResponseClient(int status, String message,  String dataText) {
		this(status,message,String.class.getName(),null,dataText);
	}
	public ResponseClient(int status, String message, String dataType, String[] dataElementTyps, String dataText) {
		if (dataType == null) {
			throw new RuntimeException("dataType 为空");
		}
		if (status <= 0) {
			status = 200;
		}
		this.status = status;
		this.message = message;
		this.dataText = dataText;
		this.dataType = dataType;
		this.dataElementTypes = dataElementTyps;
	}

	public long getBegintime() {
		return begintime;
	}

	public void setBegintime(long begintime) {
		this.begintime = begintime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public String getDataText() {
		return dataText;
	}
	@SuppressWarnings("unchecked")
	public T getData(ClassLoader cl) {
		if(dataType==null||void.class.getName().equals(dataType)||dataType.equals(Void.class.getName())) {
			return null;
		}
		Class<?> dataTypeClass;
		try {
			dataTypeClass = Class.forName(dataType,true,cl);
		} catch (ClassNotFoundException e) {
			throw new EcmException(e);
		}
		if(dataTypeClass.equals(Void.class)) {
			return null;
		}
		if (dataElementTypes != null) {
			Class<?>[] arr = new Class<?>[dataElementTypes.length];
			for (int i = 0; i < dataElementTypes.length; i++) {
				String c = dataElementTypes[i];
				try {
					arr[i] = Class.forName(c,true,cl);
				} catch (ClassNotFoundException e) {
					throw new EcmException(e);
				}
			}
			MyParameterizedType pt = new MyParameterizedType(dataTypeClass, arr);
			return (T) new Gson().fromJson(dataText, pt);
		}
		Object obj=  new Gson().fromJson(dataText, dataTypeClass);
		return (T)obj;
	}

	public void setData(T data) {
		dataType = data.getClass().getName();
		this.dataText = new Gson().toJson(data);
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public static <T> ResponseClient<T> createFromJson(String json) {
		@SuppressWarnings("unchecked")
		ResponseClient<T> obj = new Gson().fromJson(json, ResponseClient.class);
		return obj;
	}

	class MyParameterizedType implements ParameterizedType {
		Class<?> dataType;
		Class<?>[] dataElementTypes;

		public MyParameterizedType(Class<?> dataType, Class<?>[] dataElementTypes) {
			this.dataType = dataType;
			this.dataElementTypes = dataElementTypes;
			if (Map.class.isAssignableFrom(dataType)) {
				if (dataElementTypes != null && dataElementTypes[0] != Void.class && dataElementTypes.length != 2) {
					throw new RuntimeException("缺少Map及其派生类的元素Key和value的类型声明");
				}
			}
			if (Collection.class.isAssignableFrom(dataType)) {
				if (dataElementTypes != null && dataElementTypes[0] != Void.class && dataElementTypes.length != 1) {
					throw new RuntimeException("缺少Collection及其派生类的元素类型声明");
				}
			}
		}

		@Override
		public Type[] getActualTypeArguments() {
			return dataElementTypes;
		}

		@Override
		public Type getRawType() {
			return dataType;
		}

		@Override
		public Type getOwnerType() {
			return dataType.getDeclaringClass();
		}

	}
}
