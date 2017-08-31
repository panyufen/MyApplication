package com.cus.pan.library.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by _SOLID
 * Date:2016/4/20
 * Time:15:01
 */
public class FileUtils {

    private static String TAG = "FileUtils";
    private static String FILE_WRITING_ENCODING = "UTF-8";
    private static String FILE_READING_ENCODING = "UTF-8";

    public static String readFile(String _sFileName, String _sEncoding) throws Exception {
        StringBuffer buffContent = null;
        String sLine;

        FileInputStream fis = null;
        BufferedReader buffReader = null;
        if (_sEncoding == null || "".equals(_sEncoding)) {
            _sEncoding = FILE_READING_ENCODING;
        }

        try {
            fis = new FileInputStream(_sFileName);
            buffReader = new BufferedReader(new InputStreamReader(fis,
                    _sEncoding));
            boolean zFirstLine = "UTF-8".equalsIgnoreCase(_sEncoding);
            while ((sLine = buffReader.readLine()) != null) {
                if (buffContent == null) {
                    buffContent = new StringBuffer();
                } else {
                    buffContent.append("\n");
                }
                if (zFirstLine) {
                    sLine = removeBomHeaderIfExists(sLine);
                    zFirstLine = false;
                }
                buffContent.append(sLine);
            }// end while
            return (buffContent == null ? "" : buffContent.toString());
        } catch (FileNotFoundException ex) {
            throw new Exception("要读取的文件没有找到!", ex);
        } catch (IOException ex) {
            throw new Exception("读取文件时错误!", ex);
        } finally {
            // 增加异常时资源的释放
            try {
                if (buffReader != null)
                    buffReader.close();
                if (fis != null)
                    fis.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static File writeFile(String path, String content, String encoding, boolean isOverride) throws Exception {
        if (TextUtils.isEmpty(encoding)) {
            encoding = FILE_WRITING_ENCODING;
        }
        InputStream is = new ByteArrayInputStream(content.getBytes(encoding));
        return writeFile(is, path, isOverride);
    }

    public static File writeFile(InputStream is, String path, boolean isOverride) throws Exception {
        String sPath = extractFilePath(path);
        if (!pathExists(sPath)) {
            makeDir(sPath, true);
        }

        if (!isOverride && fileExists(path)) {
            if (path.contains(".")) {
                String suffix = path.substring(path.lastIndexOf("."));
                String pre = path.substring(0, path.lastIndexOf("."));
                path = pre + "_" + System.currentTimeMillis() + suffix;
            } else {
                path = path + "_" + System.currentTimeMillis();
            }
        }

        FileOutputStream os = null;
        File file = null;

        try {
            file = new File(path);
            os = new FileOutputStream(file);
            int byteCount = 0;
            byte[] bytes = new byte[1024];

            while ((byteCount = is.read(bytes)) != -1) {
                os.write(bytes, 0, byteCount);
            }
            os.flush();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("写文件错误", e);
        } finally {
            try {
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 移除字符串中的BOM前缀
     *
     * @param _sLine 需要处理的字符串
     * @return 移除BOM后的字符串.
     */
    private static String removeBomHeaderIfExists(String _sLine) {
        if (_sLine == null) {
            return null;
        }
        String line = _sLine;
        if (line.length() > 0) {
            char ch = line.charAt(0);
            // 使用while是因为用一些工具看到过某些文件前几个字节都是0xfffe.
            // 0xfeff,0xfffe是字节序的不同处理.JVM中,一般是0xfeff
            while ((ch == 0xfeff || ch == 0xfffe)) {
                line = line.substring(1);
                if (line.length() == 0) {
                    break;
                }
                ch = line.charAt(0);
            }
        }
        return line;
    }

    /**
     * 从文件的完整路径名（路径+文件名）中提取 路径（包括：Drive+Directroy )
     *
     * @param _sFilePathName
     * @return
     */
    public static String extractFilePath(String _sFilePathName) {
        int nPos = _sFilePathName.lastIndexOf('/');
        if (nPos < 0) {
            nPos = _sFilePathName.lastIndexOf('\\');
        }

        return (nPos >= 0 ? _sFilePathName.substring(0, nPos + 1) : "");
    }

    /**
     * 检查指定文件的路径是否存在
     *
     * @param _sPathFileName 文件名称(含路径）
     * @return 若存在，则返回true；否则，返回false
     */
    public static boolean pathExists(String _sPathFileName) {
        String sPath = extractFilePath(_sPathFileName);
        return fileExists(sPath);
    }

    public static boolean fileExists(String _sPathFileName) {
        File file = new File(_sPathFileName);
        return file.exists();
    }

    /**
     * 创建目录
     *
     * @param _sDir             目录名称
     * @param _bCreateParentDir 如果父目录不存在，是否创建父目录
     * @return
     */
    public static boolean makeDir(String _sDir, boolean _bCreateParentDir) {
        boolean zResult = false;
        File file = new File(_sDir);
        if (_bCreateParentDir)
            zResult = file.mkdirs(); // 如果父目录不存在，则创建所有必需的父目录
        else
            zResult = file.mkdir(); // 如果父目录不存在，不做处理
        if (!zResult)
            zResult = file.exists();
        return zResult;
    }


    public static void moveAssetsToDir(Context context, String rawName, String dir,boolean isOverRide) {
        try {
            writeFile(context.getAssets().open(rawName), dir, isOverRide);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 得到手机的缓存目录
     *
     * @param context
     * @return
     */
    public static File getCacheDir(Context context) {
        Log.i("getCacheDir", "cache sdcard state: " + Environment.getExternalStorageState());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                Log.i("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());
                return cacheDir;
            }
        }

        File cacheDir = context.getCacheDir();
        Log.i("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());

        return cacheDir;
    }

    /**
     * 得到皮肤目录
     *
     * @param context
     * @return
     */
    public static File getSkinDir(Context context) {
        File skinDir = new File(getCacheDir(context), "skin");
        if (skinDir.exists()) {
            skinDir.mkdirs();
        }
        return skinDir;
    }

    public static String getSkinDirPath(Context context) {
        return getSkinDir(context).getAbsolutePath();
    }

    public static String getSaveImagePath(Context context) {
        String path = getCacheDir(context).getAbsolutePath();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        path = path + File.separator + "Pictures";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    public static String generateFileNameByTime() {
        return System.currentTimeMillis() + "";
    }

    public static String getFileName(String path) {
        int index = path.lastIndexOf('/');
        return path.substring(index + 1);
    }


    /**
     * 从assets目录下拷贝整个文件夹，不管是文件夹还是文件都能拷贝
     *
     * @param context           上下文
     * @param rootDirFullPath   文件目录，要拷贝的目录如assets目录下有一个SBClock文件夹：SBClock
     * @param targetDirFullPath 目标文件夹位置如：/sdcrad/SBClock
     */
    public static boolean copyFolderFromAssets(Context context, boolean isCover, String rootDirFullPath, String targetDirFullPath) {
        try {
            String[] listFiles = context.getAssets().list(rootDirFullPath);// 遍历该目录下的文件和文件夹

            File targetDir = new File(targetDirFullPath);
            if (!targetDir.exists() && !new File(targetDirFullPath).mkdirs()) {
                LogUtils.d("mkdir error " + targetDirFullPath);
                return false;
            }
            if (listFiles.length == 0) {
                LogUtils.d("no file in " + targetDirFullPath);
                return false;
            }

            boolean checkCopy = true;

            for (String string : listFiles) {
                String[] tempChildFiles = context.getAssets().list(rootDirFullPath + "/" + string);
                if (tempChildFiles.length > 0) {//肯定是目录
                    String childRootDirFullPath = rootDirFullPath + "/" + string;
                    String childTargetDirFullPath = targetDirFullPath + "/" + string;
                    checkCopy = copyFolderFromAssets(context, isCover, childRootDirFullPath, childTargetDirFullPath);
                } else {//有可能是空文件夹 或者文件
                    checkCopy = copyFileFromAssets(context, isCover, rootDirFullPath + "/" + string, targetDirFullPath + "/" + string);
                }
            }
            return checkCopy;
        } catch (IOException e) {
            LogUtils.d("copyFolderFromAssets " + "IOException-" + e.getMessage());
            LogUtils.d("copyFolderFromAssets " + "IOException-" + e.getLocalizedMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从assets目录下拷贝文件
     *
     * @param context            上下文
     * @param assetsFilePath     文件的路径名如：SBClock/1.png
     * @param targetFileFullPath 目标文件路径如：/sdcard/SBClock/1.png
     */
    public static boolean copyFileFromAssets(Context context, String assetsFilePath, String targetFileFullPath) {
        return copyFileFromAssets(context, false, assetsFilePath, targetFileFullPath);
    }

    public static boolean copyFileFromAssets(Context context, boolean isCover, String assetsFilePath, String targetFileFullPath) {
        InputStream assestsFileImputStream;
        try {
            assestsFileImputStream = context.getAssets().open(assetsFilePath);
            return copyAssetsFile(isCover, assestsFileImputStream, targetFileFullPath);
        } catch (IOException e) {
            LogUtils.d("copyFileFromAssets " + "IOException-" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    private static boolean copyAssetsFile(boolean isCover, InputStream inputStream, String dest) {
        File file = new File(dest);
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists() && parentFile.mkdirs()) {
            LogUtils.d("mkdir error " + file.getParent());
            return false;
        }
        if (!file.exists() || isCover) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file.getAbsoluteFile());
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = inputStream.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } else {
//            LogUtils.i(" file is exists don't need to copy");
            return true;
        }
    }


    /**
     * 提升读写权限
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static void setPermission(String filePath)  {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取SDCard可用空间大小
     *
     * @return 单位 B
     */
    public static long getSDCardAvailableSpace() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(sdcardDir.getPath());
            long blockSize;
            long totalBlocks;
            long availableBlocks;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
                totalBlocks = stat.getBlockCountLong();
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                blockSize = stat.getBlockSize();
                totalBlocks = stat.getBlockCount();
                availableBlocks = stat.getAvailableBlocks();
            }

//            Log.d(TAG, "block大小:" + blockSize + ",block数目:" + totalBlocks + ",总大小:" + blockSize * totalBlocks / 1024 + "KB");
//            Log.d(TAG, "可用的block数目：:" + availableBlocks + ",剩余空间:" + availableBlocks * blockSize / 1024 + "KB");
            return availableBlocks * blockSize;
        }
        return 0L;
    }


}
