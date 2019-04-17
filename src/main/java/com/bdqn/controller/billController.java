package com.bdqn.controller;

import com.alibaba.fastjson.JSONArray;
import com.bdqn.entity.Bill;
import com.bdqn.entity.Provider;
import com.bdqn.entity.User;
import com.bdqn.service.billService;
import com.bdqn.service.providerService;
import com.bdqn.util.BaseController;
import com.bdqn.util.Constants;
import com.bdqn.util.PageSupport;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/bill")
public class billController extends BaseController {
    @Resource
    private providerService ps;
   @Resource
    private billService bs;
    private Logger logger=Logger.getLogger(billController.class);

    @RequestMapping("/bill.do")
    public String billlimit(Model model, @RequestParam (value = "queryProductName",required = false)String queryProductName,
                            @RequestParam (value = "queryProviderId",required = false)String queryProviderId,
                            @RequestParam (value = "queryIsPayment",required = false)String  queryIsPayment,
                            @RequestParam(value = "pageIndex",required = false)String pageIndex){
        logger.debug("进入账单页面");
        if(queryProductName==null){
            queryProductName="";
        }
        int _queryProviderId=0;
        if(queryProviderId!=null && !queryProviderId.equals("")){
            _queryProviderId=Integer.parseInt(queryProviderId);
        }
        int _queryIsPayment=0;
        if(queryIsPayment!=null && !queryIsPayment.equals("")){
            _queryIsPayment=Integer.parseInt(queryIsPayment);
        }
        int pageSize= Constants.pageSize;
        int currentPageNo=1;
        if(pageIndex!=null){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        int totalCount=bs.getBillCount(queryProductName,_queryProviderId,_queryIsPayment,(currentPageNo-1)*pageSize,pageSize);
        if(currentPageNo<1){
            currentPageNo=1;
        }else if(currentPageNo>totalCount && totalCount!=0){
            currentPageNo=totalCount;
        }
        List<Bill> billList=null;
        billList=bs.getBillList(queryProductName,_queryProviderId,_queryIsPayment,(currentPageNo-1)*pageSize,pageSize);
                model.addAttribute("billList",billList);
               List<Provider> providerList=ps.selectAllProvider();
               model.addAttribute("providerList",providerList);
        PageSupport pageSupport=new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setCurrentPageNo(currentPageNo);
                pageSupport.setTotalCount(totalCount);
        model.addAttribute("queryProductName",queryProductName);
        model.addAttribute("totalPageCount",pageSupport.getTotalPageCount());
        model.addAttribute("totalCount",pageSupport.getTotalCount());
        model.addAttribute("currentPageNo",pageSupport.getCurrentPageNo());
        model.addAttribute("queryProviderId",_queryProviderId);
        model.addAttribute("queryIsPayment",_queryIsPayment);
       return "billlist";
    }
    @RequestMapping("/billmodify.html")
    public String billmodify(@RequestParam("billid") String billid,Model model) {
        logger.debug("进入账单修改页面");

        if(billid!=null && !billid.equals("")){
            int _id=Integer.parseInt(billid);
            Bill bill=bs.selectBillById(_id);
            model.addAttribute("bill",bill);
            List<Provider>providerList=ps.selectAllProvider();
            return "billmodify";
        }else{
            return "redirect:/bill/bill.do";
        }
    }

    @RequestMapping(value = "/provider",method = RequestMethod.GET,produces =
            {"application/json;charset=UTF-8"})
    @ResponseBody
    public Object providerSelect(){
        List<Provider>providerList=ps.selectAllProvider();
        return JSONArray.toJSONString(providerList);
    }
    @RequestMapping("/updateBill")
    public String update(Bill bill, HttpSession session){
        bill.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        int count=bs.updateBill(bill);
        if(count>0){
            return "redirect:/bill/bill.do";
        }
        return "redirect:/bill/bill.do";
    }
    @RequestMapping("/add.html")
    public String add(@ModelAttribute("bill")Bill bill){
        logger.debug("进入添加账单页面");
        return "billadd";
    }
@RequestMapping("/addsave.html")
    public String addBill(Bill bill,HttpSession session){
            bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            bill.setCreationDate(new Date());
            int count=bs.addBill(bill);
            if(count>0){
                return "redirect:/bill/bill.do";
            }else{
                return "redirect:/bill/add.html";
            }


        }
        @RequestMapping(value = "/deleteBill",method = RequestMethod.GET,produces =
                {"application/json;charset=UTF-8"})
        @ResponseBody
    public Object deleteBill(@RequestParam String billid){
            HashMap<String,String>resultMap=new HashMap<>();
            if(StringUtils.isNullOrEmpty(billid)){
                resultMap.put("delResult","notexist");
            }else{
                int _billid=Integer.parseInt(billid);
                int count=bs.deleteBill(_billid);
                if(count>0){
                    resultMap.put("delResult","true");
                }else{
                    resultMap.put("delResult","false");
                }
            }
            return JSONArray.toJSONString(resultMap);

        }
        @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
      public String view(@PathVariable String id,Model model){
                  int uid=Integer.parseInt(id);
                  Bill bill=bs.getBillById(uid);
                  model.addAttribute(bill);
                  return "billview";
        }

    }

