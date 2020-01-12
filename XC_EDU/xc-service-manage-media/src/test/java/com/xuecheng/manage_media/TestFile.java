package com.xuecheng.manage_media;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestFile {

    //测试文件分块
    @Test
    public void testChunk() throws IOException {
        //源文件
        File sourceFile = new File("E:\\WebStormTest\\lucene.avi");
        //块文件目录
        String chunkFileFolder = "E:\\WebStormTest\\chunks\\";
        //先定义块文件大小
        long chunkFileSize = 5* 1024 * 1024;
        //块数
        long chunkFileNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);
        //创建文件的对象
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");
        //缓冲区
        byte[] b = new byte[1024];
        for (int i = 0; i < chunkFileNum; i++) {
            //块文件
            File chunkFile = new File(chunkFileFolder + i);
            //创建向块文件中的写对象
            RandomAccessFile raf_write = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
                //如果块文件的大小达到1M开始写下一块
                if (chunkFile.length() >= chunkFileSize) {
                    break;
                }
            }
            raf_write.close();
        }
        raf_read.close();
    }
    @Test
    public void  testMergeFile() throws IOException {
        //块文件目录
        String chunkFileFolderPath="E:\\WebStormTest\\chunks\\";
        //块文件目录对象
        File chunkFileFolder = new File(chunkFileFolderPath);
        //块文件列表
        File[] files = chunkFileFolder.listFiles();
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName())<Integer.parseInt(o2.getName())){
                    return -1;
                }
                return 1;
            }
        });
        //合并
        File mergeFile=new File("E:\\WebStormTest\\testMergeFile\\lucene_merge.avi");
        boolean newFile = mergeFile.createNewFile();
        //创建写对象
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
        byte[] b=new byte[1024];
        for (File chunkFile : fileList) {
            //创建一个读取块文件的对象
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
            int len=-1;
            while((len=raf_read.read(b))!=-1){
                raf_write.write(b,0,len);
            }
            raf_read.close();
        }
        raf_write.close();
    }
}
