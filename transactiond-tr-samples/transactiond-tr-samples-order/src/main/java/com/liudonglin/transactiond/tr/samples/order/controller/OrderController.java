package com.liudonglin.transactiond.tr.samples.order.controller;

import com.liudonglin.transactiond.tr.samples.order.entity.Order;
import com.liudonglin.transactiond.tr.samples.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    List<Order> list(){
        List<Order> list = new ArrayList<>();
        list.add(new Order(){});
        return list;
    }

    /**
     * 创建订单
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public @ResponseBody String create() {
        Order order = new Order();
        order.setUserId(1L);
        order.setProductId(1L);
        order.setCount(1);
        order.setMoney(new BigDecimal("100"));
        order.setStatus(0);
        orderService.create(order);
        return "Create order success";
    }

}
