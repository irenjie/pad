package com.hydeze.hypad.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hydeze.hypad.entity.PadKey;
import com.hydeze.hypad.entity.PadPerson;
import com.hydeze.hypad.service.IPadKeyService;
import com.hydeze.hypad.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ma
 * @since 2020-08-06
 */
@RestController
@RequestMapping("/hypad")
public class PadKeyController {

    @Autowired
    IPadKeyService iPadKeyService;

    /*
     * 关键字列表
     * @param currentPage 当前页
     * @RequestParam(defaultValue = "1") URL占位符绑定到方法参数上
     */
    @GetMapping("/keys")
    public Result keysList() {
        List<PadKey> list = iPadKeyService.list();
        return Result.success(200, "成功加载关键字", list);
    }


    /*
     * 关键字详情
     * @param id 根据 id 查询关键字
     */
    @GetMapping("/key/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        PadKey key = iPadKeyService.getById(id);
        Assert.notNull(key, "关键字不存在");
        return Result.success(200, "成功读取关键字信息", key);
    }

    /**
     * 获取所有关键字名称
     */
    @GetMapping("/keysname")
    public Result getKeysName() {
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        List<PadKey> list = iPadKeyService.list();
        return null;
    }

    /**
     * 关键字删除
     */
    @GetMapping("/key/delete/{id}")
    public Result deleteKeys(@PathVariable(name = "id") Long id) {
        return Result.success("功能测试中,去使用批量删除");
    }

    /**
     * 关键字批量删除
     *
     * @param ids 批量关键字 ID,用 - 连接
     */
    @GetMapping("/keys/delete/{ids}")
    public Result deleteMultKeys(@PathVariable(name = "ids") String ids) {
        List<String> keyList = Arrays.asList(ids.split("-"));
        iPadKeyService.removeByIds(keyList);
        return Result.success("删除关键字成功!");
    }

    /*
     * 关键字编辑
     * @param padKey
     */
    @PostMapping("/keys/edit")
    public Result keyEdit(@RequestBody PadKey padKey) {
        padKey.setGmtCreate(LocalDateTime.now());
        padKey.setGmtModified(LocalDateTime.now());
        padKey.setIsDelete(0);
        iPadKeyService.save(padKey);
        return Result.success("关键字添加成功");
    }
}
