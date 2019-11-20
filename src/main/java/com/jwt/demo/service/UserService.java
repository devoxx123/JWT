package com.jwt.demo.service;

import java.util.List;

import com.jwt.demo.model.User;
import com.jwt.demo.model.UserDto;

public interface UserService {

	User save(UserDto user);

	List<User> findAll();

	void delete(int id);

	User findOne(String username);

	User findById(int id);

	UserDto update(UserDto userDto);
}
