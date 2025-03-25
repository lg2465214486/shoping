/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.newbee.mall.controller.admin;

import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.ObjectUtil;
import ltd.newbee.mall.common.ServiceResultEnum;
import ltd.newbee.mall.controller.vo.PayConfigVo;
import ltd.newbee.mall.dao.IndexConfigMapper;
import ltd.newbee.mall.entity.AdminUser;
import ltd.newbee.mall.entity.IndexConfig;
import ltd.newbee.mall.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link https://github.com/newbee-ltd
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminUserService adminUserService;
    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }

    @GetMapping({"/test"})
    public String test() {
        return "admin/test";
    }


    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        //获取usdt地址和二维码
        IndexConfig usdtConfig = indexConfigMapper.selectByTypeAndGoodsId(8, 1L);
        request.setAttribute("usdtConfig", usdtConfig);
        IndexConfig bankConfig = indexConfigMapper.selectByTypeAndGoodsId(8, 2L);
        request.setAttribute("bankConfig", bankConfig);
        return "admin/index";
    }

    @PostMapping("/savePayConfig")
    @ResponseBody
    public String savePayConfig(@RequestBody PayConfigVo payConfigVo, HttpServletRequest request){
        IndexConfig usdtConfig = indexConfigMapper.selectByTypeAndGoodsId(8, 1L);
        if(ObjectUtil.isEmpty(usdtConfig)){
            usdtConfig = new IndexConfig();
            usdtConfig.setConfigId(100000000L);
            usdtConfig.setGoodsId(1L);
            usdtConfig.setConfigType(new Byte("8"));
            usdtConfig.setConfigName(payConfigVo.getUsdtAddress());
            usdtConfig.setRedirectUrl(payConfigVo.getUsdtImg());
            usdtConfig.setConfigRank(0);
            usdtConfig.setIsDeleted(new Byte("0"));
            usdtConfig.setCreateTime(new Date());
            usdtConfig.setUpdateTime(new Date());
            usdtConfig.setCreateUser(0);
            indexConfigMapper.insert(usdtConfig);
        }else {
            usdtConfig.setConfigName(payConfigVo.getUsdtAddress());
            usdtConfig.setRedirectUrl(payConfigVo.getUsdtImg());
            usdtConfig.setUpdateTime(new Date());
            indexConfigMapper.updateByPrimaryKey(usdtConfig);
        }
        IndexConfig bankConfig = indexConfigMapper.selectByTypeAndGoodsId(8, 2L);
        if(ObjectUtil.isEmpty(bankConfig)){
            bankConfig = new IndexConfig();
            bankConfig.setConfigId(200000000L);
            bankConfig.setGoodsId(2L);
            bankConfig.setConfigType(new Byte("8"));
            bankConfig.setConfigName(payConfigVo.getBankCard());
            bankConfig.setRedirectUrl(payConfigVo.getBankImg());
            bankConfig.setConfigRank(0);
            bankConfig.setIsDeleted(new Byte("0"));
            bankConfig.setCreateTime(new Date());
            bankConfig.setUpdateTime(new Date());
            bankConfig.setCreateUser(0);
            indexConfigMapper.insert(bankConfig);
        }else {
            bankConfig.setConfigName(payConfigVo.getBankCard());
            bankConfig.setRedirectUrl(payConfigVo.getBankImg());
            bankConfig.setUpdateTime(new Date());
            indexConfigMapper.updateByPrimaryKey(bankConfig);
        }
        return "保存成功！";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session) {
//        if (!StringUtils.hasText(verifyCode)) {
//            session.setAttribute("errorMsg", "验证码不能为空");
//            return "admin/login";
//        }
        if (!StringUtils.hasText(userName) || !StringUtils.hasText(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
//        ShearCaptcha shearCaptcha = (ShearCaptcha) session.getAttribute("verifyCode");
//        if (shearCaptcha == null || !shearCaptcha.verify(verifyCode)) {
//            session.setAttribute("errorMsg", "验证码错误");
//            return "admin/login";
//        }
        AdminUser adminUser = adminUserService.login(userName, password);
        if (adminUser != null) {
            session.setAttribute("loginUser", adminUser.getLoginUserName());
            session.setAttribute("shareCode", adminUser.getShareCode());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            //session过期时间设置为7200秒 即两小时
            //session.setMaxInactiveInterval(60 * 60 * 2);
            return "redirect:/admin/index";
        } else {
            session.setAttribute("errorMsg", "登录失败");
            return "admin/login";
        }
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminUserService.getUserDetailById(loginUserId);
        if (adminUser == null) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getLoginUserName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (!StringUtils.hasText(originalPassword) || !StringUtils.hasText(newPassword)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updatePassword(loginUserId, originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return ServiceResultEnum.SUCCESS.getResult();
        } else {
            return "修改失败";
        }
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName) {
        if (!StringUtils.hasText(loginUserName) || !StringUtils.hasText(nickName)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updateName(loginUserId, loginUserName, nickName)) {
            return ServiceResultEnum.SUCCESS.getResult();
        } else {
            return "修改失败";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }
}
