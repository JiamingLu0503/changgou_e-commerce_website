package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FastDFSUtil {
    private static Logger logger = LoggerFactory.getLogger(FastDFSUtil.class);

    /***
     * 初始化与FastDFS的TrackerServer连接
     */
    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getPath();
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            logger.error("FastDFS Client Initialization Failed.", e);
        }
    }
    // 创建一个Tracker访问的客户端对象TrackerClient
    // 通过TrackerClient访问TrackerServer服务，获取链接信息
    // 通过TrackerServer的链接信息可以获取Storage的链接信息，创建StorageClient对象存储Storage的链接信息
    // 通过StorageClient访问Storage，实现文件上传，并且获取文件上传后的存储信息

    /***
     * 文件上传
     * @param file
     * @return 1.文件的组名  2.文件的路径信息
     */
    public static String[] upload(FastDFSFile file) {
        //获取文件的名称，作者以及MD5
        NameValuePair[] meta_list = new NameValuePair[3];
        meta_list[0] = new NameValuePair("name", file.getName());
        meta_list[1] = new NameValuePair("author", file.getAuthor());
        meta_list[2] = new NameValuePair("md5", file.getMd5());

        //接收返回数据
        String[] uploadResults = null;
        StorageClient storageClient = null;
        try {
            //创建StorageClient客户端对象
            storageClient = getStorageClient();

            /***
             * 文件上传
             * 1)文件字节数组
             * 2)文件扩展名
             * 3)文件的名称，作者以及MD5
             */
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        } catch (Exception e) {
            logger.error("Exception occurred uploading the file:" + file.getName(), e);
        }

        if (uploadResults == null) {
            logger.error("Upload file failed, error code: " + storageClient.getErrorCode());
        }
        /*uploadResult[]:
            1: 文件上传所存储的Storage的组的名字: group1
            2: 文件存储到Storage上的文件名字: M00/02/44/itheima.jpg*/
        return uploadResults;
    }

    /***
     * 获取文件信息
     * @param groupName:组名
     * @param remoteFileName：文件存储完整名
     * @return
     */
    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getStorageClient();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            logger.error("Exception occurred fetching the file info", e);
        }
        return null;
    }

    /***
     * 文件下载
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downFile(String groupName, String remoteFileName) {
        try {
            //创建StorageClient
            StorageClient storageClient = getStorageClient();
            //下载文件
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (Exception e) {
            logger.error("Exception occurred downloading the file", e);
        }
        return null;
    }

    /***
     * 文件删除
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName, String remoteFileName) {
        try {
            //创建StorageClient
            StorageClient storageClient = getStorageClient();
            //删除文件
            int i = 0;
            if ((i = storageClient.delete_file(groupName, remoteFileName)) != 0) {
                logger.error("Failed to remove the file from storage with error code " + i);
            }
        } catch (Exception e) {
            logger.error("Exception occurred removing the file", e);
        }
    }

    /***
     * 获取Storage
     * @return
     */
    public static StorageServer getStorageServer() {
        try {
            //创建TrackerClient
            TrackerClient trackerClient = new TrackerClient();
            //获取TrackerServer
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取Storage组
            return trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            logger.error("Exception occurred fetching the storage server to upload file", e);
        }
        return null;
    }

    /***
     * 获取Storage信息, IP和端口
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName) {
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
        } catch (Exception e) {
            logger.error("Exception occurred fetching the storage servers to download file", e);
        }
        return null;
    }

    /***
     * 获取Tracker服务地址(ip:port)与Nginx服务地址(ip:port)保持一致
     * @return
     * @throws IOException
     */
    public static String getTrackerUrl() throws IOException {
        return "http://" + getTrackerServer().getInetSocketAddress().getHostString() + ":" + ClientGlobal.getG_tracker_http_port() + "/";
    }

    /***
     * 获取StorageClient
     * @return
     * @throws IOException
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    /***
     * 获取TrackerServer
     * @return
     * @throws IOException
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    public static void main(String[] args) throws Exception {
//        // download the file
//        InputStream is = downFile("groupName", "remoteFileName");
//        // write the file to disk
//        FileOutputStream os = new FileOutputStream("picture");
//        // initialize a byte buffer
//        byte[] buffer = new byte[1024];
//        while(is.read(buffer) != -1) {
//            os.write(buffer);
//        }
//        os.flush();
//        os.close();
//        is.close();
    }
}
