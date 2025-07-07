package com.lcwd.electronic.store.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails { // ye class represent kregi database m table
// orr persistent data ko 

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String userId;

	private String name;

	@Column(unique = true)
	private String email;

	@Column(length = 500)
	private String password;

	private String gender;

	@Column(length = 1000)
	private String about;

	private String imageName;

//	one user can have many orders...
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Order> orders = new ArrayList<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Role> roles = new HashSet<>();
	
	@OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
	private Cart cart;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		we have to return collection of GrantedAuthority
		Set<SimpleGrantedAuthority> setsAuthorities = this.roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());
		return setsAuthorities;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

}
