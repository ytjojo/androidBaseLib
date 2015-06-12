package com.kerkr.edu.reflect;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

public class BeanUtil {
	
    
    /**
     * compare two object
     * 
     * @param actual
     * @param expected
     * @return <ul>
     *         <li>if both are null, return true</li>
     *         <li>return actual.{@link Object#equals(Object)}</li>
     *         </ul>
     */
    public static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }
	/**
	 * 对象copy只拷贝public的属性
	 * @param from
	 * @param to
	 */
	public static void copyBeanWithOutNull(Object from,Object to){
		Class<?> beanClass = from.getClass();
		Method[] methodList = beanClass.getDeclaredMethods();
		Field[] fields=	beanClass.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field=fields[i];
			field.setAccessible(true);
			try {
				Object value=field.get(from);
				if(value!=null){
					field.set(to, value);
				}
			} catch (Exception e) {
			} 
		}
//		for (int i = 0; i < methodList.length; i++) {
//			Method method=methodList[i];
//			if(method.toString().startsWith("public")){
//				if(method.getName().startsWith("get")||method.getName().startsWith("is")){
//					String name=method.getName().substring(3);
//					if(method.getName().startsWith("is")){
//						name=method.getName().substring(2);
//					}
//					try {
//						Object value=method.invoke(from);
//						if(value!=null){
//							String methodName="set"+name;							
//							Method setMethod = beanClass.getDeclaredMethod(methodName,method.getReturnType());
//							setMethod.invoke(to, value);
//						}												
//					} catch (Exception e) {
//						e.printStackTrace();
//					} 
//				}
//			}
//		}
		
		
		
		
		
	}
	
	public static Field getDeclaredField(Class clazz,String name){
		try {
			return	clazz.getDeclaredField(name);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取属性
	 * @param o
	 * @param field
	 * @return
	 */
	public  static Object getProperty(Object o,String field){
		try {
			Field f =o.getClass().getDeclaredField(field);
			return	f.get(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 添加屬性
	 * @param o
	 * @param field
	 * @param value
	 */
	public static void setProperty(Object o,String field,Object value){
		try {
			Field	f = o.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(o, value);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
	}
	

    /**
     * 深度复制内存对象，处理在复制对象特别是集合类时的浅复制
     * 
     * @param srcObj
     *            复制的目标
     * @return cloneObj 复制后的对象（完全独立的个体）
     * */
    public static Object depthClone(Object srcObj) {
        Object cloneObj = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(out);
            oo.writeObject(srcObj);

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(in);
            cloneObj = oi.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cloneObj;
    }


}
