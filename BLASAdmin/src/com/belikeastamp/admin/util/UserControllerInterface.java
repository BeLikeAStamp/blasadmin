package com.belikeastamp.admin.util;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.belikeastamp.admin.model.User;

public interface UserControllerInterface {
	 @Put
	 void create(User u);

	 @Get
	 Container getAllUsers();
	 
	 @Post
	 void update(User u);
	 
	 @Delete
	 void delete(Long id);
}
