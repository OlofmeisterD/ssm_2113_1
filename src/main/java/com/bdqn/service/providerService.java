package com.bdqn.service;

import com.bdqn.entity.Provider;

import java.util.List;

public interface providerService {
    public List<Provider> selectAllProvider();
    public List<Provider> selectAllProviderLimit(String proCode,String proName,Integer PageIndex,Integer PageSize);
    public int getProviderCount(String proCode,String proName);
}
