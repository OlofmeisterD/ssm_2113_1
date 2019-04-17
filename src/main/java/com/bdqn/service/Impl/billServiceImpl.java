package com.bdqn.service.Impl;

import com.bdqn.dao.billDao;
import com.bdqn.entity.Bill;
import com.bdqn.service.billService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class billServiceImpl implements billService {
    @Resource
    private billDao bd;

    public billDao getBd() {
        return bd;
    }

    public void setBd(billDao bd) {
        this.bd = bd;
    }

    @Override
    public List<Bill> getBillList(String productName, Integer queryProviderId, Integer queryIsPayment, Integer pageIndex, Integer pageSize) {
        return bd.getBillList(productName,queryProviderId,queryIsPayment,pageIndex,pageSize);
    }

    @Override
    public int getBillCount(String productName, Integer queryProviderId, Integer queryIsPayment, Integer pageIndex, Integer pageSize) {
        return bd.getBillCount(productName,queryProviderId,queryIsPayment,pageIndex,pageSize);
    }

    @Override
    public Bill selectBillById(int id) {
        return bd.selectBillById(id);
    }

    @Override
    public int updateBill(Bill bill) {
        return bd.updateBill(bill);
    }

    @Override
    public int addBill(Bill bill) {
        return bd.addBill(bill);
    }

    @Override
    public int deleteBill(int id) {
        return bd.deleteBill(id);
    }

    @Override
    public Bill getBillById(int id) {
        return bd.getBillById(id);
    }
}
