package com.hydeze.hypad.service.impl;

import com.hydeze.hypad.entity.Admin;
import com.hydeze.hypad.mapper.AdminMapper;
import com.hydeze.hypad.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ma
 * @since 2020-08-25
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
