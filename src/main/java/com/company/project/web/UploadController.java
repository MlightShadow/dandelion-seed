package com.company.project.web;

import java.io.File;
import java.io.IOException;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 文件上传控制器
 */
@ApiIgnore
@Api(description = "文件上传")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping
    @ResponseBody
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultGenerator.genFailResult("上传失败");
        }

        String fileName = file.getOriginalFilename();
        String filePath = "/file_upload";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
        } catch (IllegalStateException | IOException e) {
            return ResultGenerator.genFailResult("上传失败");
        }

        return ResultGenerator.genSuccessResult("上传成功");
    }
}
