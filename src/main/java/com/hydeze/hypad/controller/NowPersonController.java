package com.hydeze.hypad.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hydeze.hypad.entity.*;
import com.hydeze.hypad.service.INowPersonRankService;
import com.hydeze.hypad.service.INowPersonService;
import com.hydeze.hypad.service.INowRankService;
import com.hydeze.hypad.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

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
public class NowPersonController {

    @Autowired
    INowPersonService nowPersonService;

    @Autowired
    INowRankService nowRankService;

    @Autowired
    INowPersonRankService nowPersonRankService;

    /*
     * 公司员工列表
     * @param currentPage 当前页
     * @RequestParam(defaultValue = "1") URL占位符绑定到方法参数上
     */
    @GetMapping("/nowperson")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 20);
        IPage pageData = nowPersonService.page(page, new QueryWrapper<NowPerson>().orderByDesc("cst_create"));


        return Result.success(200, "成功加载在职人员", pageData);
    }

    /*
     * 公司员工详情
     * @param id 根据 id 查询人员
     */
    @GetMapping("/nowperson/{id}")
    public Result detail(@PathVariable(name = "id") String id) {
        NowPerson nowPerson = nowPersonService.getById(id);
        System.out.println(nowPerson.getCstResign());
        Assert.notNull(nowPerson, "人员不存在");
        return Result.success(200, "成功读取 " + nowPerson.getName() + " 的信息", nowPerson);
    }

    /**
     * 公司员工编辑 修改或新建
     *
     * @param nowPerson
     */
    @PostMapping("/nowperson/edit")
    public Result edit(@RequestBody NowPerson nowPerson) {
        NowPerson temp = nowPersonService.getById(nowPerson.getId());
        if (temp == null) {
            nowPerson.setCstCreate(LocalDateTime.now());
        }
        nowPerson.setIsDelete(0);
        nowPerson.setCstModified(LocalDateTime.now());
        nowPersonService.saveOrUpdate(nowPerson);

        if (nowPerson.getCstResign() == null) {
            UpdateWrapper<NowPerson> wrapper = new UpdateWrapper<>();
            wrapper.setSql("cst_resign=null where id='" + nowPerson.getId() + "'");
            nowPersonService.update(nowPerson, wrapper);
        }
        return Result.success(nowPerson.getName() + " 的信息录入成功");
    }

    /*
     * 根据职级名字查找人员列表,最近职级符合
     *
     * @param ranksname 为职级集，用 - 连接
     */
    public List<NowPerson> searchPersonByLastRank(String ranksname) {
        // 职级集名字转为 id list
        List<String> ranks = Arrays.asList(ranksname.split("-"));
        QueryWrapper<NowRank> wrapper = new QueryWrapper<>();
        wrapper.in("name", ranks);
        wrapper.select("id");
        List<NowRank> rankList = nowRankService.list(wrapper);
        ArrayList<Long> ranksId = new ArrayList<>();
        rankList.forEach(item -> {
            ranksId.add(item.getId());
        });

        // 查找所有带职级的人
        QueryWrapper<NowPerson> wrapper1 = new QueryWrapper<>();
        wrapper.ne("ranknum", 0);
        List<NowPerson> nowPersonList = nowPersonService.list(wrapper1);

        // 循环每个人，这人最近职级 in ranksId
        List<NowPerson> result = new ArrayList<>();
        QueryWrapper<NowPersonRank> wrapper2 = new QueryWrapper<>();
        for (NowPerson nowPerson : nowPersonList) {
            wrapper2.clear();
            wrapper2.eq("personid", nowPerson.getId());
            wrapper2.eq("shunxu", nowPerson.getRanknum());
            wrapper2.in("rankid", ranksId);
            if (nowPersonRankService.getOne(wrapper2) != null)
                result.add(nowPersonService.getById(nowPerson.getId()));
        }

        return result;
    }

    /*
     * 根据职级名字查找人员列表，历史职级符合
     *
     * @param ranksname 为职级集，用 - 连接
     */
    public List<NowPerson> searchPersonByRank(String ranksname) {
        List<String> ranks = new ArrayList<>();
        ranks = Arrays.asList(ranksname.split("-"));
        // 根据 rankname 查找 rankid
        List<Long> rankIdList = new ArrayList<>();
        for (int i = 0; i < ranks.size(); i++) {
            QueryWrapper<NowRank> wrapper = new QueryWrapper();
            wrapper.eq("name", ranks.get(i));
            NowRank rank = nowRankService.getOne(wrapper);
            rankIdList.add(rank.getId());
        }

        // 根据 rankId 查找有该职级的人员 id
        HashSet<String> nowPersonidlist = new HashSet<>();
        for (int i = 0; i < rankIdList.size(); i++) {
            QueryWrapper<NowPersonRank> wrapper = new QueryWrapper();

            wrapper.eq("rankid", rankIdList.get(i));
            List<NowPersonRank> nowPersonRanks = nowPersonRankService.list(wrapper);
            for (NowPersonRank nowPersonRank : nowPersonRanks) {
                nowPersonidlist.add(nowPersonRank.getPersonid());
            }
        }

        // 根据人员id 查找人员
        List<NowPerson> nowPeoples = new ArrayList<>();
        if (nowPersonidlist.size() != 0) {
            nowPeoples = nowPersonService.listByIds(nowPersonidlist);
        }
        return nowPeoples;
    }

    /**
     * 员工删除
     */
    @GetMapping("/nowperson/delete/{id}")
    public Result delete(@PathVariable(name = "id") String id) {
        boolean result = nowPersonService.removeById(id);// 据库中为Long id
        if (result)
            return Result.success("成功删除本人员");
        return Result.fail("人员不存在");
    }

    /**
     * 搜索公司员工
     *
     * @param terms
     */
    @GetMapping("/nowperson/searchMult/{terms}")
    public Result searchMult(@PathVariable(name = "terms") String terms) {
        System.out.println(terms);
        // 什么条件都没选择
        if (terms == null || terms.equals("|"))
            return Result.success(200, "成功加载人员", nowPersonService.list());
        // '|' 是特殊字符，需要转义
        List<String> termList = Arrays.asList(StringUtils.split(terms, "\\|"));

        switch (termList.size()) {
            case 1:
                if (terms.startsWith("|")) {
                    // 没有职级选择
                    List<NowPerson> nowPersonList = nowPersonService.list();
                    String pattern = ".*" + termList.get(0) + ".*";
                    nowPersonList.removeIf(nowPerson -> !Pattern.matches(pattern, nowPerson.toString()));
                    return Result.success("成功查找员工", nowPersonList);
                } else {
                    // 没有输入搜索内容
                    return Result.success("成功查找人员", searchPersonByLastRank(termList.get(0)));
                }
            case 2:
                // 关键字和搜索内容都有
                List<NowPerson> nowPersonList = searchPersonByLastRank(termList.get(0));
                String pattern = ".*" + termList.get(1) + ".*";
                nowPersonList.removeIf(nowPerson -> !Pattern.matches(pattern, nowPerson.toString()));
                return Result.success("成功查找员工", nowPersonList);
        }
        return Result.success("搜索成功", nowPersonService.list());
    }
}
