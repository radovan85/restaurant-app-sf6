package com.radovan.spring.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.form.RegistrationForm;
import com.radovan.spring.service.AddressService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.UserService;

@Controller
public class MainController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@GetMapping(value = "/")
	public String indexPage() {
		return "index";
	}

	@GetMapping(value = "/login")
	public String login() {
		return "fragments/login :: ajaxLoadedContent";
	}

	@GetMapping(value = "/home")
	public String homePage() {
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/userRegistration")
	public String registration(ModelMap map) {
		RegistrationForm tempForm = new RegistrationForm();
		map.put("tempForm", tempForm);
		return "fragments/registration :: ajaxLoadedContent";
	}

	@PostMapping(value = "/saveUser")
	public String createUser(@ModelAttribute("tempForm") RegistrationForm tempForm) {
		customerService.registerCustomer(tempForm);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/registerComplete")
	public String registrationCompleted() {
		return "fragments/registration_completed :: ajaxLoadedContent";
	}

	@GetMapping(value = "/registerFail")
	public String registrationFailed() {
		return "fragments/registration_failed :: ajaxLoadedContent";
	}

	@GetMapping(value = "/loginErrorPage")
	public String logError(ModelMap map) {
		map.put("alert", "Invalid username or password!");
		return "fragments/login :: ajaxLoadedContent";
	}

	@PostMapping(value = "/loginPassConfirm")
	public String confirmLoginPass(Principal principal) {
		Optional<Principal> authPrincipal = Optional.ofNullable(principal);
		if (!authPrincipal.isPresent()) {
			Error error = new Error("Invalid user");
			throw new InvalidUserException(error);
		}

		return "fragments/homePage :: ajaxLoadedContent";
	}

	@PostMapping(value = "/loggedout")
	public String logout() {
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(null);
		SecurityContextHolder.clearContext();
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@GetMapping(value = "/accountInfo")
	public String userAccountInfo(ModelMap map) {
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		AddressDto address = addressService.getAddressById(customer.getAddressId());
		map.put("authUser", authUser);
		map.put("address", address);
		return "fragments/accountDetails :: ajaxLoadedContent";
	}

	@GetMapping(value = "/about")
	public String aboutPage() {
		return "fragments/about :: ajaxLoadedContent";
	}
}
