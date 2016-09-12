package com.moxian.librarys.mxutils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * 文件工具类
 * 
 * @author LQ
 * @date 2014年12月30日 15:54
 */
public class MxFileUtils {
	public static final String DEFAULT_CHARSET_NAME = "UTF-8";

	/**
	 * 读取文件的所有字符串
	 * 
	 * @param path
	 * @return
	 */
	public static String read(String path) {
		FileInputStream is = null;
		if (!TextUtils.isEmpty(path) && isExist(path)) {
			try {
				is = new FileInputStream(path);
				byte[] data = new byte[is.available()];
				if (is.read(data) > 0) {
					return new String(data, DEFAULT_CHARSET_NAME);
				}
			} catch (Exception e) {
				MxUtils.w(e.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return null;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isExist(File file) {
		return file.exists();
	}

	/**
	 * 从url中获取图片的名字
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileName(String url) {
		int index = url.lastIndexOf("/");
		String name = null;
		if (index == -1)
			return name;
		name = url.substring(index + 1);
		return name;
	}

	/**
	 * 从url中获取后缀名
	 *
	 * @param url
	 * @return
	 */
	public static String getExtensionName(String url) {
		// 获取文件的后缀名
		int index = url.lastIndexOf('.');
		String fileEndName = null;
		if (index > 1) {
			fileEndName = url.substring(url.lastIndexOf('.') + 1);
		}
		return fileEndName;
	}

	/**
	 * 创建文件夹
	 * 
	 * @return
	 */
	public static boolean createDir(String path) {
		File file = new File(path);
		boolean r = false;
		if (!file.exists()) {
			r = file.mkdirs();
		}
		return r;
	}

	/**
	 * 创建文件
	 * 
	 * @param fileFullPath
	 * @return
	 */
	public static boolean createFile(String fileFullPath) {
		File file = new File(fileFullPath);
		boolean r = false;
		if (!file.exists()) {
			try {
				if (file.getParentFile() != null
						&& !file.getParentFile().exists()) {
					r = file.getParentFile().mkdirs();
				}
				r = file.createNewFile();
			} catch (IOException e) {
			}
		}
		return r;
	}

	/**
	 * 创建文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean createFile(File file) {
		boolean r = false;
		if (!file.exists()) {
			try {
				if (file.getParentFile() != null
						&& !file.getParentFile().exists()) {
					r = file.getParentFile().mkdirs();
				}
				r = file.createNewFile();
			} catch (IOException e) {
			}
		}
		return r;
	}

	/**
	 * 删除文件，可以是单个文件或文件夹
	 * 
	 * @param fileName
	 *            待删除的文件名
	 * @return 文件删除成功返回true,否则返回false
	 */

	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			MxUtils.d("--------------delete fileName=" + fileName);
			return false;
		} else {
			if (file.isFile()) {
				return deleteFile(fileName);

			} else {
				return deleteDirectory(fileName);

			}

		}

	}

	/**
	 * 把url转换成文件全路径
	 * 
	 * @param url
	 * @param path
	 * @return
	 */
	public static String urlToFile(String url, String path) {
		if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(path)) {
			// return path+File.separator+getFileName(url);
			return path + File.separator + url;
		}
		return null;
	}

	/**
	 * 删除文件，可以是单个文件或文件夹
	 * 
	 * @param filePath
	 *            待删除的文件名
	 * @param filter
	 *            过滤包含filter字符的文件或文件夹
	 * @return 文件删除成功返回true,否则返回false
	 */

	public static boolean delete(String filePath, final String filter) {
		File file = new File(filePath);
		String[] files = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filter.contains(filename)) {
					return false;
				}
				return true;
			}
		});
		if (files != null && files.length > 0) {
			for (String f : files) {
				delete(filePath + File.separator + f);
			}
		}
		return true;
	}

	/**
	 * 删除文件，可以是单个文件或文件夹
	 * 
	 * @param fileName
	 *            待删除的文件名
	 * @param filters
	 *            过滤的文件或文件夹,区分大小写
	 * @return 文件删除成功返回true,否则返回false
	 */

	/*public static boolean delete(final String filePath, final String... filters) {
		File file = new File(filePath);
		String[] files = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filters != null) {
					if (ArrayUtils.contains(filters, filePath + File.separator
							+ filename)) {
						return false;
					}
				}
				return true;
			}
		});
		if (files != null && files.length > 0) {
			for (String f : files) {
				delete(filePath + File.separator + f);
			}
		}
		return true;
	}*/

	/**
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true,否则返回false
	 */
	private static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			return true;

		} else {
			return false;

		}
	}

	/**
	 * 
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param dir
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true,否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		if (!dir.endsWith(File.separator)) {// 如果dir不以文件分隔符结尾，自动添加文件分隔符
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		if (!dirFile.exists() || !dirFile.isDirectory()) {// 如果dir对应的文件不存在，或者不是一个目录，则退出
			return false;
		}
		boolean flag = true;
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			} else {// 删除子目录
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}
		if (!flag) {
			return false;

		}
		if (dirFile.delete()) {// 删除当前目录
			return true;

		} else {
			return false;
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @return
	 */
	public static boolean createDir(File path) {
		boolean r = false;
		if (path != null && !path.exists()) {
			r = path.mkdirs();
		}
		return r;
	}

	/**
	 * 得到父目录
	 * 
	 * @param path
	 * @return
	 */
	public static String getParentPath(String path) {
		File file = new File(path);
		return file.getParent();
	}

	/**
	 * 把字符串写入文件
	 * 
	 * @param str
	 */
	public static void write(String path, byte[] str) {
		if (path != null && str != null) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(path);
				fos.write(str);
			} catch (Exception e) {
				MxUtils.w(e.getMessage());
			} finally {
				try {
					if (fos != null)
						fos.close();
				} catch (IOException e) {
					MxUtils.w(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 删除指定目录下的某些文件
	 * @param dir 指定目录
	 * @param tag 文件名标记
	 * @param timeLen 时间段（多少天）
	 * @return
	 */
	public static boolean deleteFilesInDirectory(String dir, String tag, int timeLen) {
		if (!dir.endsWith(File.separator)) {// 如果dir不以文件分隔符结尾，自动添加文件分隔符
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		if (!dirFile.exists() || !dirFile.isDirectory()) {// 如果dir对应的文件不存在，或者不是一个目录，则退出
			return false;
		}
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			String fileName=files[i].getName();
			//System.out.println("search file:"+fileName);
			if (files[i].isFile()&&fileName.startsWith(tag)) {
				int lastPoint=fileName.lastIndexOf(".");
				String date=fileName.substring(tag.length(), (lastPoint>0)?lastPoint:fileName.length()-1);
				//System.out.println("date:"+date);
				long timeStamp= MxTimeUtils.conversionDateToLong(date, MxTimeUtils.TimeFormatType.TIME_FOEMAT_Y_M_D);
				long curTime= System.currentTimeMillis();
				//System.out.println("timeStamp:"+timeStamp+"   curTimeStamp:"+curTime);
				if((curTime-timeStamp)>=timeLen* MxTimeUtils.UNIT_MS_DAY){
					String filePath=files[i].getAbsolutePath();
					//System.out.println("delete file:"+filePath);
					deleteFile(filePath);
				}
			}
		}
		return true;
	}
}