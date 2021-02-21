package com.seesawin.controller;

import com.seesawin.models.Orderdetail;
import com.seesawin.models.Ordermain;
import com.seesawin.payload.request.GetOrdeByUserNamerRequest;
import com.seesawin.payload.request.OrderRequest;
import com.seesawin.payload.response.CommonResponse;
import com.seesawin.payload.response.OrderListResponse;
import com.seesawin.payload.response.OrderdetailResponse;
import com.seesawin.repository.OrderdetailMapper;
import com.seesawin.repository.OrdermainMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrdermainMapper ordermainMapper;

    @Autowired
    private OrderdetailMapper orderdetailMapper;

    @PostMapping("/save")
    @ApiOperation(
            value = "新增訂單",
            notes = "資料新增、刪除、修改時，需要使用POST",
            response = CommonResponse.class)
    @PreAuthorize("hasRole('USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("orderRquest: ", orderRequest);

        // calculate the total price
        Integer totalPrice = orderRequest.getOrderDetialRequestList().stream()
                .map(od -> od.getPrice() * od.getCount())
                .reduce(0, Integer::sum);

        // add order
        Ordermain order = new Ordermain();
        order.setUsername(orderRequest.getUsername());
        order.setTotalprice(totalPrice.intValue());
        int row = ordermainMapper.insert(order);
        log.info("row: ", row);
        int orderNo = order.getOrderno();
        log.info("orderNo: ", orderNo);

        // add order details
        orderRequest.getOrderDetialRequestList().forEach(od -> {
            Orderdetail detail = Orderdetail.builder()
                    .count(od.getCount())
                    .name(od.getName())
                    .price(od.getPrice())
                    .orderno(orderNo).build();
            orderdetailMapper.insert(detail);
        });

        Map<String, Object> resultParam = new HashMap<>();
        resultParam.put("orderNo", orderNo);

        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("sucess");
        response.setData(resultParam);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orderNo/{orderNo}")
    @ApiOperation(
            value = "根據orderNo查詢訂單(restful)",
            notes = "資料查詢時使用GET",
            response = CommonResponse.class)
    @PreAuthorize("hasRole('USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    public ResponseEntity<?> getOrder(@Valid @PathVariable int orderNo) {
        Ordermain ordermain = ordermainMapper.selectByPrimaryKey(orderNo);
        List<Orderdetail> orderdetailList = orderdetailMapper.selectByOrderNo(orderNo);

        if (orderdetailList.size() == 0) {
            CommonResponse response = new CommonResponse();
            response.setCode("00");
            response.setMsg("orderNo: " + orderNo + " isn't exist");
            return ResponseEntity.ok(response);
        }

        List<OrderdetailResponse> collect = orderdetailList.stream().map(od -> {
            OrderdetailResponse newOd = OrderdetailResponse.builder()
                    .count(od.getCount())
                    .name(od.getName())
                    .price(od.getPrice()).build();
            return newOd;
        }).collect(Collectors.toList());

        OrderListResponse orderListResponse = OrderListResponse.builder()
                .orderNo(orderNo)
                .username(ordermain.getUsername())
                .totalPrice(ordermain.getTotalprice())
                .orderDetails(collect).build();

        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("sucess");
        response.setData(orderListResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    @ApiOperation(
            value = "根據username查詢訂單(restful)",
            notes = "資料查詢時使用GET",
            response = CommonResponse.class)
    @PreAuthorize("hasRole('USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    public ResponseEntity<?> getOrder(@Valid @PathVariable String username) {
        List<OrderListResponse> orderListResponse = this.getOrderListResponse(username);
        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("sucess");
        if (orderListResponse == null) {
            response.setMsg(username + " don't have any order");
            return ResponseEntity.ok(response);
        }
        response.setData(orderListResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username")
    @ApiOperation(
            value = "根據username查詢訂單(queryString)",
            notes = "資料查詢時使用GET",
            response = CommonResponse.class)
    @PreAuthorize("hasRole('USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    public ResponseEntity<?> getOrderByQueryString(@Valid @RequestParam String username) {
        List<OrderListResponse> orderListResponse = this.getOrderListResponse(username);
        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("sucess");
        if (orderListResponse == null) {
            response.setMsg(username + " don't have any order");
            return ResponseEntity.ok(response);
        }
        response.setData(orderListResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/username")
    @ApiOperation(
            value = "根據username查詢訂單(data in the post's body)",
            notes = "資料查詢時使用GET",
            response = CommonResponse.class)
    @PreAuthorize("hasRole('USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    public ResponseEntity<?> getOrderByPost(@Valid @RequestBody GetOrdeByUserNamerRequest getOrdeByUserNamerRequest) {
        List<OrderListResponse> orderListResponse = this.getOrderListResponse(getOrdeByUserNamerRequest.getUsername());
        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("sucess");
        if (orderListResponse == null) {
            response.setMsg(getOrdeByUserNamerRequest.getUsername() + " don't have any order");
            return ResponseEntity.ok(response);
        }
        response.setData(orderListResponse);
        return ResponseEntity.ok(response);
    }

    private List<OrderListResponse> getOrderListResponse(String username) {
        List<OrderListResponse> resultList = new ArrayList<>();
        List<Ordermain> ordermainList = ordermainMapper.selectByUsername(username);
        if (ordermainList.size() == 0) {
            return null;
        }
        ordermainList.forEach(odMain -> {
            List<Orderdetail> orderdetailList = orderdetailMapper.selectByOrderNo(odMain.getOrderno());

            List<OrderdetailResponse> collect = orderdetailList.stream().map(od -> {
                OrderdetailResponse newOd = OrderdetailResponse.builder()
                        .count(od.getCount())
                        .name(od.getName())
                        .price(od.getPrice()).build();
                return newOd;
            }).collect(Collectors.toList());

            OrderListResponse orderListResponse = OrderListResponse.builder()
                    .orderNo(odMain.getOrderno())
                    .username(odMain.getUsername())
                    .totalPrice(odMain.getTotalprice())
                    .orderDetails(collect).build();

            resultList.add(orderListResponse);
        });
        return resultList;
    }
}



