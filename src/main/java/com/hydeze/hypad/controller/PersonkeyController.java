package com.hydeze.hypad.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hydeze.hypad.entity.PadKey;
import com.hydeze.hypad.entity.PadPerson;
import com.hydeze.hypad.entity.Personkey;
import com.hydeze.hypad.service.IPadKeyService;
import com.hydeze.hypad.service.IPersonkeyService;
import com.hydeze.hypad.utils.Result;
import org.springframework.beans.BeanUtils;
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
public class PersonkeyController {

    @Autowired
    IPersonkeyService personkeyService;

    @Autowired
    IPadKeyService padKeyService;

    /**
     * 人员关键字编辑（修改或新建)
     *
     * @param personkey 人员ID-关键字-关键字...
     */
    @GetMapping("/personkey/edit/{personkey}")
    public Result edit(@PathVariable(name = "personkey") String personkey) {
        List<String> keyList = Arrays.asList(personkey.split("-"));
        long personid = Long.parseLong(keyList.get(0));
        for (int i = 1; i < keyList.size(); i++) {
            Personkey temp = new Personkey();
            temp.setPersonid(personid);

            // 根据 keyname 查找 keyId
            QueryWrapper<PadKey> wrapper = new QueryWrapper();
            wrapper.eq("keyname", keyList.get(i));
            PadKey key = padKeyService.getOne(wrapper);

            // 判断记录是否已存在,若存在，将 ID 复制
            QueryWrapper<Personkey> personkeyWrapper = new QueryWrapper<>();
            personkeyWrapper.eq("personid", personid);
            personkeyWrapper.eq("keyid", key.getId());
            personkeyWrapper.last("LIMIT 1");
            Personkey one = personkeyService.getOne(personkeyWrapper);

            if (one != null)
                temp.setId(one.getId());
            temp.setKeyid(key.getId());
            temp.setGmtCreate(LocalDateTime.now());
            temp.setGmtModified(LocalDateTime.now());
            temp.setIsDelete(0);
            personkeyService.saveOrUpdate(temp);
        }

        return Result.success("OK");
    }

}
