package com.kerkr.edu.utill;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.kerkr.edu.app.BaseApplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

/**
 * @类描述 文件操作类 </br>
 * @创建人 BaiB </br>
 * @创建时间  2014-03-03 12:06 </br>
 * @修改备注
 */
public class FileUtil {
    private static final int BUFF_SIZE = 1048576; // 1M Byte
    
    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    
    public static final String FILE_ROOT = SDCARD_ROOT + BaseApplication.getInstance().mAppName + "/";
    
    private static final long LOW_STORAGE_THRESHOLD = 1024 * 1024 * 10;
    
    
    

    /**
     * 检查给定的路径有多大的可用空间。
     * 
     * @param path
     *            给定的路径。
     * @return 返回给定路径的可用空间大小。
     */
    public static long getUsableSpace(File path) {
        final StatFs stats = new StatFs(path.getPath());
        return stats.getBlockCount() * stats.getAvailableBlocks();
    }

    public static void mkdir() throws IOException {
        
        File file = new File(FILE_ROOT);
        if (!file.exists() || !file.isDirectory())
            file.mkdir();
    }
    
    public static String size(long size) {
        
        if (size / (1024 * 1024) > 0) {
            float tmpSize = (float) (size) / (float) (1024 * 1024);
            DecimalFormat df = new DecimalFormat("#.##");
            return "" + df.format(tmpSize) + "MB";
        }
        else if (size / 1024 > 0) {
            return "" + (size / (1024)) + "KB";
        }
        else
            return "" + size + "B";
    }
    
    public static void installAPK(Context context, final String url) {
        
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String fileName = FILE_ROOT + FileUtil.getFileNameFromUrl(url);
        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setClassName("com.android.packageinstaller", "com.android.packageinstaller.PackageInstallerActivity");
        context.startActivity(intent);
    }
    
    public static String getFileNameFromUrl(String url) {
        // 通过 ‘？’ 和 ‘/’ 判断文件名
        int index = url.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = url.substring(url.lastIndexOf('/') + 1, index);
        }
        else {
            filename = url.substring(url.lastIndexOf('/') + 1);
        }
        
