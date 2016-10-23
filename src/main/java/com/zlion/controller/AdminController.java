package com.zlion.controller;

import com.zlion.model.BlockApplication;
import com.zlion.model.User;
import com.zlion.service.AdminService;
import com.zlion.service.UavService;
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
    private UavService uavService;

    @Autowired
    public AdminController(AdminService adminService, UavService uavService) {
        this.adminService = adminService;
        this.uavService = uavService;
    }

    /*
    管理员的主界面
     */
    @RequestMapping(value = "/home")
    public String adminHome(Map<String,Object> map){
        map.put("adminName", "123");
        return "/admin/home";
    }

    /*
    管理员登陆login
    登陆验证
     */
    /**
     * @api {post} /admin/login Admin login system
     * @apiName Admin login
     * @apiGroup Admin
     * @apiVersion 0.1.0
     *
     * @apiParam {String} username Username of the admin.
     * @apiParam {String} password  Password of the admin.
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *     }
     *
     * @apiError UserOrPassError The Username or password error.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 103 LoginError
     *     {
     *       "Msg": "User or Password Error",
     *       "Code": 103
     *     }
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
//        jsonRender.put("header", response.getHeader("Access-Control-Allow-Origin"));

        return jsonRender;
    }

    /*
    管理员登出logout
     */
    /**
     * @api {delete} /admin/logout Admin logout from system
     * @apiName Admin user logout
     * @apiGroup Admin
     * @apiVersion 0.1.0
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *     }
     *
     * @apiError NeedAuth No user login.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 101 NeedAuth
     *     {
     *       "Msg": "User Not Login",
     *       "Code": 101
     *     }
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
    /**
     * @api {get} /admin/show/users Admin show user list
     * @apiName Admin show users
     * @apiGroup Admin
     * @apiVersion 0.1.0
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     * @apiSource {[User]} Data of user list
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 200,
     *       "Msg": "ok"
     *       "Data": [{
     *           "usernmae", "password", "phone", "address", "email", "groupName", "registeDate", "confirm"
     *       },{...},...
     *       ]
     *     }
     *     #其中registeDate是存储为时间戳格式，需要做格式化的输出
     *
     */
    @ResponseBody
    @RequestMapping(value = "/show/users", method = RequestMethod.GET)
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
    /**
     * @api {get} /admin/show/{id} Admin show user detail info(id 为用户的id)
     * @apiName Admin show user detail
     * @apiGroup Admin
     * @apiVersion 0.2.1
     *
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     * @apiSource {Data} Data of user
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *       "Data": {
     *           "usernmae", "password", "phone", "address", "email", "groupName", "registeDate", "confirm"
     *       }
     *     }
     *     #其中registeDate是存储为时间戳格式，需要做格式化的输出
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
    /**
     * @api {get} /admin/showUserByName/{username} Admin find user by username(username 是用户名)
     * @apiName Admin find user by username
     * @apiGroup Admin
     * @apiVersion 0.1.0
     *
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     * @apiSource {Data} Data of user
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *       "Data": {
     *           "usernmae", "password", "phone", "address", "email", "groupName", "registeDate", "confirm"
     *       }
     *     }
     *     #其中registeDate是存储为时间戳格式，需要做格式化的输出
     *
     * @apiError UserNotFoundError The user named username is not found.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 104 UserNotFoundError
     *     {
     *       "Msg": "Can not fount {username}",
     *       "Code": 104
     *     }
     */
    @ResponseBody
    @RequestMapping(value = "/showUserByName/{username}", method = RequestMethod.GET)
    public Result showDetailOfUserByUserName(@PathVariable("username") String username){

        User user = adminService.getUserByUsername(username);

        Result jsonRender = new Result();
        if(user != null){
            jsonRender.put("Data",user);
        }
        else{
            jsonRender.put("Msg", "Can not fount "+username);
            jsonRender.put("Code", 104);
        }

        return jsonRender;
    }

    /*
    管理员直接添加新用户
     */
    /**
     * @api {post} /admin/add/auth Admin add new user
     * @apiName Admin add auth
     * @apiGroup Admin
     * @apiVersion 0.1.0
     *
     * @apiParam {String} username Username of the auth.
     * @apiParam {String} password  Password of the auth.
     * @apiParam {String} email  Email of the auth.(Not necessary)
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *     }
     *
     * @apiError UserOrPassError The Username or password error.
     * @apiError InsertDataError Can'n save auth data.
     * @apiError RepeatingError The User name repeated.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 102 UserOrPassError
     *     {
     *       "Msg": "Illegal Arguments",
     *       "Code": 102
     *     }
     *
     *     HTTP/1.1 103 InsertDataError
     *     {
     *       "Msg": 看具体情况,
     *       "Code": 103
     *     }
     *
     *     HTTP/1.1 104 RepeatingError
     *     {
     *       "Msg": "Repeating User Name",
     *       "Code": 104
     *     }
     *
     */
    @ResponseBody
    @RequestMapping(value = "/add/auth", method = RequestMethod.POST)
    public Result saveAuth(HttpServletRequest request){
        Result jsonRender = new Result();

        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        if (user.getUsername()==null || user.getPassword()==null){
            jsonRender.put("Msg", "Illegal Arguments");
            jsonRender.put("Code", 102);
        }
        else{
            if (adminService.checkUserExist(user.getUsername())){
                jsonRender.put("Code", 104);
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


    /*
    删除用户数据
    主要是使用在有不正当申请的用户时
     */
    /**
     * @api {delete} /admin/delete/auth Admin delete user
     * @apiName Admin delete auth
     * @apiGroup Admin
     * @apiVersion 0.1.0
     *
     * @apiParam {Long} id Id of the auth.
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *     }
     *
     * @apiError AuthNotExistError User is not exist.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 101 AuthNotExistError
     *     {
     *       "Msg": "Auth Not Exist!",
     *       "Code": 101
     *     }
     *
     */
    @ResponseBody
    @RequestMapping(value = "/delete/auth", method = RequestMethod.DELETE)
    public Result deleteAuth(HttpServletRequest request){
        Result jsonRender = new Result();

        Long id = Long.parseLong(request.getParameter("id"));

        if (adminService.checkUserExistById(id))
        {
            adminService.deleteUser(id);
        }
        else
        {
            jsonRender.put("Msg","Auth Not Exist!");
            jsonRender.put("Code",101);
        }
        return jsonRender;
    }

    /*
    更新用户密码
    需要通过参入参数来实现，和post方法通过form表单传递不一样
     */
    /**
     * @api {put} /admin/updatePwd/auth Admin update password of user
     * @apiName Admin updatePwd for auth
     * @apiGroup Admin
     * @apiVersion 0.1.0
     *
     * @apiParam {Long} id Id of the auth.
     * @apiParam {String} password New password for user.
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *     }
     *
     * @apiError ArgumentsError Illegal Arguments.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 102 ArgumentsError
     *     {
     *       "Msg": "Password Can't be empty",
     *       "Code": 102
     *     }
     *
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
        if (newPassword == null || newPassword.equals("")){
            jsonRender = jsonRender.argError();
            jsonRender.put("Msg", "Password Can't be empty");
        }
        else{
            adminService.updateUserPwd(id, newPassword);
        }
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

    /**
     * @api {post} /admin/add/admin Admin add admin user
     * @apiName Admin add admin
     * @apiGroup Admin
     * @apiVersion 0.2.2
     *
     * @apiParam {String} username Username of the admin.
     * @apiParam {String} password  Password of the admin.
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *     }
     *
     * @apiError IllegalMethodError Add admin user Error.
     * @apiError RepeatedUser Repeating User Name.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 104 IllegalMethodError
     *     {
     *       "Msg": "Illegal Action Parameters",
     *       "Code": 104
     *     }
     *     HTTP/1.1 105 RepeatedUser
     *     {
     *       "Code": 105,
     *       "Msg": "Repeating User Name"
     *     }
     *
     */
    @ResponseBody
    @RequestMapping(value = "/add/admin", method = RequestMethod.POST)
    public Result addAdmin(HttpServletRequest request){
        Result jsonRender = new Result();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (adminService.getAdminByUserId(username) != null){
            jsonRender.put("Code", 105);
            jsonRender.put("Msg", "Repeating User Name");
            return jsonRender;
        }
        try{
            adminService.addAdmin(username, password);
        }catch(Exception e){
            e.printStackTrace();
            jsonRender = jsonRender.illegalMethod();
        }

        return jsonRender;
    }

    /**
     * @api {delete} /admin/delete/admin Admin delete admin user
     * @apiName Admin delete admin
     * @apiGroup Admin
     * @apiVersion 0.1.0
     *
     * @apiParam {String} username Username of the admin.
     * @apiParam {String} password  Password of the admin.
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *     }
     *
     * @apiError IllegalMethodError Add admin user Error.
     * @apiError PassOrUserError Admin user
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 104 IllegalMethodError
     *     {
     *       "Msg": "Admin user can't be empty",
     *       "Code": 104
     *     }
     *
     *     HTTP/1.1 103 PassOrUserError
     *     {
     *       "Msg": 多种情况,
     *       "Code": 103
     *     }
     *
     */
    @ResponseBody
    @RequestMapping(value = "/delete/admin", method = RequestMethod.DELETE)
    public Result deleteAdmin(HttpServletRequest request){
        Result jsonRender = new Result();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (adminService.checkDeleteAccess()){
            jsonRender.illegalMethod();
            jsonRender.put("Msg", "Admin user can't be empty");
        }
        else{
            Map<String,Object> result = adminService.deleteAdmin(username, password);
            if (!(boolean)result.get("state")){
                jsonRender = jsonRender.passError();
                jsonRender.put("Msg", result.get("msg"));
            }
        }

        return jsonRender;
    }


    /**
     * @api {put} /admin/confirm/blockApplication Block Application confirm(认证通过用户的区块申请)
     * @apiName Admin confirm block application
     * @apiGroup Admin
     * @apiVersion 0.3.3
     *
     * @apiParam {String} id Id of the block application.
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 100 OK
     *     {
     *       "Code": 100,
     *       "Msg": "ok"
     *     }
     *
     * @apiError IllegalMethodError Method Exception!
     * @apiError AuthException Action need admin user!
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 101 AuthException
     *     {
     *       "Code": 101,
     *       "Msg": "Action need admin user!"
     *     }
     *
     *     HTTP/1.1 105 IllegalMethodError
     *     {
     *       "Code": 105,
     *       "Msg": "Method Exception!"
     *     }
     *
     */
    @ResponseBody
    @RequestMapping(value = "/confirm/blockApplication", method = RequestMethod.PUT)
    public Result confirmBlockApplication(HttpServletRequest request, HttpSession session){
        Result jsonRender = new Result();

        if (session.getAttribute("adminId") == null){
            jsonRender = jsonRender.needAuth();
            jsonRender.put("Msg", "Action need admin user!");
            return jsonRender;
        }
        String id = request.getParameter("id");
        try{
            adminService.confirmBlockApplication(id);
        }catch (Exception e){
            e.printStackTrace();
            jsonRender.put("Code", 105);
            jsonRender.put("Msg", "Method Exception!");
        }

        return jsonRender;
    }

    /**
     * @api {get} /admin/show/unconfirm/blockApplications Show unconfirm block Applications.
     * @apiName Admin get block applications
     * @apiGroup Admin
     * @apiVersion 0.5.1
     *
     * @apiParam {Number} page Present page index.
     * @apiParam {Number} rows Number of locations for one page.
     *
     * @apiSource {Number} Code Return code of state
     * @apiSource {String} Msg Msg of state
     * @apiSource {BlockApplications} Data List of blockApplications.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "Code": 200,
     *       "Msg": "ok"
     *       "Data": [
     *          {id, geohash, startDate, endDate, [uavId,...], applyUserId, confirm, msg}
     *       ]
     *     }
     *
     */
    @ResponseBody
    @RequestMapping(value = "/show/unconfirm/blockApplications", method = RequestMethod.GET)
    public Result getUnconfirmBlockApplication(HttpServletRequest request, HttpSession session){
        Result jsonRender = new Result();
        jsonRender = jsonRender.okForList();

        int page = 1, rows = 10;
        //分页的基本参数，根据需要自己设置需要的参数把
        if (!(request.getParameter("page").equals("")||request.getParameter("page")==null)
                && !(request.getParameter("rows")==null||request.getParameter("rows").equals(""))){
            page = Integer.parseInt(request.getParameter("page"));
            rows = Integer.parseInt(request.getParameter("rows"));
        }

        List<BlockApplication> blockApplications = uavService.getUnconfirmBlockApplications(page, rows);

        jsonRender.put("Data", blockApplications);

        return jsonRender;
    }

}
