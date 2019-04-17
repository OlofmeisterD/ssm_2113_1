package com.bdqn.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bdqn.entity.Role;
import com.bdqn.entity.User;
import com.bdqn.service.roleService;
import com.bdqn.service.userService;
import com.bdqn.util.BaseController;
import com.bdqn.util.Constants;
import com.bdqn.util.PageSupport;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/user")
public class userController extends BaseController {
    @Resource
    private userService us;
    @Resource
    private roleService rs;
    private Logger logger = Logger.getLogger(userController.class);

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String login() {
        logger.debug("进入登录页面");
        return "login";
    }
    @RequestMapping(value = "dologin.html", method = RequestMethod.POST)
    public String doLogin(@RequestParam String userCode, @RequestParam String userPassword
            , HttpServletRequest req, HttpSession session) {
        User user = us.getLogin(userCode, userPassword);
        if (user == null) {
            req.setAttribute("error", "用户名或密码错误");
            return "login";
        } else {
            session.setAttribute(Constants.USER_SESSION, user);
            return "frame";
        }

    }
    @RequestMapping("/user.do")
      public String user(Model model, @RequestParam(value = "queryname",required = false)String queryname,
                         @RequestParam(value = "queryUserRole",required = false)String queryUserRole,
                         @RequestParam(value = "pageIndex",required = false)String pageIndex){

            if(queryname==null){
                queryname="";
            }
            int _queryUserRole=0;
            if(queryUserRole!=null &&queryUserRole!=""){
                try {
                    _queryUserRole=Integer.parseInt(queryUserRole);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            int pagesize= Constants.pageSize;
            int currentPageNo=1;
            if(pageIndex!=null ){
                try {
                    currentPageNo=Integer.valueOf(pageIndex);
                } catch (NumberFormatException e) {
                    return "error";
                }
            }
                int totalCount=us.selectUserCount(queryname,_queryUserRole);
                if(currentPageNo<1){
                    currentPageNo=1;
                }else if(currentPageNo>totalCount && totalCount!=0){
                    currentPageNo=totalCount;
                }
                List<User>userlist=null;
        System.out.println("========================="+currentPageNo);
                userlist=us.selectAllUser(queryname,_queryUserRole,(currentPageNo-1)*pagesize,pagesize);
                model.addAttribute("userList",userlist);
                List<Role>rolelist=null;
                rolelist=rs.selectAllRole();
                model.addAttribute("roleList",rolelist);
                PageSupport pageSupport=new PageSupport();
                pageSupport.setPageSize(pagesize);
                pageSupport.setCurrentPageNo(currentPageNo);
                pageSupport.setTotalCount(totalCount);
                model.addAttribute("queryUserName",queryname);
                model.addAttribute("totalPageCount",pageSupport.getTotalPageCount());
                model.addAttribute("totalCount",pageSupport.getTotalCount());
                model.addAttribute("currentPageNo",pageSupport.getCurrentPageNo());
                model.addAttribute("queryUserRole",_queryUserRole);
                return "userlist";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute(Constants.USER_SESSION);
        return "redirect:/user/login.html";
    }
    @RequestMapping(value = "/useradd",method = RequestMethod.GET)
    public String add(@ModelAttribute("user")User user){
        logger.debug("进入添加页面");
        return "useradd";
    }
    @RequestMapping(value="/useraddsave.html",method=RequestMethod.POST)
    public String addUserSave(User user,HttpSession session,HttpServletRequest request,
                              @RequestParam(value ="attachs", required = false) MultipartFile[] attachs){
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");
        logger.info("uploadFile path ============== > "+path);
        for(int i = 0;i < attachs.length ;i++){
            MultipartFile attach = attachs[i];
            if(!attach.isEmpty()){
                if(i == 0){
                    errorInfo = "uploadFileError";
                }else if(i == 1){
                    errorInfo = "uploadWpError";
                }
                String oldFileName = attach.getOriginalFilename();//原文件名
                logger.info("uploadFile oldFileName ============== > "+oldFileName);
                String prefix= FilenameUtils.getExtension(oldFileName);//原文件后缀
                logger.debug("uploadFile prefix============> " + prefix);
                int filesize = 500000;
                logger.debug("uploadFile size============> " + attach.getSize());
                if(attach.getSize() >  filesize){//上传大小不得超过 500k
                    request.setAttribute("e", " * 上传大小不得超过 500k");
                    flag = false;
                }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                    String fileName = System.currentTimeMillis()+ RandomUtils.nextInt(1000000)+"_Personal.jpg";
                    logger.debug("new fileName======== " + attach.getName());
                    File targetFile = new File(path, fileName);
                    if(!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    //保存
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                    }
                    if(i == 0){
                        idPicPath = path+File.separator+fileName;
                    }else if(i == 1){
                        workPicPath = path+File.separator+fileName;
                    }
                    logger.debug("idPicPath: " + idPicPath);
                    logger.debug("workPicPath: " + workPicPath);

                }else{
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        if(flag){
            user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            user.setCreationDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            try {
                if(us.addUser(user)>0){
                    return "redirect:/user/user.do";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "useradd";
    }

    @RequestMapping(value = "/usermodify.html",method = RequestMethod.GET)
    public String usermodify(@RequestParam (value ="uid",required = true)String uid,Model model){
        if(!uid.equals("")&&uid!=null){
            int userid=Integer.parseInt(uid);
            User userList=us.selectAllUserById(userid);
            model.addAttribute("user",userList);
        }else{
            return "redirect:/user/user.do";
        }
        return "usermodify";
    }
    @RequestMapping(value = "usermodifysave.html",method = RequestMethod.POST)
    public String updateUser( User user,HttpSession session){
        user.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        int count=us.updateUser(user);
        System.out.println(count);
        if(count>0){
            return "redirect:/user/user.do";
        }else{
            return "usermodify";
        }

    }
    @RequestMapping(value = "/delete.html")
    @ResponseBody
    public Object userdelete(@RequestParam String userid){
        System.out.println("======================================");
        System.out.println("======================================");
        System.out.println("======================================");
        HashMap<String,String> resultMap=new HashMap<>();
        if(StringUtils.isNullOrEmpty(userid)){
            resultMap.put("delResult","notexist");
        }else{
            int _id=Integer.parseInt(userid);
            int count=us.deleteUser(_id);
            if(count<=0){
                resultMap.put("delResult","false");
            }else{
                resultMap.put("delResult","true");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }
    @RequestMapping(value = "/view",method = RequestMethod.GET,produces =
            {"application/json;charset=UTF-8"})
    @ResponseBody
    public Object view(@RequestParam String id){
        logger.debug("view id============="+id);
        String cjson="";
        if(null==id||"".equals(id)){
            return "nodata";
        }else{
            try {
                int _id=Integer.parseInt(id);
                User user=us.selectAllUserById(_id);
                cjson= JSON.toJSONString(user);
                logger.debug("cjson========="+cjson);
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
            return cjson;

        }
    }





}
