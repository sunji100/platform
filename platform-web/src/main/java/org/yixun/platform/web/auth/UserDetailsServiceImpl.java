package org.yixun.platform.web.auth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.yixun.platform.application.security.AuthDataService;

/**
 * 获得用户的详细信息
 * @author sunji
 * 
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	@Inject
	private AuthDataService authDataService;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		org.yixun.platform.application.crud.dto.UserDetails user;
		try {
			user = authDataService.loadUserByUseraccount(username);
		} catch (Exception e) {
			throw new UsernameNotFoundException("用户名没找到");
		}
		if(null == user){
			throw new UsernameNotFoundException("用户名没找到");
		}
		//用户所拥有的角色 
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for(String role:user.getRoles()){
			grantedAuthorities.add(new GrantedAuthorityImpl(role));
		}
		CustomUserDetails userDetails = new CustomUserDetails(user.getPassword(), user.getUseraccount(),user.isAccountNonExpired(),user.isAccountNonLocked(),user.isCredentialsNonExpired(),user.isEnabled(),grantedAuthorities,user.getUserId());
		return userDetails;
	}

}
