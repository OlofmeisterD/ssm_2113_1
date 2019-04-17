package com.bdqn.dao;

import com.bdqn.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface userDao {
    public User getLogin(@Param("userCode") String userCode, @Param("userPassword") String userPassword);
    public int selectUserCount(@Param("queryname") String queryname, @Param("queryUserRole") Integer queryUserRole);
    public List<User> selectAllUser(@Param("queryname") String queryname, @Param("queryUserRole") Integer queryUserRole, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);
    public int addUser(User user);
    public User selectAllUserById(int id);
    public int updateUser(User user);
    public int deleteUser(int id);
}
