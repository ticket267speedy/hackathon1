package com.ticket267.hackathon1.security;

import com.ticket267.hackathon1.model.Player;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Player player;

    public CustomUserDetails(Player player) { this.player = player; }

    public Player getPlayer() { return player; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + player.getRole().name()));
    }
    @Override public String getPassword()  { return player.getPasswordHash(); }
    @Override public String getUsername()  { return player.getUsername(); }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
