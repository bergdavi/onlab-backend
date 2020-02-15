package com.bergdavi.onlab.gameservice.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * WebSecurityConfig
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	// @Autowired
	// private DataSource dataSource;

	private JdbcUserDetailsManager jdbcUserDetailsManager;

	@Autowired
	public WebSecurityConfig(DataSource dataSource) {
		jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/game-service/v1/users/register", "/login").permitAll()
				.anyRequest().authenticated()
                .and()
            .formLogin()
				.loginPage("/login")
				.permitAll()
				.defaultSuccessUrl("/game-service/v1/users", true)
				.failureHandler(new AuthenticationFailureHandler(){				
					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException exception) throws IOException, ServletException {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						response.getWriter().write(exception.getLocalizedMessage());
					}
				})
				.and()
            .csrf()
				.disable()
			.logout()
				.permitAll()
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(new AuthenticationEntryPoint(){				
					@Override
					public void commence(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException authException) throws IOException, ServletException {
						if(authException != null) {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write(authException.getLocalizedMessage());
						}						
					}
				});
	}

	@Bean
	@Override
	public JdbcUserDetailsManager userDetailsService() {
		return jdbcUserDetailsManager;
	}

	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}