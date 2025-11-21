package cn.arorms.android.ht.server.util

import com.aliyun.oss.ClientException
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.OSSException
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AliOssUtil(
    private val endpoint: String,
    private val accessKeyId: String,
    private val accessKeySecret: String,
    private val bucketName: String
) {
    private val log = LoggerFactory.getLogger(AliOssUtil::class.java)

    /**
     * 文件上传
     *
     * @param bytes 文件字节数组
     * @param objectName 原始文件名
     * @return 文件访问URL
     */
    fun upload(bytes: ByteArray, objectName: String): String {
        // 填写Object完整路径，例如202406/1.png。Object完整路径中不能包含Bucket名称。
        //获取当前系统日期的字符串,格式为 yyyy/MM
        val dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"))
        //生成一个新的不重复的文件名
        val newFileName = UUID.randomUUID().toString() + objectName.substring(objectName.lastIndexOf("."))
        val finalObjectName = "$dir/$newFileName"
        
        // 创建OSSClient实例。
        val ossClient: OSS = OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret)

        try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, finalObjectName, ByteArrayInputStream(bytes))
        } catch (oe: OSSException) {
            log.error("Caught an OSSException, which means your request made it to OSS, " +
                    "but was rejected with an error response for some reason.")
            log.error("Error Message: {}", oe.errorMessage)
            log.error("Error Code: {}", oe.errorCode)
            log.error("Request ID: {}", oe.requestId)
            log.error("Host ID: {}", oe.hostId)
        } catch (ce: ClientException) {
            log.error("Caught an ClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with OSS, " +
                    "such as not being able to access the network.")
            log.error("Error Message: {}", ce.message)
        } finally {
            ossClient.shutdown()
        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        val url = "https://$bucketName.$endpoint/$finalObjectName"

        log.info("文件上传到:{}", url)

        return url
    }
}

