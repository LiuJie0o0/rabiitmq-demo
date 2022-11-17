package ind.liuer.rabbitmq.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author LiuJie
 * @date 2022-11-07
 * @since 1.0
 */
@Slf4j
public class FileUtils {

    /**
     * 加载类路径下资源文件流
     *
     * @param filename 文件名称
     */
    public static InputStream getClasspathFileStream(String filename) {
        String builder = System.getProperty("user.dir") +
            File.separator + "rabbitmq-basic" +
            File.separator + "src" +
            File.separator + "main" +
            File.separator + "resources" +
            File.separator + filename;
        try {
            return new FileInputStream(builder);
        } catch (FileNotFoundException e) {
            log.error("文件[{}]加载失败: [{}]", builder, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
