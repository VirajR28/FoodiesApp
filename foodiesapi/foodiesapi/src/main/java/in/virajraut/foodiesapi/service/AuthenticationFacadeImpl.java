package in.virajraut.foodiesapi.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade{
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication(); //Will use this to get the authentication, from that we'll get the logged in user's emailId, through that we'll get the userId.
    }
}
