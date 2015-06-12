package com.kerkr.edu.log;

import android.util.Log;

import java.util.Hashtable;

import com.kerkr.edu.app.BaseApplication;
import com.kerkr.edu.app.Constans;

/**
 * The class for print log
 *
 * @author kesenhoo
 */
public class LogBase
{
    
    public final static String customTagPrefix = BaseApplication.getInstance().mAppName; //自己的AP名称
    
    private final static int logLevel = Log.VERBOSE;
    
    
    private static Hashtable<String, LogBase> sLoggerTable = new Hashtable<String, LogBase>();
    
 
    
    private String mLabel;
    
    LogBase(String name)
    {
        mLabel = name;
    }
    
    /**
     * @param className
     * @return
     */
    @SuppressWarnings("unused")
    private static LogBase getLogger(String className)
    {
        LogBase classLogger = (LogBase) sLoggerTable.get(className);
        if (classLogger == null)
        {
            classLogger = new LogBase(className);
            sLoggerTable.put(className, classLogger);
        }
        return classLogger;
    }
   
    private String generateTag(StackTraceElement stack)
    {
        
        String tag = "%s.%s(L:%d)";
        String className = stack.getClassName();
        String methodName = stack.getMethodName();
    
        className = className.substring(className.lastIndexOf(".") + 1);
        tag = String.format(tag, stack.getClassName(), methodName, stack.getLineNumber());
//        tag = customTagPrefix == null ? tag : customTagPrefix + ":" + tag;
        StringBuilder sb  = new StringBuilder(mLabel); 
        sb.append("[");
        sb.append(tag);
        sb.append("]   ");
        return sb.toString();
    }
    
    /**
     * Get The Current Function Name
     *
     * @return
     */
    private String getFunctionName( StackTraceElement[] sts)
    {
        if (sts == null)
        {
            return null;
        }
        for (StackTraceElement st : sts)
        {
            if (st.isNativeMethod())
            {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName()))
            {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName()))
            {
                continue;
            }
            return mLabel + "[ " + Thread.currentThread().getName() + ": " + st.getFileName() + ":" + st.getLineNumber() + " " + st.getMethodName() + " ]";
        }
        return null;
    }
    
    /**
     * The Log Level:i
     *
     * @param str
     */
    public void i(Object str)
    {
        if (!Constans.DEBUG_MODE)
        {
            return;
        }
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);
        Log.i(customTagPrefix, tag + " --- :  " + str.toString());
        
    }
    
    /**
     * The Log Level:d
     *
     * @param str
     */
    public void d(Object str)
    {
        if (!Constans.DEBUG_MODE)
        {
            return;
        }
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);
        Log.d(customTagPrefix, tag + " --- :  " + str.toString());
    }
    
    /**
     * The Log Level:V
     *
     * @param str
     */
    public void v(Object str)
    {
        if (!Constans.DEBUG_MODE)
        {
            return;
        }
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);
        Log.v(customTagPrefix, tag + " --- :  " + str.toString());
    }
    
    /**
     * The Log Level:w
     *
     * @param str
     */
    public void w(Object str)
    {
        if (!Constans.DEBUG_MODE)
        {
            return;
        }
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);
        Log.w(customTagPrefix, tag + " --- :  " + str.toString());
    }
    
    /**
     * The Log Level:e
     *
     * @param str
     */
    public void e(Object str)
    {
        if (!Constans.DEBUG_MODE)
        {
            return;
        }
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);
        Log.e(customTagPrefix, tag + " --- :  " + str.toString());
    }
    
    /**
     * The Log Level:e
     *
     * @param ex
     */
    public void e(Exception ex)
    {
        if (!Constans.DEBUG_MODE)
        {
            return;
        }
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);
        Log.e(customTagPrefix, tag , ex);
    }
    
    /**
     * The Log Level:e
     *
     * @param log
     * @param tr
     */
    public void e(String log, Throwable tr)
    {
        
        if (!Constans.DEBUG_MODE) {
            return ;
        }
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = generateTag(caller);
        Log.e(customTagPrefix,tag + " --- :  "+ log,tr);
    }
}