        if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
            filename = UUID.randomUUID() + ".apk";// 默认取一个文件名
        }
        return filename;
    }
    
    /**
     * @方法描述  复制单个文件 </br> 
     * @参数  oldFile 源文件 newFile 复制后的新文件</br> 
     * @创建人 qin </br>
     * @创建时间  </br>
     * */
    public static boolean copyFile(File oldFile, File newFile) {
        if (oldFile == null && newFile == null) {
            return false;
        }
        try {
            @SuppressWarnings("unused")
            int bytesum = 0;
            int byteread = 0;
            if (oldFile.exists()) {
                // 文件存在时
                InputStream inStream = new FileInputStream(oldFile); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
                fs.close();
                inStream.close();
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * 方法描述： 复制单个文件 </br> 参数： oldPath 源文件路径 newPath 复制后的新文件路径 </br> 创 建 人： </br>
     * 创建时间：</br>
     * */
    public static boolean copyFile(String oldPath, String newPath) {
        return copyFile(new File(oldPath), new File(newPath));
    }
    
    /**
     * 方法描述： 将文件夹下的所有文件复制到新的文件夹下 </br> 参数： oldFile 源文件夹 newFile 复制后的新文件夹 </br> 创
     * 建 人： </br> 创建时间：</br>
     * */
    @SuppressWarnings("resource")
    public static boolean copyFiles(File oldFile, File newFile) {
        {
            if (!oldFile.exists()) {
                return false;
            }
            byte[] b = new byte[(int) oldFile.length()];
            if (oldFile.isFile()) {
                try {
                    FileInputStream is = new FileInputStream(oldFile);
                    FileOutputStream ps = new FileOutputStream(newFile);
                    is.read(b);
                    ps.write(b);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            else if (oldFile.isDirectory()) {
                if (!oldFile.exists())
                    oldFile.mkdir();
                String[] list = oldFile.list();
                for (int i = 0; i < list.length; i++) {
                    copyFiles(oldFile.getAbsolutePath() + "/" + list[i], newFile.getAbsolutePath() + "/" + list[i]);
                }
            }
        }
        return true;
    }
    
    /**
     * 方法描述： 将文件夹下的所有文件复制到新的文件夹下 </br> 参数：oldPath 源文件夹路径 newPath 复制后的新文件夹路径</br>
     * 创 建 人： </br> 创建时间：</br>
     * */
    public static boolean copyFiles(String oldPath, String newPath) {
        return copyFiles(new File(oldPath), new File(newPath));
    }
    
    /**
     * 方法描述： 将文件夹下的所有文件删除 </br> 参数： File 源文件夹 </br> 创 建 人： </br> 创建时间：</br>
     * */
    public static boolean delFiles(File file) {
        if (file.isFile()) {
            file.delete();
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
            }
            for (File f : childFile) {
                delFiles(f);
            }
            // file.delete();
        }
        return true;
    }
    
    /**
     * 方法描述： 将文件夹下的所有文件删除 </br> 参数： File 源文件夹 </br> 创 建 人： </br> 创建时间：</br>
     * */
    public static boolean delFiles(String Path) {
        return delFiles(new File(Path));
    }
    
    /**
     * 方法描述： 获取文件夹下某格式的所有文件列表 </br> 参数： File 源文件夹 suffixName 后缀名 例如 ".zip"</br>
     * 返回值：针对该文件夹的相对路径列表</br> 创 建 人： </br> 创建时间：</br>
     * */
    public static List<String> getSimpleFileList(File file, String suffixName) {
        List<String> list = new ArrayList<String>();
        String path = "";
        if (!file.exists()) {
            return null;
        }
        // 创建fileArray名字的数组
        File[] fileArray = file.listFiles();
        // 如果传进来一个以文件作为对象的allList 返回0
        if (null == fileArray) {
            return null;
        }
        // 偏历目录下的文件
        for (int i = 0; i < fileArray.length; i++) {
            // 如果是个目录
            if (fileArray[i].isDirectory()) {
                // 递归调用
                list.addAll(getSimpleFileList(fileArray[i].getAbsoluteFile(), suffixName));
                
            }
            else if (fileArray[i].isFile()) {
                // 如果是以“”结尾的文件
                if (suffixName == null || fileArray[i].getName().endsWith(suffixName)) {
                    // 展示文件
                    path = fileArray[i].getAbsolutePath();
                    Log.e("@@@@@", path);
                    list.add(path);
                }
            }
        }
        return list;
    }
    
    /**
     * 方法描述： 获取文件夹下某格式的所有文件列表 </br> 参数： path 源文件夹路径 suffixName 后缀名 例如
     * ".zip"</br> 返回值：针对该文件夹的相对路径列表</br> 创 建 人： </br> 创建时间：</br>
     * */
    public static List<String> getSimpleFileList(String path, String suffixName) {
        return getSimpleFileList(new File(path), suffixName);
    }
    
    /**
     * 获得指定文件的byte数组
     * 
     * @param filePath
     *            文件路径
     * @return byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(URLDecoder.decode(filePath, "UTF-8"));
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
    
    /**
     * 根据byte数组，生成文件
     * 
     * @param bfile
     *            byte流
     * @param filePath
     *            文件路径
     * @param fileName
     *            文件名称
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                // 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    /**
     * @description 从assets文件夹中拷贝数据到sd卡中
     * @param context
     *            上下文环境
     * @param assetsNamee
     *            资源文件名
     * @param strOutFilePath
     *            拷贝到指定路径
     * @throws IOException
     */
    public static void copyDataToSD(Context context, String assetsNamee, String strOutFilePath) throws IOException {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFilePath + "/" + assetsNamee);
        myInput = context.getAssets().open(assetsNamee);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
    
    /**
     * @description 获取文件夹的大小
     * 
     * @param f
     *            文件夹
     * @return size 文件大小
     * @throws Exception
     */
    public static long getFileSize(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            }
            else {
                size = size + flist[i].length();
            }
        }
        return size;
    }
    
    /**
     * @description 加载本地图片
     * 
     * @param url
     *            本地图片地址
     * @return Bitmap
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @description 文件夹内是否存在文件。是返回true
     * 
     * @param file
     *            文件夹
     * @return true/false
     */
    public static boolean havefile(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    havefile(files[i]);
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * @description 获取文件内容
     * @param strFilePath
     *            文件地址
     * @return content 文件内容字符串
     * @throws IOException
     */
    public static String ReadTxtFile(String strFilePath) throws IOException {
        String path = strFilePath;
        String content = ""; // 文件内容字符串
        // 打开文件
        File file = new File(path);
        // 如果path是传递过来的参数，可以做一个非目录的判断
        if (!file.isDirectory()) {
            InputStream instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                // 分行读取
                while ((line = buffreader.readLine()) != null) {
                    content += line;
                }
                instream.close();
            }
        }
        return content;
    }
    
    /**
     * @description 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下
     * @param zipName
     *            待解压缩的ZIP文件名 /mnt/sdcard/ce.zip
     * @param targetBaseDirName
     *            目标目录 /mnt/sdcard/cache/
     */
    public static void upzipFile(String zipFileName, String targetBaseDirName) throws IOException {
        if (!targetBaseDirName.endsWith(File.separator)) {
            targetBaseDirName += File.separator;
        }
        
        // 根据ZIP文件创建ZipFile对象
        @SuppressWarnings("resource")
        ZipFile myZipFile = new ZipFile(zipFileName);
        ZipEntry entry = null;
        String entryName = null;
        String targetFileName = null;
        byte[] buffer = new byte[4096];
        int bytes_read;
        // 获取ZIP文件里所有的entry
        Enumeration<?> entrys = myZipFile.entries();
        // 遍历所有entry
        while (entrys.hasMoreElements()) {
            entry = (ZipEntry) entrys.nextElement();
            // 获得entry的名字
            entryName = entry.getName();
            targetFileName = targetBaseDirName + entryName;
            if (entry.isDirectory()) {
                // 如果entry是一个目录，则创建目录
                new File(targetFileName).mkdirs();
                continue;
            }
            else {
                // 如果entry是一个文件，则创建父目录
                new File(targetFileName).getParentFile().mkdirs();
            }
            // 否则创建文件
            File targetFile = new File(targetFileName);
            // System.out.println("创建文件：" + targetFile.getAbsolutePath());
            // 打开文件输出流
            FileOutputStream os = new FileOutputStream(targetFile);
            // 从ZipFile对象中打开entry的输入流
            InputStream is = myZipFile.getInputStream(entry);
            while ((bytes_read = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytes_read);
            }
            // 关闭流
            os.close();
            is.close();
        }
    }
    
    /**
     * @description 压缩文件
     * @param resFile
     *            需要压缩的文件（夹） F://cc/ or F://abc.txt
     * @param zipout
     *            压缩的目的文件
     * @param rootpath
     *            压缩的文件路径
     * @throws FileNotFoundException
     *             找不到文件时抛出
     * @throws IOException
     *             当压缩过程出错时抛出
     */
    public static void zipFile(File resFile, ZipOutputStream zipout, String rootpath) throws FileNotFoundException, IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator) + resFile.getName();
        rootpath = new String(rootpath.getBytes("8859_1"), "UTF-8");
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipout, rootpath);
            }
        }
        else {
            byte buffer[] = new byte[BUFF_SIZE];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile), BUFF_SIZE);
            zipout.putNextEntry(new ZipEntry(rootpath));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, realLength);
            }
            in.close();
            zipout.flush();
            zipout.closeEntry();
        }
    }
    
    /**
     * @description 
     *              将存放在sourceFilePath目录下的源文件,打包成fileName名称的ZIP文件,并存放到zipFilePath
     * 
     * @param sourceFilePath
     *            待压缩的文件路径
     * @param zipFilePath
     *            压缩后存放路径
     * @param fileName
     *            压缩后文件的名称
     * @return flag 压缩是否成功
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) throws IOException {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        if (sourceFile.exists() == false) {
        }
        else {
            File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
            File[] sourceFiles = sourceFile.listFiles();
            if (null == sourceFiles || sourceFiles.length < 1) {
            }
            else {
                fos = new FileOutputStream(zipFile);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                byte[] bufs = new byte[1024 * 10];
                for (int i = 0; i < sourceFiles.length; i++) {
                    // 创建ZIP实体,并添加进压缩包
                    // if(sourceFiles[i].getName().contains(".p12")||sourceFiles[i].getName().contains(".truststore")){
                    ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                    zos.putNextEntry(zipEntry);
                    // 读取待压缩的文件并写进压缩包里
                    fis = new FileInputStream(sourceFiles[i]);
                    bis = new BufferedInputStream(fis, 1024 * 10);
                    int read = 0;
                    while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                        zos.write(bufs, 0, read);
                    }
                    bis.close();
                    fis.close();
                    // }
                }
                flag = true;
            }
            zos.close();
            fos.close();
        }
        return flag;
    }
    
    /*
     * 
     * 
     */
    public static byte[] getFileToByteArray(File file) {
        ByteArrayOutputStream bos = null;
        FileInputStream fis = null;
        byte[] buffer = null;
        try {
            bos = new ByteArrayOutputStream();
            fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            buffer = bos.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bos != null)
                    bos.close();
            }
            catch (IOException e) {
                bos = null;
            }
            try {
                if (fis != null)
                    fis.close();
            }
            catch (IOException e) {
                fis = null;
            }
        }
        return buffer;
    }
    

    public final static String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * read file
     * 
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     * 
     * @param filePath
     * @param content
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     * 
     * @param filePath
     * @param contentList
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (!CollectionUtils.isValid(contentList)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file, the string will be written to the begin of the file
     * 
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * write file, the string list will be written to the begin of the file
     * 
     * @param filePath
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     * 
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     * 
     * @param file the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     * 
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     * 
     * @param file the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * move file
     * 
     * @param sourceFilePath
     * @param destFilePath
     */
    public static void moveFile(String sourceFilePath, String destFilePath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(sourceFilePath), new File(destFilePath));
    }

    /**
     * move file
     * 
     * @param srcFile
     * @param destFile
     */
    public static void moveFile(File srcFile, File destFile) {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }

 

    /**
     * read file to string list, a element of list is a line
     * 
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * get file name from path, not include suffix
     * 
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     * 
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     * 
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     * 
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name from path
     * 
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * get suffix of file from path
     * 
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     * 
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     *         the directories can not be created.
     *         <ul>
     *         <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
     *         <li>if target directory already exists, return true</li>
     *         <li>return {@link java.io.File#makeFolder}</li>
     *         </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     * 
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     * 
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StringUtils.isBlank(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     * 
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     * 
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (StringUtils.isBlank(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }
}