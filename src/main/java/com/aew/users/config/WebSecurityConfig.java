package com.aew.users.config;

import com.aew.users.error.CustomAccessDeniedHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Configuracion de Spring Security
 * 
 * @author Adrian
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  /**
   * Autentificacion de usuarios en memoria
   */
  /*@Autowired
  public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {

    auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER").and().withUser("admin")
        .password("{noop}admin").roles("ADMIN", "MOD", "USER").and().withUser("mod").password("{noop}mod")
        .roles("MOD", "USER");
  }*/


  @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.authorizeRequests().antMatchers("/api/v1/welcome", "/", "/api/v1/info").permitAll()
        .antMatchers(HttpMethod.POST,"api/v1/user").permitAll()
        .antMatchers("/api/v1/user/*").hasAnyRole("USER", "ADMIN").antMatchers("/api/v1/users").hasRole("ADMIN");
        //.anyRequest().authenticated();

    http.formLogin()
        // .loginPage("mi pagina html o jsp de login")
        //.failureUrl("/api/v1/login/error")
        //.usernameParameter("login")
        //.loginProcessingUrl("/perform_login")
        //.passwordParameter("password")
        //.failureHandler(authenticationFailureHandler())
        .defaultSuccessUrl("/api/v1/welcome")
        /*.failureHandler((req,res,exp)->{  // Failure handler invoked after authentication failure
          String errMsg="";
          if(exp.getClass().isAssignableFrom(BadCredentialsException.class)){
            errMsg="Invalid username or password.";
            System.out.println(errMsg);
          }else{
             errMsg="Unknown error - "+exp.getMessage();
             System.out.println(errMsg);
          }
          req.getSession().setAttribute("message", errMsg);
          res.sendRedirect("/api/v1/error"); // Redirect user to login page with error message.
       })*/
        .permitAll();

    http.logout()
        //.logoutUrl("/api/v1/logout")
        .deleteCookies("JSESSIONID")
        .logoutSuccessUrl("/api/v1/logout")
        .invalidateHttpSession(true)
        .permitAll();

    http.exceptionHandling().accessDeniedPage("/api/v1/accessDenied");
    //http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    
     //Control de concurrencia de recursos:
        /*http.sessionManagement()
                .invalidSessionUrl("Pagina de la sesion ha expirado")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true); //Solo deja entrar a 1, si pongo false no se queja pero invalida la sesion actual
        */

        //REMEMBER ME:
        
        /*http.rememberMe()
                .rememberMeParameter("rememberMeParameter") //Nombre x defecto del remember me
                .rememberMeCookieName("my remember me") //Nombre de la cookie
                .tokenValiditySeconds(86400); //La cantidad de tiempo que la sesion estara iniciada
        */ 

         //403 FORBIDEN
        // When the user has logged in as XX.
        // But access a page that requires role YY,
        // AccessDeniedException will be thrown.
        //http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
       /*http.exceptionHandling()
                .accessDeniedPage("Pagina de acceso denegado diferente para que salga cuando intento acceder a algun metodo 
                                  de un controlador para que el fui habilitado");
        
        */

    http.csrf().disable();
  
  }

  @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}