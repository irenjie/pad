package com.hydeze.hypad.controller;

import com.hydeze.hypad.utils.Result;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/hypad")
public class FileController {

    /*
     * 文件上传
     */
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> map) {
        if (file.isEmpty()) {
            return Result.fail("请选择上传的文件");
        }
        String originalFilename = file.getOriginalFilename(); // 带扩展
        // 服务器文件路径.需要两个 // , \ 需转义，
        String filePath = "//home//hypad//uploadfile//" + map.get("id") + "//";
        // 本地开发
        //String filePath = "D:\\Program\\JavaProgram\\hypad\\uploadfile\\" + map.get("id") + "\\";

        File dest = new File(filePath + originalFilename);
        if (!dest.getParentFile().exists()) {
            // 若路径不存在
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(500, originalFilename + "上传失败", null);
        }
        System.out.println("上传文件-"+originalFilename);
        return Result.success(originalFilename + "上传成功");
    }

    /*
     * 用户文件的文件名列表
     */
    @GetMapping("/filename/{personid}")
    public Result getFileNameById(@PathVariable(name = "personid") String personid) {
        // 服务器
        File file = new File("//home//hypad//uploadfile//" + personid + "//");
        // 本地
        //File file = new File("D:\\Program\\JavaProgram\\hypad\\uploadfile\\" + personid + "\\");
        if (!file.exists())
            return Result.success("还没有文件");
        String[] list = file.list();
        ArrayList<String> filenameList = new ArrayList<>();
        for (String s : list) {
            filenameList.add(s);
        }
        System.out.println(filenameList);
        return Result.success("OK", filenameList);
    }

    /*
     * 删除文件
     */
    @GetMapping("/deleteFile/{filename}")
    public Result deleteFile(@PathVariable(name = "filename") String filename) {
        // 服务器
        String dest = filename.replaceFirst("-", "//");
        File file = new File("//home//hypad//uploadfile//" + dest);
        // 本地
        /*String dest = filename.replaceFirst("-", "\\");
        File file = new File("D:\\Program\\JavaProgram\\hypad\\uploadfile\\" + dest);*/
        if (file.exists()) {
            file.delete();
            return Result.success("删除成功");
        }
        return Result.fail("删除失败");
    }

    /*
     * 下载文件
     */
    @GetMapping("/downloadFile/{filename}")
    public void downloadFile(@PathVariable(name = "filename") String filename, final HttpServletResponse response, final HttpServletRequest request) {

        System.out.println(filename);
        OutputStream os = null;
        InputStream is = null;
        // 服务器
        String dest = filename.replaceFirst("-", "//");
        File file = new File("//home//hypad//uploadfile//" + dest);
        // 本地
        /*String dest = filename.replaceFirst("-", "\\\\");
        File file = new File("D:\\Program\\JavaProgram\\hypad\\uploadfile\\" + dest);*/
        try {
            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setContentType("application/x-download;charset=GBK");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    new String(filename.substring(filename.indexOf("-") + 1).getBytes("utf-8"), "iso-8859-1"));
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            is = new FileInputStream(file);
            if (is == null) {
                return;
                /*return Result.fail("下载失败");*/
            }
            //复制
            IOUtils.copy(is, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            return;
            /*return Result.fail("下载附件失败,error:" + e.getMessage());*/
        }
        //文件的关闭放在finally中
        finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
