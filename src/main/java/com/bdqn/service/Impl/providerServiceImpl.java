package com.bdqn.service.Impl;

import com.bdqn.dao.providerDao;
import com.bdqn.entity.Provider;
import com.bdqn.service.providerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class providerServiceImpl implements providerService {
    @Resource
    private providerDao pd;

    public providerDao getPd() {
        return pd;
    }

    public void setPd(providerDao pd) {
        this.pd = pd;
    }

    @Override
    public List<Provider> selectAllProvider() {
        return pd.selectAllProvider();
    }

    @Override
    public List<Provider> selectAllProviderLimit(String proCode, String proName, Integer PageIndex, Integer PageSize) {
        return pd.selectAllProviderLimit(proCode,proName,PageIndex,PageSize);
    }

    @Override
    public int getProviderCount(String proCode, String proName) {
        return pd.getProviderCount(proCode,proName);
    }
}
