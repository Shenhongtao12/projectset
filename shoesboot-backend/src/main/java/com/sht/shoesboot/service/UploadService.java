package com.sht.shoesboot.service;

import com.sht.shoesboot.utils.RestResponse;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UploadService {
    //private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

    public RestResponse upload(MultipartFile[] fileArray, String site) {
        try {
            if (fileArray == null) {
                return new RestResponse(400,"文件不能为空");
            }
            Map<String, Object> result = new HashMap<>();
            String name = "";
            String url = "";
            //循环上传图片
            for (int i = 0; i < fileArray.length; i++) {
                MultipartFile file = fileArray[i];

                BufferedImage image = ImageIO.read(file.getInputStream());
                if (image == null) {
                    log.error("上传失败，文件内容不符合");
                    return new RestResponse(400, "上传失败，文件内容不符合");
                }
                Long size = file.getSize();
                if (size >= 10 * 1024 * 1024) {
                    log.error("文件过大：" + size + " 字节");
                    return new RestResponse(400, "文件大小不能超过10M");
                }
                //原图
                String fileName = file.getOriginalFilename();
                //截取图片后缀名
                String suffixName = fileName.substring(fileName.lastIndexOf("."));

                String str = ".jpg, .jpeg, .png, .gif, .JPG, .JPEG, .PNG, .GIF";
                if (!str.contains(suffixName)) {
                    log.error("格式错误: " + suffixName);
                    return new RestResponse(400, "请上传文件格式 'jpg, jpeg, png, gif' 的图片");
                }

                UUID uuid = UUID.randomUUID();
                fileName = uuid + suffixName;

                File dir = new File(site);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                file.transferTo(new File(dir, fileName));

                //缩略图
                String thumbnailName = uuid + "thumbnail" + suffixName;

                String thumbnailUrl = "http://182.92.218.236:8888" + site + "/" + thumbnailName;

                if (size < 200 * 1024) {
                    Thumbnails.of(new String[]{site + "/" + fileName}).scale(1.0D).toFile(site + "/" + thumbnailName);
                } else if (size < 900 * 1024){
                    Thumbnails.of(new String[]{site + "/" + fileName}).scale(1.0D).outputQuality(0.4f).toFile(site + "/" + thumbnailName);
                }else {
                    Thumbnails.of(new String[]{site + "/" + fileName}).scale(1.0D).outputQuality(0.2f).toFile(site + "/" + thumbnailName);
                }
                if (i == 0 ){
                    name += thumbnailName;
                    url += thumbnailUrl;
                }else {
                    name += ","+thumbnailName;
                    url += ","+thumbnailUrl;
                }
            }
            result.put("thumbnailName", name);
            result.put("thumbnailUrl", url);
            return new RestResponse(200, result, "上传成功");
        } catch (Exception e) {
            return new RestResponse(500, e.getMessage(), "上传失败，其他错误");
        }
    }

}
