package hr.moremogucnosti.more_mogucnosti_backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserPrincipal extends User {
    private final Long id;
    public Long getId() {return id;}

    public AppUserPrincipal(Long id,
                            String username,
                            String password,
                            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }
}
