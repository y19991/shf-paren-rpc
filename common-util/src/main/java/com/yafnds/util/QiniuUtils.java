package com.yafnds.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 类描述：七牛云工具类
 * <p>创建时间：2022/10/10/010 20:15
 *
 * @author yafnds
 * @version 1.0
 */
public class QiniuUtils {

    /** 七牛云密匙 AK */
    private static final String ACCESS_KEY = "WUGDbJ5z6XhTFRf5ysgSt5pXSO0vhynhZtyCB3PE";
    /** 七牛云密匙 SK */
    private static final String SECRET_KEY = "5clUVzlo0JSQcfdD35-S3g0YmVuCGWYq2WlNeMnd";
    /** 空间名称 */
    private static final String BUCKET = "atguigu-yafnds";
    /** 外链域名 */
    private static final String FILE_DNS = "http://rjbibpv28.hn-bkt.clouddn.com/";

    /**
     * 上传文件
     * @param filePath 文件地址
     * @param fileName 文件名称
     */
    public static void upload2Qiniu(String filePath,String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = uploadManager.put(filePath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 上传文件
     * @param bytes 文件转成的字节数组
     * @param fileName 要上传的文件名
     */
    public static void upload2Qiniu(byte[] bytes, String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 删除文件
     * @param fileName 要删除的文件名
     */
    public static void deleteFileFromQiniu(String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        String key = fileName;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(BUCKET, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    /**
     * 获取文件链接
     * @param uuidName 唯一文件名
     * @return 文件链接
     */
    public static String getUrl(String uuidName) {
        return FILE_DNS + uuidName;
    }

    /**
     * 获得图片在七牛云中的文件名（含目录）
     * @param url 文件链接
     * @return 图片在七牛云中的文件名（含目录）
     */
    public static String getFileNameFromUrl(String url) {
        int dnsIndex = url.indexOf(FILE_DNS);
        return url.substring(dnsIndex + FILE_DNS.length());
    }
}
