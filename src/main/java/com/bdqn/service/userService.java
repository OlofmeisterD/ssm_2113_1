package com.bdqn.service;

import com.bdqn.entity.User;

import java.util.List;

public interface userService {
    public User getLogin(String userCode, String userPassword);
    public int selectUserCount(String queryname, Integer queryUserRole);
    public List<User> selectAllUser(String queryname, Integer queryUserRole, Integer pageIndex, Integer pageSize);
    public int addUser(User user);
    public User selectAllUserById(int id);
    public int updateUser(User user);
    public int deleteUser(int id);

}
