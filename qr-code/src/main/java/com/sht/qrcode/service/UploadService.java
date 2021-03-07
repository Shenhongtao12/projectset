package com.sht.qrcode.service;

import cn.hutool.core.util.IdUtil;
import com.sht.qrcode.utils.JsonData;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;


/**
 * 图片上传
 * @author Administrator
 */
@Slf4j
@Service
public class UploadService {
    //private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

    public JsonData upload(MultipartFile file, String site) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                log.error("上传失败，文件内容不符合");
                return JsonData.buildError("上传失败，文件内容不符合");
            }
            Long size = file.getSize();
            if (size >= 8 * 1024 * 1024) {
                log.error("文件过大：" + size + " 字节");
                return JsonData.buildError("文件大小不能超过10M");
            }
            //原图
            String fileName = file.getOriginalFilename();
            //截取图片后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));

            String str = ".jpg, .jpeg, .png, .gif, .JPG, .JPEG, .PNG, .GIF";
            if (!str.contains(suffixName)) {
                log.error("格式错误: " + suffixName);
                return JsonData.buildError("请上传文件格式 'jpg, jpeg, png, gif' 的图片");
            }

            String uuid = IdUtil.simpleUUID();
            fileName = uuid + suffixName;

            File dir = new File(site);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.transferTo(new File(dir, fileName));

            //缩略图
            String thumbnailName = uuid + "thumbnail" + suffixName;

            String thumbnailUrl = "http://101.201.151.118:8889" + site + "/" + thumbnailName;

            if (size < 200 * 1024) {
                Thumbnails.of(new String[]{site + "/" + fileName}).scale(1.0D).toFile(site + "/" + thumbnailName);
            } else if (size < 900 * 1024){
                Thumbnails.of(new String[]{site + "/" + fileName}).scale(1.0D).outputQuality(0.4f).toFile(site + "/" + thumbnailName);
            }else {
                Thumbnails.of(new String[]{site + "/" + fileName}).scale(1.0D).outputQuality(0.2f).toFile(site + "/" + thumbnailName);
            }
            return JsonData.buildSuccess(thumbnailUrl, "");
        } catch (Exception e) {
            return JsonData.buildError(e.getMessage(), "上传失败，其他错误");
        }
    }
}
