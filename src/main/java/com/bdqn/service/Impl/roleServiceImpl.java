package com.bdqn.service.Impl;

import com.bdqn.dao.roleDao;
import com.bdqn.entity.Role;
import com.bdqn.service.roleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("roleService")
public class roleServiceImpl implements roleService {
    @Resource
    private roleDao rd;

    public roleDao getRd() {
        return rd;
    }

    public void setRd(roleDao rd) {
        this.rd = rd;
    }

    @Override
    public List<Role> selectAllRole() {
        return rd.selectAllRole();
    }
}
