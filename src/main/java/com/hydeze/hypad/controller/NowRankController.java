package com.hydeze.hypad.controller;


import com.hydeze.hypad.entity.NowRank;
import com.hydeze.hypad.entity.PadKey;
import com.hydeze.hypad.service.INowRankService;
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
 * @since 2020-08-19
 */
@RestController
@RequestMapping("/hypad")
public class NowRankController {

    @Autowired
    INowRankService nowRankService;

    /*
     * 职级详情
     *
     * @param rankId 根据 id 查询职级
     */
    @GetMapping("/rank/{rankId}")
    public Result detail(@PathVariable(name = "rankId") Long rankId) {
        NowRank nowRank = nowRankService.getById(rankId);
        Assert.notNull(nowRank, "职级不存在");
        return Result.success("OK", nowRank);
    }

    /*
     * 所有职级
     */
    @GetMapping("/ranks")
    public Result ranks() {
        List<NowRank> rankList = nowRankService.list();
        return Result.success("OK", rankList);
    }

    /**
     * 职级删除
     */
    @GetMapping("/rank/delete/{id}")
    public Result deleteKeys(@PathVariable(name = "id") Long id) {
        return Result.success("功能测试中,去使用批量删除");
    }

    /**
     * 职级批量删除
     *
     * @param ids 批量职级 ID,用 - 连接
     */
    @GetMapping("/ranks/delete/{ids}")
    public Result deleteMultKeys(@PathVariable(name = "ids") String ids) {
        List<String> rankList = Arrays.asList(ids.split("-"));
        nowRankService.removeByIds(rankList);
        return Result.success("删除关键字成功!");
    }

    /*
     * 职级编辑
     * @param nowRank
     */
    @PostMapping("/rank/edit")
    public Result keyEdit(@RequestBody NowRank nowRank) {
        nowRank.setIsDelete(0);
        nowRank.setCstCreate(LocalDateTime.now());
        nowRank.setCstModified(LocalDateTime.now());
        nowRankService.save(nowRank);
        return Result.success("职级添加成功");
    }

}
