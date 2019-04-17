package com.bdqn.service.Impl;

import com.bdqn.dao.userDao;
import com.bdqn.entity.User;
import com.bdqn.service.userService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class userServiceImpl implements userService {
    @Resource
    private userDao ud;

    public userDao getUd() {
        return ud;
    }

    public void setUd(userDao ud) {
        this.ud = ud;
    }


    @Override
    public User getLogin(String userCode, String userPassword) {
        return ud.getLogin(userCode,userPassword);
    }

    @Override
    public int selectUserCount(String queryname, Integer queryUserRole) {
        return ud.selectUserCount(queryname,queryUserRole);
    }

    @Override
    public List<User> selectAllUser(String queryname, Integer queryUserRole, Integer pageIndex, Integer pageSize) {
        return ud.selectAllUser(queryname,queryUserRole,pageIndex,pageSize);
    }

    @Override
    public int addUser(User user) {
        return ud.addUser(user);
    }

    @Override
    public User selectAllUserById(int id) {
        return ud.selectAllUserById(id);
    }

    @Override
    public int updateUser(User user) {
        return ud.updateUser(user);
    }

    @Override
    public int deleteUser(int id) {
        return ud.deleteUser(id);
    }
}
