package com.radovan.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.radovan.spring.security.handler.LoginSuccessHandler;
import com.radovan.spring.service.impl.UserDetailsImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration {

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(authProvider);
	}

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http.formLogin(fl -> fl.loginPage("/login").
				successHandler(new LoginSuccessHandler()).loginProcessingUrl("/login")
				.usernameParameter("email").passwordParameter("password").permitAll());

				

		http.logout(logout -> logout.permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout"));
		
		http.csrf(csrf -> csrf.disable());

		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/login","/loginErrorPage","/loginUser").permitAll()
				.requestMatchers("/products/**", "/about").permitAll()
				.requestMatchers("/", "/home", "/loginPassConfirm").permitAll()
				.requestMatchers("/admin/**").hasAuthority("ADMIN")
				.requestMatchers("/cart/**", "/order/**").hasAuthority("ROLE_USER")
				.requestMatchers("/userRegistration","/registerComplete", "/registerFail","/register","/saveUser").anonymous()
				.anyRequest().authenticated());
				
				
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer resourcesCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/resources/**", "/static/**", "/images/**", "/css/**",
				"/js/**");
	}
}
