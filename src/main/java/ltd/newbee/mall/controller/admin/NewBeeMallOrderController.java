/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.newbee.mall.controller.admin;

import ltd.newbee.mall.common.ServiceResultEnum;
import ltd.newbee.mall.controller.vo.NewBeeMallOrderItemVO;
import ltd.newbee.mall.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.dao.NewBeeMallOrderItemMapper;
import ltd.newbee.mall.dao.NewBeeMallOrderMapper;
import ltd.newbee.mall.entity.NewBeeMallOrder;
import ltd.newbee.mall.entity.NewBeeMallOrderItem;
import ltd.newbee.mall.service.NewBeeMallOrderService;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link https://github.com/newbee-ltd
 */
@Controller
@RequestMapping("/admin")
public class NewBeeMallOrderController {

    @Resource
    private NewBeeMallOrderService newBeeMallOrderService;
    @Autowired
    private NewBeeMallOrderMapper newBeeMallOrderMapper;
    @Autowired
    private NewBeeMallGoodsMapper newBeeMallGoodsMapper;
    @Autowired
    private NewBeeMallOrderItemMapper newBeeMallOrderItemMapper;

    @GetMapping("/orders")
    public String ordersPage(HttpServletRequest request) {
        request.setAttribute("path", "orders");
        return "admin/newbee_mall_order";
    }

    @GetMapping("/orders_manage")
    public String ordersManagePage(HttpServletRequest request) {
        request.setAttribute("path", "orders_manage");
        return "admin/newbee_mall_order_manage";
    }

    @PostMapping("/orders_manage_yesOrNo")
    @ResponseBody
    @Transactional
    public String ordersManageYesOrNo(@RequestBody List<Long> ids, @RequestParam Integer yesOrNo, HttpServletRequest request) {
        for (Long id : ids) {
            NewBeeMallOrder newBeeMallOrder = newBeeMallOrderMapper.selectByPrimaryKey(id);
            if (yesOrNo == 1){
                newBeeMallOrder.setPayStatus(new Byte("1"));
                newBeeMallOrder.setOrderStatus(new Byte("1"));
                List<NewBeeMallOrderItem> newBeeMallOrderItems = newBeeMallOrderItemMapper.selectByOrderId(newBeeMallOrder.getOrderId());
                //减库存
                for (NewBeeMallOrderItem orderItem : newBeeMallOrderItems) {
                    newBeeMallGoodsMapper.reduceStockNum(orderItem.getGoodsId(), orderItem.getGoodsCount());
                }
            }else {
                newBeeMallOrder.setPayStatus(new Byte("-1"));
                newBeeMallOrder.setOrderStatus(new Byte("-1"));
            }
            newBeeMallOrderMapper.updateByPrimaryKey(newBeeMallOrder);
        }
        return "1";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/orders/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params, HttpSession session) {
        if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String myLoginName = (String) session.getAttribute("loginUser");
        params.put("myLoginName", myLoginName);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallOrderService.getNewBeeMallOrdersPage(pageUtil));
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/orders/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody NewBeeMallOrder newBeeMallOrder) {
        if (Objects.isNull(newBeeMallOrder.getTotalPrice())
                || Objects.isNull(newBeeMallOrder.getOrderId())
                || newBeeMallOrder.getOrderId() < 1
                || newBeeMallOrder.getTotalPrice() < 1
                || !StringUtils.hasText(newBeeMallOrder.getUserAddress())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallOrderService.updateOrderInfo(newBeeMallOrder);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/order-items/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        List<NewBeeMallOrderItemVO> orderItems = newBeeMallOrderService.getOrderItems(id);
        if (!CollectionUtils.isEmpty(orderItems)) {
            return ResultGenerator.genSuccessResult(orderItems);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }

    /**
     * 配货
     */
    @RequestMapping(value = "/orders/checkDone", method = RequestMethod.POST)
    @ResponseBody
    public Result checkDone(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallOrderService.checkDone(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 出库
     */
    @RequestMapping(value = "/orders/checkOut", method = RequestMethod.POST)
    @ResponseBody
    public Result checkOut(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallOrderService.checkOut(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/orders/close", method = RequestMethod.POST)
    @ResponseBody
    public Result closeOrder(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallOrderService.closeOrder(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


}