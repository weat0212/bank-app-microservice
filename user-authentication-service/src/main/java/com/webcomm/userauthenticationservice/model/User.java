package com.webcomm.userauthenticationservice.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "First Name cannot be null")
	@NotBlank
	private String firstName;
	@NotNull(message = "Last Name cannot be null")
	@NotBlank
	private String lastName;
	
	@Email(message = "Email should be valid")
	@Pattern(regexp="^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z]+$",
			message="Email Format Not Correct!")
	@NotBlank
	@NotNull(message = "Email cannot be null")
	private String email;
	
	@NotNull(message = "Password cannot be null")
	@NotBlank
	private String password;
	
	private String jwtToken;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "user_role", 
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "id"))
	private Collection<Role> roles;
}