package com.bdqn.service;

import com.bdqn.entity.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface billService {
    public List<Bill> getBillList(@Param("productName")String productName,
                                  @Param("queryProviderId") Integer queryProviderId,
                                  @Param("queryIsPayment") Integer queryIsPayment,
                                  @Param("pageIndex")Integer pageIndex,
                                  @Param("pageSize")Integer pageSize);
    public int getBillCount(@Param("productName")String productName,
                            @Param("queryProviderId") Integer queryProviderId,
                            @Param("queryIsPayment") Integer queryIsPayment,
                            @Param("pageIndex")Integer pageIndex,
                            @Param("pageSize")Integer pageSize);
    public Bill selectBillById(int id);
    public int updateBill(Bill bill);
    public int addBill(Bill bill);
    public int deleteBill(int id);
    public Bill getBillById(int id);

}
