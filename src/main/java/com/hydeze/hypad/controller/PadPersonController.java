package com.hydeze.hypad.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hydeze.hypad.entity.PadKey;
import com.hydeze.hypad.entity.PadPerson;
import com.hydeze.hypad.entity.Personkey;
import com.hydeze.hypad.service.IPadKeyService;
import com.hydeze.hypad.service.IPadPersonService;
import com.hydeze.hypad.service.IPersonkeyService;
import com.hydeze.hypad.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
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
 * @since 2020-07-31
 */
@RestController
@RequestMapping("/hypad")
public class PadPersonController {

    @Autowired
    IPadPersonService iPadPersonService;

    @Autowired
    IPadKeyService padKeyService;

    @Autowired
    IPersonkeyService personkeyService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /*
     * 人员列表
     * @param currentPage 当前页
     * @RequestParam(defaultValue = "1") URL占位符绑定到方法参数上
     */
    @GetMapping("/person")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 20);
        IPage pageData = iPadPersonService.page(page, new QueryWrapper<PadPerson>().orderByDesc("gmt_create"));


        return Result.success(200, "成功加载人员", pageData);
    }

    /*
     * 人员详情
     * @param id 根据 id 查询人员
     */
    @GetMapping("/person/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        PadPerson person = iPadPersonService.getById(id);
        Assert.notNull(person, "人员不存在");
        return Result.success(200, "成功读取人员信息", person);
    }

    /**
     * 人员编辑（修改或新建)
     *
     * @param padPerson
     * @Validated 对 padPerson 进行验证
     */
    @PostMapping("/person/edit")
    public Result edit(@RequestBody PadPerson padPerson) {
        PadPerson temp = null;
        // 修改人员信息
        if (padPerson.getId() != 0) {
            temp = iPadPersonService.getById(padPerson.getId());
        } else {
            // 新人员
            temp = new PadPerson();
            temp.setGmtCreate(LocalDateTime.now());
            temp.setIsDelete(0);
        }
        temp.setGmtModified(LocalDateTime.now());

        BeanUtils.copyProperties(padPerson, temp, "id", "isDelete", "gmtCreate", "gmtModified");
        iPadPersonService.saveOrUpdate(temp);

        // 自增主键的实体类插入后会自动 setId
        return Result.success(200, "录入人员信息成功", temp.getId());
    }

    /**
     * 人员删除
     */
    @GetMapping("/person/delete/{id}")
    public Result delete(@PathVariable(name = "id") Long id) {
        boolean result = iPadPersonService.removeById(id);// 据库中为Long id
        System.out.println("#######################删除*******************");
        if (result)
            return Result.success("成功删除本人员");
        return Result.fail("人员不存在");
    }

    /*
     * 人员关键字详情
     * @param personid 根据 personid 查询他的关键字
     * return 返回关键字名字列表
     */
    @GetMapping("/personkey/{personid}")
    public Result personkey(@PathVariable(name = "personid") Long personid) {
        QueryWrapper<Personkey> wrapper = new QueryWrapper();
        wrapper.eq("personid", personid);
        List<Personkey> personkeys = personkeyService.list(wrapper);

        ArrayList<String> keys = new ArrayList<>();
        for (Personkey personkey : personkeys) {
            PadKey padKey = padKeyService.getById(personkey.getKeyid());
            keys.add(padKey.getKeyname());
        }
        for (String key : keys) {
            System.out.println(key);
        }
        return Result.success(200, "OK", keys);
    }

    /*
     * 根据关键字查找人员列表
     *
     * ids为关键字集，用 - 连接
     */
    public List<PadPerson> searchPersonByKey(String ids) {
        List<String> keynameList = new ArrayList<>();
        keynameList = Arrays.asList(ids.split("-"));
        // 根据 keyname 查找 keyid
        List<Long> keyIdList = new ArrayList<>();
        for (int i = 0; i < keynameList.size(); i++) {
            QueryWrapper<PadKey> wrapper = new QueryWrapper();
            wrapper.eq("keyname", keynameList.get(i));
            PadKey padKey = padKeyService.getOne(wrapper);
            keyIdList.add(padKey.getId());
        }

        // 根据 keyid 查找有该关键字的人员id
        HashSet<Long> personidlist = new HashSet<>();
        for (int i = 0; i < keyIdList.size(); i++) {
            QueryWrapper<Personkey> wrapper = new QueryWrapper();

            wrapper.eq("keyid", keyIdList.get(i));
            List<Personkey> personkeys = personkeyService.list(wrapper);
            for (Personkey personkey : personkeys) {
                personidlist.add(personkey.getPersonid());
            }
        }

        // 根据人员id 查找人员
        List<PadPerson> peoples = new ArrayList<>();
        if (personidlist.size() != 0) {
            peoples = iPadPersonService.listByIds(personidlist);
        }
        return peoples;
    }

    /**
     * 根据关键字、姓名查询人员
     *
     * @param terms 用'|'分割.关键字 name,用 - 连接;名字;
     */
    @GetMapping("/searchMult/{terms}")
    public Result searchMult(@PathVariable(name = "terms") String terms) {
        System.out.println(terms);
        // 什么条件都没选择
        if (terms == null || terms.equals("||"))
            return Result.success(200, "成功加载人员", iPadPersonService.list());

        // '|' 是特殊字符，需要转义
        List<String> termList = Arrays.asList(StringUtils.split(terms, "\\|"));
        // 根据条件列表 size 细分条件组合
        switch (termList.size()) {
            // termlist size为1，即只有一个条件
            case 1:
                // 只有亮点公司
                if (terms.startsWith("||")) {
                    QueryWrapper<PadPerson> wrapper = new QueryWrapper();
                    wrapper.like("lightcomp", terms.substring(2));
                    return Result.success(200, "成功查找人员", iPadPersonService.list(wrapper));
                } else if (terms.endsWith("||")) {
                    // 只有关键字
                    return Result.success("成功查找人员", searchPersonByKey(termList.get(0)));
                } else {
                    // 只有人名
                    QueryWrapper<PadPerson> wrapper = new QueryWrapper();
                    wrapper.like("name", termList.get(0));
                    return Result.success("成功查找人员", iPadPersonService.list(wrapper));
                }
            case 2:
                // 拥有两个条件
                // 没有关键字
                if (terms.startsWith("|")) {
                    QueryWrapper<PadPerson> wrapper = new QueryWrapper();
                    wrapper.like("name", termList.get(0));
                    wrapper.like("lightcomp", termList.get(1));
                    return Result.success("成功查找人员", iPadPersonService.list(wrapper));
                } else if (terms.endsWith("|")) {
                    // 没有亮点公司
                    List<PadPerson> personList = searchPersonByKey(termList.get(0));
                    String pattern = ".*" + termList.get(1) + ".*";
                    personList.removeIf(padPerson -> !Pattern.matches(pattern, padPerson.getName()));
                    return Result.success("成功查找人员", personList);
                } else {
                    // 没有人名
                    List<PadPerson> personList = searchPersonByKey(termList.get(0));
                    String pattern = ".*" + termList.get(1) + ".*";
                    personList.removeIf(padPerson -> !Pattern.matches(pattern, padPerson.getLightcomp()));
                    return Result.success("成功查找人员", personList);
                }
        }
        // 关键字、人名、亮点公司都有
        List<PadPerson> personList = searchPersonByKey(termList.get(0));
        String pattern = ".*" + termList.get(1) + ".*";
        personList.removeIf(padPerson -> !Pattern.matches(pattern, padPerson.getName()));
        String pattern2 = ".*" + termList.get(2) + ".*";
        personList.removeIf(padPerson -> !Pattern.matches(pattern2, padPerson.getLightcomp()));
        return Result.success("成功查找人员", personList);
    }
}
