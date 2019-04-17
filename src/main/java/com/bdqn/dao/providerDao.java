package com.bdqn.dao;

import com.bdqn.entity.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface providerDao {
    public List<Provider> selectAllProvider();
    public List<Provider> selectAllProviderLimit(@Param("proCode")String proCode,@Param("proName")String proName,@Param("PageIndex")Integer PageIndex,@Param("PageSize")Integer PageSize);
    public int getProviderCount(@Param("proCode")String proCode,@Param("proName")String proName);
}
