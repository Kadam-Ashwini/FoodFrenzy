package com.app.food.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.model.PaymentMethod;
import com.app.food.service.PaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	private Long requireUserId(HttpSession session) {
		Object uid = session.getAttribute("userId");
		return uid == null ? null : (Long) uid;
	}

	
	/*
	 * @GetMapping("/payment/{orderId}") public String paymentPage(@PathVariable
	 * Long orderId, HttpSession session, Model model) { Long userId =
	 * requireUserId(session); if (userId == null) return "redirect:/login";
	 * 
	 * var order = paymentService.getOrderForUser(orderId, userId); var items =
	 * paymentService.items(orderId);
	 * 
	 * model.addAttribute("order", order); model.addAttribute("items", items);
	 * model.addAttribute("methods", PaymentMethod.values()); return "payment"; }
	 */
	  
	@GetMapping("/payment/{orderId}")
	public String paymentPage(@PathVariable Long orderId, HttpSession session, Model model) {
	    Long userId = requireUserId(session);
	    if (userId == null) return "redirect:/login";

	    var order = paymentService.getOrderForUser(orderId, userId);
	    var items = paymentService.items(orderId);
	    var txns = paymentService.transactions(orderId);

	    model.addAttribute("order", order);
	    model.addAttribute("items", items);
	    model.addAttribute("txns", txns);
	    model.addAttribute("methods", PaymentMethod.values());
	    return "payment";
	}
	 

	@PostMapping("/payment/{orderId}/success")
	public String paySuccess(@PathVariable Long orderId, @RequestParam PaymentMethod method, HttpSession session) {
		Long userId = requireUserId(session);
		if (userId == null)
			return "redirect:/login";

		paymentService.markPaid(orderId, userId, method);
		return "redirect:/orders/" + orderId + "?ok=Payment%20Successful";
	}

	@PostMapping("/payment/{orderId}/failed")
	public String payFailed(@PathVariable Long orderId, @RequestParam PaymentMethod method, HttpSession session) {
		Long userId = requireUserId(session);
		if (userId == null)
			return "redirect:/login";

		paymentService.markFailed(orderId, userId, method);
		return "redirect:/payment/" + orderId + "?msg=Payment%20Failed.%20Try%20again";
	}

	@PostMapping("/payment/{orderId}/cod")
	public String cod(@PathVariable Long orderId, HttpSession session) {
		Long userId = requireUserId(session);
		if (userId == null)
			return "redirect:/login";

		paymentService.chooseCod(orderId, userId);
		return "redirect:/orders/" + orderId + "?ok=Cash%20on%20Delivery%20selected";
	}
}