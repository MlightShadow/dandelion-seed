package com.company.project.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.util.DateUtil;
import com.company.project.util.UUIDUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 文件控制器
 */
@Api(description = "文件")
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${web.upload-path}")
    private String filePath;

    @Autowired
    private UUIDUtil uuid;

    @PostMapping
    @ApiOperation(value = "文件上传, 文件访问host:port/file/yyyy/MM/xxxx.pdf")
    @ResponseBody
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ServiceException("文件异常");
        }

        String dateFolder = DateUtil.dateFormat("yyyy/MM/").format(new Date());
        String savePath = this.filePath + dateFolder;
        // 如果不存在,创建文件夹
        File f = new File(savePath);
        if (!f.exists()) {
            f.mkdirs();
        }

        String OriginalName = file.getOriginalFilename();
        String suffix = OriginalName.substring(OriginalName.lastIndexOf(".") + 1);
        String fileName = uuid.getUUID() + '.' + suffix;
        String savefileName = savePath + fileName;

        File dest = new File(savefileName);
        try {
            file.transferTo(dest);
        } catch (IllegalStateException | IOException e) {
            throw new ServiceException("文件异常");
        }

        // TODO 写file log

        return ResultGenerator.genSuccessResult(savefileName);
    }
}
