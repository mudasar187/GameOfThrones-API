package com.ahmmud16.gameofthrones.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 * Created by Endre on 02.09.2018.
 * news
 * Verson:
 */

@Configuration
class Security : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity): Unit {
        http
                .authorizeRequests()
                .antMatchers("/*").permitAll()
                .antMatchers("/gotrest/api/").permitAll()
                .and()
                .cors().disable()
                .csrf().disable()
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("user").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("ADMIN")
    }
}