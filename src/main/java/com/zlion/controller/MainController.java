package com.zlion.controller;

import com.zlion.model.Article;
import com.zlion.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: zengzhishi
 * Created by zzs on 2016/8/31.
 * This is a simple test controller as demo
 */
@Controller
@EnableAutoConfiguration
public class MainController {

    private final BlogRepository blogRepository;

    private String hello = "HelloShanhy";

    @Autowired
    public MainController(BlogRepository blogRepository){
        this.blogRepository = blogRepository;
    }

    @ResponseBody
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Map<String, Object> index(){


        Map<String, Object> map = new HashMap<String, Object>(3);
        List<Article> articles = blogRepository.findAll();
        map.put("status","success");
        map.put("code","100");
        map.put("data",articles);
        return map;
    }

//    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
//    public int test(){
//        Test test = new Test();
//
//        return test.calculate(10, 20);
//    }

    @RequestMapping(value = "/hello")
    public String page(Map<String,Object> map){
        map.put("hello","from TemplateController.hello");
        return "/hello";
    }

}
