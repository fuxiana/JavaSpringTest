package com.xiaofutest.repository;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.xiaofutest.Config.oss.OSSConfig;
import com.xiaofutest.model.OssDTO;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OSSUploader {

    // 上传本地文件
    public static String uploadFile(String objectName, File file) {
        OSS ossClient = OSSConfig.createOSSClient();
        try {
            PutObjectRequest request = new PutObjectRequest(
                    OSSConfig.BUCKET_NAME,
                    objectName,  // OSS 中的文件路径，如 "images/avatar.jpg"
                    file
            );
            ossClient.putObject(request);
            return generateUrl(objectName); // 生成访问 URL
        } finally {
            ossClient.shutdown();
        }
    }

    // 上传文件流（常用）
    public static String uploadInputStream(String objectName, InputStream inputStream) {
        OSS ossClient = OSSConfig.createOSSClient();
        try {
            ossClient.putObject(OSSConfig.BUCKET_NAME, objectName, inputStream);
            return generateUrl(objectName);
        } finally {
            ossClient.shutdown();
        }
    }

    // 生成访问 URL（私有文件需签名）
    private static String generateUrl(String objectName) {
        return "https://" + OSSConfig.BUCKET_NAME + "." + OSSConfig.ENDPOINT + "/" + objectName;
    }

    public static OssDTO getOssConfig(Integer id){
        // SQL 查询语句
        String sql = "SELECT * FROM config WHERE id = ?";
        Connection connection = RequestDB.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // 设置参数（id = 1）
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 读取查询结果
                    OssDTO ossDTO = new OssDTO();
                    ossDTO.setAccessKeyId(rs.getString("accessKeyId"));
                    ossDTO.setAccessKeySecret(rs.getString("accessKeySecret"));
                    return  ossDTO;
                } else {
                    System.out.println("未找到 ID 为 1 的记录");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}