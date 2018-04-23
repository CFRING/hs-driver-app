package com.hongshi.wuliudidi.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

public class ImageUtil {
	public static InputStream getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		float hh = 700f;
		float ww = 700f;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		// be=1表示不缩放
		int be = 1;
		if (w > h && w > ww) {
			// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
		{
			be = 1;
		}
		// 设置缩放比例
		newOpts.inSampleSize = be;
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// 压缩好比例大小后再进行质量压缩
		return compressImage(bitmap);
	}
	/**
	 * 得到Bitmap
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		float hh = 700f;
		float ww = 700f;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		// be=1表示不缩放
		int be = 1;
		if (w > h && w > ww) {
			// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
		{
			be = 1;
		}
		// 设置缩放比例
		newOpts.inSampleSize = be;
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// 压缩好比例大小后再进行质量压缩
		return bitmap;
	}

	public static InputStream compressImage(Bitmap image) {

		if(image == null){
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) {
			// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			// 重置baos即清空baos
			baos.reset();
			// 每次都减少10
			options -= 10;
			// 这里压缩options%，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);

		}
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

		// Bitmap bitmap = BitmapFactory.decodeStream(isBm, null,
		// null);//把ByteArrayInputStream数据生成图片
		// return bitmap;
		return isBm;
	}

    // 保存拍摄的照片到手机的sd卡  
	public static void SavePicInLocal(String fileName,Bitmap bitmap) {  
        FileOutputStream fos = null;  
        BufferedOutputStream bos = null;  
        ByteArrayOutputStream baos = null;
        try {  
           baos = new ByteArrayOutputStream();  
           bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
            byte[] byteArray = baos.toByteArray();
            String saveDir = Environment.getExternalStorageDirectory()  
                    + fileName;  
            File dir = new File(saveDir);  
            if (!dir.exists()) {  
                dir.mkdir();
            }  
            File file = new File(fileName);  
            file.delete();  
            if (!file.exists()) {  
                file.createNewFile();
//               Toast.makeText(TackPhoto.this, fileName, Toast.LENGTH_LONG)  
//                        .show();  
           }  
           // 将字节数组写入到刚创建的图片文件中  
           fos = new FileOutputStream(file);
           bos = new BufferedOutputStream(fos);
           bos.write(byteArray);  
 
       } catch (Exception e) {  
           e.printStackTrace();  
 
      } finally {  
            if (baos != null) {  
              try {  
                   baos.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
           if (bos != null) {  
                try {  
                   bos.close();  
                } catch (Exception e) {  
                   e.printStackTrace();  
               }  
            }  
            if (fos != null) {  
                try {  
                   fos.close();  
               } catch (Exception e) {  
                    e.printStackTrace();  
                }  
           }  
 
        }  
  
    }  


	@SuppressLint("NewApi")
	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.parseLong(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
			{
				return imageUri.getLastPathSegment();
			}
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	public static byte[] readStream(InputStream inStream) throws Exception
	{
		byte[] buffer = new byte[1024];

		int len = -1;

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		while ((len = inStream.read(buffer)) != -1)

		{

			outStream.write(buffer, 0, len);

		}

		byte[] data = outStream.toByteArray();

		outStream.close();

		inStream.close();

		return data;

	}
	
	/**
	 * 对于布局上没有指明宽高的图片，按屏幕分辨率对其进行缩放处理
	 */
	@SuppressLint("NewApi")
	public static Bitmap resizeBitmap(Activity context, Bitmap bitMap, int x, int y){
		//获取屏幕宽高
		Point screenSize = new Point();
		context.getWindowManager().getDefaultDisplay().getSize(screenSize);
		// 计算缩放比例   
		double scale = screenSize.x / 720.0;
	    
		Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(bitMap, (int)(x*scale), (int)(y*scale));
	    return resizeBitmap;
	}
}
