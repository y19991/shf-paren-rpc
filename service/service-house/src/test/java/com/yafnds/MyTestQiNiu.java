package com.yafnds;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * 类描述：
 * <p>创建时间：2022/10/7/007 16:31
 *
 * @author yafnds
 * @version 1.0
 */

public class MyTestQiNiu {

    /**
     * 七牛云文件上传
     */
    @Test
    public void testUpload() {
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());

        UploadManager uploadManager = new UploadManager(cfg);

        // 密匙与空间名
        String accessKey = "WUGDbJ5z6XhTFRf5ysgSt5pXSO0vhynhZtyCB3PE";
        String secretKey = "5clUVzlo0JSQcfdD35-S3g0YmVuCGWYq2WlNeMnd";
        String bucket = "atguigu-yafnds";

        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "d:/qiniu/test.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.out.println(r);
            try {
                System.out.println(r.bodyString());
            } catch (QiniuException e) {
                // 忽略
            }
        }
    }

    /**
     * 七牛云文件删除
     */
    @Test
    public void deleteFile() {
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());

        // 密匙与空间名
        String accessKey = "WUGDbJ5z6XhTFRf5ysgSt5pXSO0vhynhZtyCB3PE";
        String secretKey = "5clUVzlo0JSQcfdD35-S3g0YmVuCGWYq2WlNeMnd";
        String bucket = "atguigu-yafnds";
        String key = "2022/10/11/cffcbf58-0af7-40b1-aa7a-4420c7985cca.jpg";

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException e) {
            // 异常说明删除失败
            System.out.println(e.code());
            System.out.println(e.response);
        }
    }

    /**
     * 查询文件详情
     */
    @Test
    public void detils() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());

        // 密匙与空间名
        String accessKey = "WUGDbJ5z6XhTFRf5ysgSt5pXSO0vhynhZtyCB3PE";
        String secretKey = "5clUVzlo0JSQcfdD35-S3g0YmVuCGWYq2WlNeMnd";
        String bucket = "atguigu-yafnds";
        String key = "Frcwajn2dA_eTUldm2KencBoM3Xn";

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            FileInfo fileInfo = bucketManager.stat(bucket, key);
            System.out.println(fileInfo.hash);
            System.out.println(fileInfo.fsize);
            System.out.println(fileInfo.mimeType);
            System.out.println(fileInfo.putTime);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }

    }
}
