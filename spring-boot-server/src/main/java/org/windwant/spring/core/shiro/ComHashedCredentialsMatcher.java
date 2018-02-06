package org.windwant.spring.core.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2017/9/11.
 */
public class ComHashedCredentialsMatcher extends HashedCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = ((UsernamePasswordToken)token);
        if(usernamePasswordToken == null) return false;

        String password = String.valueOf(usernamePasswordToken.getPassword());
        if(StringUtils.isEmpty(password)) return false;

        Object tokenHashedCredentials = hashProvidedCredentials(token, info);
        Object accountCredentials = getCredentials(info);
        return equals(tokenHashedCredentials, accountCredentials);
    }
}
