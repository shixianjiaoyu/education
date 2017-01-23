package cn.sjjy.edu.web.framework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhongyuan on 2016/10/27.
 *
 * @author zhongyuan@bilibili.com
 */
@Controller
@RequestMapping(value = "/")
public class HomeController {

    @ResponseBody
    @RequestMapping(value = "/checkhealth.do", method = RequestMethod.GET, produces = "text/plain")
    public String checkHealth() {
        return  "server is health";
    }
}
