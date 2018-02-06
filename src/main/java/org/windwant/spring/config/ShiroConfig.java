package org.windwant.spring.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.windwant.spring.core.shiro.ComAuthFilter;
import org.windwant.spring.core.shiro.ComHashedCredentialsMatcher;
import org.windwant.spring.core.shiro.MyAuthorizingRealm;

import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfig implements EnvironmentAware {
	
	private final static int REMEMBER_ME_MAX_AGE = 365 * 24 * 3600;
	
	// 这是个DestructionAwareBeanPostProcessor的子类，负责org.apache.shiro.util.Initializable类型bean的生命周期的，
	// 初始化和销毁。主要是AuthorizingRealm类的子类，以及EhCacheManager类
	@Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
	
	@Bean
	public SimpleCookie rememberMeCookie(){
	      SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
	      simpleCookie.setMaxAge(REMEMBER_ME_MAX_AGE);
	      return simpleCookie;
	}
	
	@Bean
	public CookieRememberMeManager rememberMeManager(){
	      CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
	      cookieRememberMeManager.setCookie(rememberMeCookie());
	      //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
	      cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
	      return cookieRememberMeManager;
	}
	
	// 为了对密码进行编码的，防止密码在数据库里明码保存，当然在登陆认证，这个类也负责对form里输入的密码进行编码。
	@Bean(name = "hashedCredentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new ComHashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName("MD5");//散列算法:这里使用MD5算法;
		credentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(""));
		credentialsMatcher.setStoredCredentialsHexEncoded(true);//true时密码加密用的是Hex编码；false时用Base64编码
		return credentialsMatcher;
	}
	
	// 增加缓存减少对数据库的查询压力
	@Bean(name = "ehcacheManager")
    public EhCacheManager getEhCacheManager() {  
        EhCacheManager em = new EhCacheManager();  
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");  
        return em;  
    }  
	
	// 自定义的认证类，继承自AuthorizingRealm，负责用户的认证和权限的处理
	@Bean(name = "shiroRealm")
    public MyAuthorizingRealm shiroRealm() {
		MyAuthorizingRealm realm = new MyAuthorizingRealm();
		realm.setCredentialsMatcher(hashedCredentialsMatcher());
        realm.setCachingEnabled(true);
        realm.setCacheManager(getEhCacheManager());
        return realm;
    }

	//权限管理，这个类组合了登陆，登出，权限，session的处理
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager(){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm());
		securityManager.setCacheManager(getEhCacheManager());
		securityManager.setRememberMeManager(rememberMeManager());
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setGlobalSessionTimeout(Long.parseLong(environment.getProperty("session.timeout")));
        securityManager.setSessionManager(defaultWebSessionManager);
        return securityManager;
	}
	
	// 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证 * 配置以下
	// 两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
	@Bean(name = "advisorAutoProxyCreator")
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
	
	@Bean(name = "authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.getFilters().put("comauth", new ComAuthFilter());

        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setLoginUrl("/");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/notlogin");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/", "user");

        //本地静态文件放权
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");

        //druid监控web页面使用
        filterChainDefinitionMap.put("/druid/css/**", "anon");
        filterChainDefinitionMap.put("/druid/js/**", "anon");
        filterChainDefinitionMap.put("/druid/img/**", "anon");


        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/**.html", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/login/checkcode", "anon");
        filterChainDefinitionMap.put("/login/notlogin", "anon");
        filterChainDefinitionMap.put("/export", "anon");
        filterChainDefinitionMap.put("/spiCalc", "anon");
        filterChainDefinitionMap.put("/hello/**", "anon"); //配置不控制权限请求 anon
        filterChainDefinitionMap.put("/hellox", "anon");
        filterChainDefinitionMap.put("/druid/*", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/**", "comauth");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
