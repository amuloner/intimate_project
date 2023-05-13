package com.wu.intimate.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.model.Notice;
import com.wu.intimate.service.NoticeService;
import com.wu.intimate.service.TestListService;
import com.wu.intimate.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/im/sys")
@Api("一些工具接口")
public class SystemController {

    @Value("${img_url}")
    private String url;

    @Autowired
    private NoticeService noticeServiceImpl;

    //图片上传
    @PostMapping("/fileUpload")
    @ApiOperation("图片上传接口")
    public MyResult fileUpload(@RequestParam("file") MultipartFile srcFile, @RequestParam("type") String type) {

        String message = "";
        //前端没有选择文件，srcFile为空
        if(srcFile.isEmpty()) {
            message = "请选择一个文件";
        }
        //选择了文件，开始上传操作
        try {
            //构建上传目标路径，找到项目的target的classes目录(项目的相对路径)、资源文件夹
            String webPath = new File(ResourceUtils.getURL("classpath:").getPath() + "static\\images\\" + type).getAbsolutePath();
            String localPath = new File(ResourceUtils.getURL("").getPath() + "\\src\\main\\resources\\static\\images\\" + type).getAbsolutePath();
            webPath = java.net.URLDecoder.decode(webPath, "utf-8");
            localPath = java.net.URLDecoder.decode(localPath, "utf-8");

            System.out.println("本地上传路径："+localPath);
            System.out.println("target路径："+webPath);

            File webFile = new File(webPath);
            File localFile = new File(localPath);
            //验证文件夹是否存在
            if(!webFile.exists())
                webFile.mkdirs();
            if(!localFile.exists())
                localFile.mkdirs();

            // 获得文件原始名称
            String fileName = srcFile.getOriginalFilename();
            //使用UUID生成文件新名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // 获得文件后缀名称
            assert fileName != null;
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if(!StringUtils.isNullOrEmpty(suffixName) && suffixName.equals("jpeg"))
                suffixName = "jpg";
            // 生成最新的文件名称
            String newFileName = uuid + "."+ suffixName;

            OutputStream localOut = new FileOutputStream(localPath + "\\" + newFileName);
            OutputStream webOut = new FileOutputStream(webPath + "\\" + newFileName);
            localOut.write(srcFile.getBytes());//srcFile为MultipartFile对象
            webOut.write(srcFile.getBytes());//srcFile为MultipartFile对象
            localOut.flush();webOut.flush();//释放
            localOut.close();webOut.close();//关闭输出流

            return MyResult.ok("/images/"+ type + "/" +  newFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MyResult.fail(message);
    }

    //文件长传
    @PostMapping("/filesUpload")
    @ApiOperation("文件上传接口")
    public MyResult filesUpload(@RequestParam("file") MultipartFile srcFile) {

        try {
            //构建上传目标路径，找到项目的target的classes目录(项目的相对路径)、资源文件夹
            String webPath = new File(ResourceUtils.getURL("classpath:").getPath() + "static\\file\\").getAbsolutePath();
            String localPath = new File(ResourceUtils.getURL("").getPath() + "\\src\\main\\resources\\static\\file\\").getAbsolutePath();
            webPath = java.net.URLDecoder.decode(webPath, "utf-8");
            localPath = java.net.URLDecoder.decode(localPath, "utf-8");

            System.out.println("本地上传路径：" + localPath);
            System.out.println("target路径：" + webPath);

            File webFile = new File(webPath);
            File localFile = new File(localPath);
            //验证文件夹是否存在
            if (!webFile.exists())
                webFile.mkdirs();
            if (!localFile.exists())
                localFile.mkdirs();

            // 获得文件原始名称
            String fileName = srcFile.getOriginalFilename();
            //使用UUID生成文件新名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // 获得文件后缀名称
            assert fileName != null;
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

            // 生成最新的文件名称
            String newFileName = uuid + "." + suffixName;

            OutputStream localOut = new FileOutputStream(localPath + "\\" + newFileName);
            OutputStream webOut = new FileOutputStream(webPath + "\\" + newFileName);
            localOut.write(srcFile.getBytes());//srcFile为MultipartFile对象
            webOut.write(srcFile.getBytes());//srcFile为MultipartFile对象
            localOut.flush();
            webOut.flush();//释放
            localOut.close();
            webOut.close();//关闭输出流

            return MyResult.ok("/file" + "/" + newFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MyResult.fail();
    }

    //公告发布
    @PostMapping("/addNotice")
    @ApiOperation("公告发布")
    public MyResult addNotice(@RequestBody Notice notice){
        Date date = new Date(System.currentTimeMillis());
        notice.setCtime(date);
        notice.setStatus(1);//发布状态
        noticeServiceImpl.save(notice);
        return MyResult.ok();
    }

    //根据id修改公告
    @PostMapping("/editNoticeById")
    @ApiOperation("根据id修改公告")
    public MyResult editNoticeById(@RequestBody Notice notice){
        notice.setStatus(0);//发布状态
        noticeServiceImpl.updateById(notice);
        return MyResult.ok();
    }

    //根据id删除公告
    @PostMapping("/deleteNoticeById")
    @ApiOperation("根据id删除公告")
    public MyResult deleteNoticeById(@RequestBody Notice notice){
        noticeServiceImpl.removeById(notice.getId());
        return MyResult.ok();
    }

    //查找公告 status 0、1
    @GetMapping("/getNoticeList/{status}")
    @ApiOperation("查找公告")
    public MyResult getNoticeList(@PathVariable("status") Integer status){
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        List<Notice> list = noticeServiceImpl.list(wrapper);
        return MyResult.ok(list);
    }
}
