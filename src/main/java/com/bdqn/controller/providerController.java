package com.bdqn.controller;

import com.bdqn.entity.Provider;
import com.bdqn.service.providerService;
import com.bdqn.util.BaseController;
import com.bdqn.util.Constants;
import com.bdqn.util.PageSupport;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/provider")
public class providerController extends BaseController {
    @Resource
    private providerService ps;
    private Logger logger=Logger.getLogger(providerController.class);
    @RequestMapping("/provider.do")
    public String providerList(@RequestParam (value = "queryProCode",required = false)String queryProCode,
                               @RequestParam (value="queryProName",required = false)String queryProName,
                               @RequestParam(value = "pageIndex",required = false)String pageIndex,
                               Model model){
        if(queryProCode==null){
            queryProCode="";
        }
        if(queryProName==null){
            queryProName="";
        }
        int pageSize= Constants.pageSize;
        int currPageNo=1;
        if(pageIndex!=null && !pageIndex.equals("")){
            currPageNo=Integer.parseInt(pageIndex);
        }
        int totalCount=ps.getProviderCount(queryProCode,queryProName);
        if(currPageNo<1){
            currPageNo=1;
        }else if(currPageNo>totalCount && totalCount!=0){
            currPageNo=totalCount;
        }
        List<Provider>providerList=ps.selectAllProviderLimit(queryProCode,queryProName,(currPageNo-1)*pageSize,pageSize);
                model.addAttribute("providerList",providerList);
        PageSupport pageSupport=new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setCurrentPageNo(currPageNo);
        pageSupport.setTotalCount(totalCount);
        model.addAttribute("queryProCode",queryProCode);
        model.addAttribute("queryProName",queryProName);
        model.addAttribute("totalPageCount",pageSupport.getTotalPageCount());
        model.addAttribute("totalCount",pageSupport.getTotalCount());
        model.addAttribute("currentPageNo",pageSupport.getCurrentPageNo());
        return "providerlist";
    }
}
