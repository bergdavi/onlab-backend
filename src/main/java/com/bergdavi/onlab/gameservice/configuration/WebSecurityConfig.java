package com.bergdavi.onlab.gameservice.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * WebSecurityConfig
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	private JdbcUserDetailsManager jdbcUserDetailsManager;

	private TokenBasedRememberMeServices rememberMeServices;

	private AuthenticationManager authenticationManager;

	@Autowired
	public WebSecurityConfig(DataSource dataSource) throws Exception {
		jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);

		rememberMeServices = new TokenBasedRememberMeServices("superSecretKey", jdbcUserDetailsManager);
		rememberMeServices.setAlwaysRemember(true);
	}

	@Bean
    public JsonUsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        JsonUsernamePasswordAuthenticationFilter authenticationFilter
			= new JsonUsernamePasswordAuthenticationFilter();
		authenticationFilter.setRememberMeServices(rememberMeServices);
        authenticationFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler(){

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				response.setStatus(200);
			}
		});
		authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/game-service/v1/users/login", "POST"));
		
		if(authenticationManager == null) {
			authenticationManager = authenticationManagerBean();
		}
        authenticationFilter.setAuthenticationManager(authenticationManager);
        return authenticationFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if(authenticationManager == null) {
			authenticationManager = authenticationManagerBean();
		}
		http
			.authorizeRequests()
				
				.antMatchers(
					"/game-service/v1/users/register", 
					"/game-service/v1/users/login",
					"/game-service/v1/games"
					).permitAll()
				.antMatchers(
					"/game-service/v1/**"
					).authenticated()
				.antMatchers("/game-service/v1/users", "/game-service/v1/users/").hasAuthority("ROLE_ADMIN")
				.anyRequest().permitAll()
				.and()
			.addFilterAfter(new JsonUsernamePasswordAuthenticationFilter(), RememberMeAuthenticationFilter.class)
			.addFilterBefore(new RememberMeAuthenticationFilter(authenticationManager, rememberMeServices), RememberMeAuthenticationFilter.class)
			// .rememberMe()
			// 	.userDetailsService(jdbcUserDetailsManager)
			// 	.rememberMeServices(rememberMeServices)
			// 	.alwaysRemember(true)
			// 	.key("superSecretKey")
			// 	.and()
            .csrf()
				.disable()
			.logout()
				.logoutUrl("/game-service/v1/users/logout")
				.logoutSuccessHandler(new LogoutSuccessHandler(){				
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						response.setStatus(200);
					}
				})
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

	@Bean
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
}