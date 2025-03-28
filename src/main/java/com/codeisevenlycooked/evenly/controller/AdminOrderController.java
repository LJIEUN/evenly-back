package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.dto.AdminListDto;
import com.codeisevenlycooked.evenly.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.getAllOrdersAdmin());
        return "admin/orders";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderAdmin(id));
        return "admin/order-form";
    }

    @PostMapping("/{id}/edit")
    public String updateOrder(@PathVariable Long id, @ModelAttribute AdminListDto adminListDto){
        orderService.updateOrderAdmin(id,adminListDto);
        return "redirect:/admin/orders";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id){
        orderService.deleteOrderAdmin(id);
        return "redirect:/admin/orders";
    }

}
