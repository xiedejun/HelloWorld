package com.tcl.example.com.tcl.dejun.xie.ReadAndWriteData;

import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import android.text.format.Formatter;

/**
 * Created by dejun.xie on 2016/9/6.
 */
public class GetMemoryUtil {

    //工具类禁止被创建
    private GetMemoryUtil(){

    }

    public static String getTotalMemory(Context context,String path){
        StatFs statFs=new StatFs(path);
        String totalSize;

        if(path==null || path.trim().length()<=0 ){
            try {
                throw new Exception("memory path is null or empty");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //根据API版本的不同调用不同的获取内存的方法（4.3或18以上，才能使用statFs.getBlockSizeLong()等方法）
        if(Build.VERSION.SDK_INT>=18){
            //获取每个内存块的大小,返回的单位是字节
            long eachBlockSize=statFs.getBlockSizeLong();
            long totalBlocks=statFs.getBlockCountLong();
//			long availableBlocks=statFs.getAvailableBlocksLong();
            totalSize= Formatter.formatFileSize(context, eachBlockSize * totalBlocks);
        }else{
            int eachBlockSize=statFs.getBlockSize();
            int totalBlocks=statFs.getBlockCount();
//			int availableBlocks=statFs.getAvailableBlocks();
            totalSize=Formatter.formatFileSize(context, eachBlockSize*totalBlocks);
        }
        return totalSize;
    }

    public static String getAvaiableMemory(Context context,String path){
        StatFs statFs=new StatFs(path);
        String availableSize;

        if(path==null || path.trim().length()<=0){
            try {
                throw new Exception("memory path is null or empty");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //根据API版本的不同调用不同的获取内存的方法（4.3或18以上，才能使用statFs.getBlockSizeLong()等方法）
        if(Build.VERSION.SDK_INT>=18){
            //获取每个内存块的大小,返回的单位是字节
            long eachBlockSize=statFs.getBlockSizeLong();
            long availableBlocks=statFs.getAvailableBlocksLong();
            availableSize=Formatter.formatFileSize(context, eachBlockSize*availableBlocks);
        }else{
            int eachBlockSize=statFs.getBlockSize();
            int availableBlocks=statFs.getAvailableBlocks();
            availableSize=Formatter.formatFileSize(context, eachBlockSize*availableBlocks);
        }
        return availableSize;
    }

    public static String getUsedMemory(Context context,String path){
        StatFs statFs=new StatFs(path);
        String usedMemory;

        if(path==null || path.trim().length()<=0){
            try {
                throw new Exception("memory path is null or empty");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //根据API版本的不同调用不同的获取内存的方法（4.3或18以上，才能使用statFs.getBlockSizeLong()等方法）
        if(Build.VERSION.SDK_INT>=18){
            //获取每个内存块的大小,返回的单位是字节
            long eachBlockSize=statFs.getBlockSizeLong();
            long totalBlocks=statFs.getBlockCountLong();
            long availableBlocks=statFs.getAvailableBlocksLong();
            usedMemory=Formatter.formatFileSize(context, eachBlockSize*(totalBlocks-availableBlocks));
        }else{
            int eachBlockSize=statFs.getBlockSize();
            int totalBlocks=statFs.getBlockCount();
            int availableBlocks=statFs.getAvailableBlocks();
            usedMemory=Formatter.formatFileSize(context, eachBlockSize*(totalBlocks-availableBlocks));
        }
        return usedMemory;
    }

    public static long getAvaiableMemorySize(Context context,String path){
        StatFs statFs=new StatFs(path);
        long availableSize=0;

        if(path==null || path.trim().length()<=0){
            try {
                throw new Exception("memory path is null or empty");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //根据API版本的不同调用不同的获取内存的方法（4.3或18以上，才能使用statFs.getBlockSizeLong()等方法）
        if(Build.VERSION.SDK_INT>=18){
            //获取每个内存块的大小,返回的单位是字节
            long eachBlockSize=statFs.getBlockSizeLong();
            long availableBlocks=statFs.getAvailableBlocksLong();
            availableSize=eachBlockSize*availableBlocks;
        }else{
            int eachBlockSize=statFs.getBlockSize();
            int availableBlocks=statFs.getAvailableBlocks();
            availableSize=eachBlockSize*availableBlocks;
        }
        return availableSize;
    }
}
