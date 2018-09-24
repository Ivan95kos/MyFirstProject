package com.example.MyFirstProject.config;

//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Bean
//    public PasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder(8);
//    }
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//
//        // No session will be created or used by spring security
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        // Entry points
//        http.authorizeRequests()//
//                .antMatchers("/login","/signUpUser","/userUpdate").permitAll()
//                // Disallow everything else..
//                .anyRequest().authenticated()
//            .and()
//                .formLogin()
//                .permitAll()
//            .and()
//                .logout()
//                .permitAll();
//
//
//
//
////        http
//
////                .authorizeRequests()
////                .antMatchers("/signUpUser").permitAll()
////                .anyRequest().authenticated()
////                .and()
////                .formLogin().permitAll()
////                .and()
////                .logout().permitAll();
//    }
//
//
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
//    }
//}

