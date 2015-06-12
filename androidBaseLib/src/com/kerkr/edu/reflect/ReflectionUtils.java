package com.kerkr.edu.reflect;
 
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kerkr.edu.log.VALog;
import com.kerkr.edu.utill.Assert;
import com.kerkr.edu.utill.StringUtils;
 
 
/**
 * 反射的Utils函数集合. 提供访问私有变量,获取泛型类型Class,提取集合中元素的属性等Utils函数.
 * 
 * @author lei
 */
public class ReflectionUtils {
 
 
    private ReflectionUtils() {
    }
 
    /**
     * <是否是继承关系>
     * <功能详细描述>
     * @param type
     * @param superClass
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    public static boolean isSubclassOf(Class<?> type, Class<?> superClass) {
        if (type.getSuperclass() != null) {
            if (type.getSuperclass().equals(superClass)) {
                return true;
            }

            return isSubclassOf(type.getSuperclass(), superClass);
        }

        return false;
    }
    
    /** 
     * 通过构造函数实例化对象  
     * @param className       类的全路径名称    
     * @param parameterTypes  参数类型 
     * @param initargs        参数值 
     * @return 
     */  
    @SuppressWarnings("rawtypes")  
    public static Object constructorNewInstance(String className,Class [] parameterTypes,Object[] initargs) {   
        try {  
            Constructor<?> constructor = (Constructor<?>) Class  
                    .forName(className).getDeclaredConstructor(parameterTypes);                     //暴力反射  
            constructor.setAccessible(true);  
            return constructor.newInstance(initargs);  
        } catch (Exception ex) {  
            throw new RuntimeException();  
        }  
  
    }  
    /** 
     * 循环向上转型, 获取对象的 DeclaredMethod 
     * @param object : 子类对象 
     * @param methodName : 父类中的方法名 
     * @param parameterTypes : 父类中的方法参数类型 
     * @return 父类中的方法对象 
     */  
      
    public static Method getDeclaredMethod(Object object, String methodName, Class<?> ... parameterTypes){  
        Method method = null ;  
          
        for(Class<?> clazz = object.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {  
                method = clazz.getDeclaredMethod(methodName, parameterTypes) ;  
                return method ;  
            } catch (Exception e) {  
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。  
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了  
              
            }  
        }  
          
        return null;  
    }  
      
    /** 
     * 直接调用对象方法, 而忽略修饰符(private, protected, default) 
     * @param object : 子类对象 
     * @param methodName : 父类中的方法名 
     * @param parameterTypes : 父类中的方法参数类型 
     * @param parameters : 父类中的方法参数 
     * @return 父类中方法的执行结果 
     */  
      
    public static Object invokeMethod(Object object, String methodName, Class<?> [] parameterTypes,  
            Object [] parameters) {  
        //根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象  
        Method method = getDeclaredMethod(object, methodName, parameterTypes) ;  
          
        //抑制Java对方法进行检查,主要是针对私有方法而言  
        method.setAccessible(true) ;  
          
            try {  
                if(null != method) {  
                      
                    //调用object 的 method 所代表的方法，其方法的参数是 parameters  
                    return method.invoke(object, parameters) ;  
                }  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            } catch (InvocationTargetException e) {  
                e.printStackTrace();  
            }  
          
        return null;  
    }  
    
    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        Field field = getDeclaredField(object, fieldName);
 
        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
 
        makeAccessible(field);
 
        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            VALog.e("不可能抛出的异常{}" +e.getMessage());
        }
        return result;
    }
 
    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);
 
        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
 
        makeAccessible(field);
 
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            VALog.e("不可能抛出的异常:{}" + e.getMessage());
        }
    }
 
    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    protected static Field getDeclaredField(final Object object, final String fieldName) {
        Assert.notNull(object, "object不能为空");
        return getDeclaredField(object.getClass(), fieldName);
    }
 
    /**
     * 循环向上转型,获取类的DeclaredField.
     */
    @SuppressWarnings("unchecked")
    protected static Field getDeclaredField(final Class clazz, final String fieldName) {
        Assert.notNull(clazz, "clazz不能为空");
        Assert.hasText(fieldName, "fieldName");
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }
 
    /**
     * 强制转换fileld可访问.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }
 
    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如public UserDao extends HibernateDao<User>
     * 
     * @param clazz
     *            The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     *         determined
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }
 
    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如public UserDao extends
     * HibernateDao<User,Long>
     * 
     * @param clazz
     *            clazz The class to introspect
     * @param index
     *            the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     *         determined
     */
 
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {
 
        Type genType = clazz.getGenericSuperclass();
 
        if (!(genType instanceof ParameterizedType)) {
            VALog.w(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }
 
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
 
        if (index >= params.length || index < 0) {
            VALog.w("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            VALog.w(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }
 
    /**
     * 提取集合中的对象的属性,组合成List.
     * 
     * @param collection
     *            来源集合.
     * @param propertityName
     *            要提取的属性名.
     */
    @SuppressWarnings("unchecked")
    public static List<Object>  fetchElementPropertyToList(final Collection collection, final String propertyName) throws Exception {
 
        List<Object> list = new ArrayList<Object>();
 
        for (Object obj : collection) {
            list.add(BeanUtil.getProperty(obj, propertyName));
        }
 
        return list;
    }
 
    /**
     * 提取集合中的对象的属性,组合成由分割符分隔的字符串.
     * 
     * @param collection
     *            来源集合.
     * @param propertityName
     *            要提取的属性名.
     * @param separator
     *            分隔符.
     */
    @SuppressWarnings("unchecked")
    public static String fetchElementPropertyToString(final Collection collection, final String propertyName, final String separator) throws Exception {
        List<Object> list = fetchElementPropertyToList(collection, propertyName);
        return StringUtils.joinObjects(list.toArray(), separator);
    }
}