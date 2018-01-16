package org.windwant.spring.core.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.util.StringUtils;
import org.windwant.spring.model.User;
import org.windwant.spring.util.ConfigUtil;
import org.windwant.spring.util.MD5Util;

import java.util.Collection;
import java.util.List;

/** 
*/

public class MyAuthorizingRealm extends AuthorizingRealm {
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		String name = (String) principal.getPrimaryPrincipal();
		User user = User.build("admin", "admin", 1);
		List<String> accessList;

		//获取可访问接口列表
		if (user != null) {
			accessList = ConfigUtil.getList("access.list");
		} else {
			throw new AuthorizationException();
		}

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(accessList); 
        return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
		String name = (String) authToken.getPrincipal();
		if(StringUtils.isEmpty(name) &&SecurityUtils.getSubject().getPrincipal() != null && name.equals(SecurityUtils.getSubject().getPrincipal().toString())){
			SecurityUtils.getSubject().logout();
		}

		//用户不存在
		if(name == null) {
			throw new UnknownAccountException();
		}

		//处理session
		DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
		DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
		Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
		for(Session session:sessions){
			//清除该用户以前登录时保存的session
			if(name.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
				session.setAttribute("kickout", true);
				sessionManager.getSessionDAO().update(session);
			}
		}

		//生成测试账户 userName：admin passwd：admin MD5加密
		User user = User.build(ConfigUtil.get("user.name"), MD5Util.getMD5Code(ConfigUtil.get("user.passwd")), 1);
		if(user == null) {
            throw new UnknownAccountException(); //
        }

		if(user.getStatus() == 2){
			throw new DisabledAccountException();
		}
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUserName(), 
                user.getPasswd(),
                getName() 
        );
        return authenticationInfo;
	}

	public static void main(String[] args) {
		System.out.println(ConfigUtil.getList("access.list"));
	}
}
