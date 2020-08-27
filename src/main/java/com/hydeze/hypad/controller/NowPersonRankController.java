package com.hydeze.hypad.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hydeze.hypad.entity.*;
import com.hydeze.hypad.service.INowPersonRankService;
import com.hydeze.hypad.service.INowPersonService;
import com.hydeze.hypad.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NowPersonRankController {

    @Autowired
    INowPersonRankService nowPersonRankService;

    @Autowired
    INowPersonService nowPersonService;

    /*
     * 返回公司员工历史职级
     *
     * @param nowpersonid 员工编号
     */
    @GetMapping("/nowpersonrank/{nowpersonid}")
    public Result ranksByNowpersonid(@PathVariable(name = "nowpersonid") String nowpersonid) {
        QueryWrapper<NowPersonRank> wrapper = new QueryWrapper<>();
        wrapper.eq("personid", nowpersonid);
        wrapper.orderBy(true, true, "shunxu");
        List<NowPersonRank> nowPersonRanks = nowPersonRankService.list(wrapper);
        if (nowPersonRanks != null)
            return Result.success("OK", nowPersonRanks);
        return Result.success("null");
    }

    /*
     * 根据人员 ID 获取最新职级
     *
     * @param nowpersonid 员工编号
     */
    @GetMapping("/lastRank/{nowpersonid}")
    public Result lastRank(@PathVariable(name = "nowpersonid") String nowpersonid) {
        NowPerson nowPerson = nowPersonService.getById(nowpersonid);
        QueryWrapper<NowPersonRank> wrapper = new QueryWrapper<>();
        wrapper.eq("personid", nowpersonid);
        wrapper.eq("shunxu", nowPerson.getRanknum());
        NowPersonRank personRank = nowPersonRankService.getOne(wrapper);
        if (personRank != null)
            return Result.success("OK", personRank);
        return Result.success("null");
    }

    /*
     * 根据 now_perosn_rank ID 删除记录
     *
     * @param nowpersonid 员工编号
     */
    @GetMapping("/nowpersonrank/delete/{id}")
    public Result deleteById(@PathVariable(name = "id") Long id) {
        NowPersonRank nowPersonRank = nowPersonRankService.getById(id);
        if (nowPersonRank != null) {
            UpdateWrapper<NowPerson> wrapper = new UpdateWrapper<>();
            wrapper.setSql("ranknum=ranknum-1 where id ='" + nowPersonRank.getPersonid() + "'");
            nowPersonService.update(wrapper);
            boolean isRemove = nowPersonRankService.removeById(id);
            if (isRemove) {
                return Result.success("删除职级记录成功");
            }
        }
        return Result.fail("删除职级记录失败");
    }

    /**
     * 人员职级添加
     *
     * @param nowPersonRank
     * @Validated 对 padPerson 进行验证
     */
    @PostMapping("/nowpersonrank/add")
    public Result edit(@RequestBody NowPersonRank nowPersonRank) {
        nowPersonRank.setIsDelete(0);
        nowPersonRank.setCstCreate(LocalDateTime.now());
        nowPersonRank.setCstModified(LocalDateTime.now());
        System.out.println(nowPersonRank);
        boolean isSave = nowPersonRankService.save(nowPersonRank);
        if (isSave) {
            UpdateWrapper<NowPerson> wrapper = new UpdateWrapper<>();
            wrapper.setSql("ranknum=ranknum+1 where id ='" + nowPersonRank.getPersonid() + "'");
            nowPersonService.update(wrapper);
            return Result.success("添加职级成功");
        }
        return Result.fail("添加职级失败");
    }

}
