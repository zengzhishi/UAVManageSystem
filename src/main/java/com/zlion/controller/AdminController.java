package com.zlion.controller;

import com.zlion.model.User;
import com.zlion.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zzs on 2016/9/1.
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /*
    管理员的主界面
     */
    @RequestMapping(value = "/home")
    public String adminHome(Map<String,Object> map){
        map.put("adminName","123");
        return "/admin/home";
    }

    /*
    管理员登陆login
    登陆验证
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(HttpSession session,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception{

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.print(">>>>>>username: "+username+"  password: "+password);

        Result jsonRender = new Result();
        if(!adminService.loginValidate(session, username, password)){
            jsonRender.put("Msg", "Wrong username or password");
            jsonRender.put("Code", 103);
        }

        return jsonRender;
    }

    /*
    管理员登出logout
     */
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public Result logout(HttpSession session){
        Result jsonRender = new Result();
        if (session.getAttribute("admin") == null){
            jsonRender.put("Code", 101);
            jsonRender.put("Msg", "Need Auth");
        }
        session.removeAttribute("admin");
        session.removeAttribute("adminId");
        return jsonRender;
    }

    /*
    查询所有的普通用户
     */
    @ResponseBody
    @RequestMapping(value = "/show/userList", method = RequestMethod.GET)
    public Result getUsers(){

        Result jsonRender = new Result();
        List<User> userList = adminService.getUserList();
        jsonRender = jsonRender.okForList();
        jsonRender.put("Data", userList);

        return jsonRender;
    }

    /*
    使用用户的id进行查询用户的数据
     */
    @ResponseBody
    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public Result showDetailOfUser(@PathVariable("id") Long userId){

        User user = adminService.getUserByUserId(userId);

        Result jsonRender = new Result();
        jsonRender.put("Data",user);

        return jsonRender;
    }

    /*
    根据用户的名字查看用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/showUserByName/{userName}", method = RequestMethod.GET)
    public Result showDetailOfUserByUserName(@PathVariable("userName") String userName){

        User user = adminService.getUserByUsername(userName);

        Result jsonRender = new Result();
        if(user != null){
            jsonRender.put("Data",user);
        }
        else{
            jsonRender.put("Msg", "Can not fount "+userName);
            jsonRender.put("Code", 104);
        }

        return jsonRender;
    }

    /*
    管理员直接添加新用户
     */
    @ResponseBody
    @RequestMapping(value = "/save/auth", method = RequestMethod.PATCH)
    public Result saveAuth(HttpServletRequest request){

        Result jsonRender = new Result();

        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        if (user.getUsername()==null && user.getPassword()==null){
            jsonRender.put("Msg", "Illegal Arguments");
            jsonRender.put("Code", 102);
        }
        else{
            if (adminService.checkUserExist(user.getUsername())){
                jsonRender.put("Code", 103);
                jsonRender.put("Msg", "Repeating User Name");
            }
            else{
                user.setEmail(request.getParameter("email"));
                user.setRegistDate(new Date());

                try{
                    adminService.saveUser(user);
                }catch (Exception e){
                    jsonRender.put("Msg", e.getStackTrace());
                    jsonRender.put("Code", 103);
                }
            }
        }
        return jsonRender;
    }


    /**
    删除用户数据
    主要是使用在有不正当申请的用户时
     **/
    @ResponseBody
    @RequestMapping(value = "/delete/auth", method = RequestMethod.DELETE)
    public Result deleteAuth(HttpServletRequest request){
        Result jsonRender = new Result();

        Long id = Long.parseLong(request.getParameter("id"));

        if (adminService.checkUserExistById(id))
        {
            adminService.deleteUser(id);
            jsonRender.put("Msg","Delete success");
        }
        else
        {
            jsonRender.put("Msg","Delete failed");
            jsonRender.put("Code",101);
        }
        return jsonRender;
    }

    /*
    更新用户密码
    需要通过参入参数来实现，和post方法通过form表单传递不一样
     */
    @ResponseBody
    @RequestMapping(value = "/updatePwd/auth", method = RequestMethod.PUT)
    public Result updateAuth(HttpServletRequest request,
                             HttpServletResponse response) throws Exception{
        Result jsonRender = new Result();

        Long id = 1L;
        try{
            id = Long.parseLong(request.getParameter("id"));
        }catch (Exception e){
            e.printStackTrace();
        }
        String newPassword = request.getParameter("password");
//        System.out.println(">>>>>"+id+"   password:"+newPassword);

        adminService.updateUserPwd(id, newPassword);
        return jsonRender;
    }


    /**
    可能需要做一个用于存储验证信息的功能
     **/
    @ResponseBody
    @RequestMapping(value = "/confirm/auth", method = RequestMethod.PUT)
    public Result confirmAuth(HttpServletRequest request, HttpSession session){
        Result jsonRender = new Result();

        String userId = request.getParameter("userId");

        return jsonRender;
    }



}
