package com.example.library.server.api;

import com.example.library.server.dataaccess.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

@Component
public class UserResourceAssembler {

  private final ModelMapper modelMapper;
  private final IdGenerator idGenerator;

  public UserResourceAssembler(ModelMapper modelMapper, IdGenerator idGenerator) {
    this.modelMapper = modelMapper;
    this.idGenerator = idGenerator;
  }

  public UserResource toResource(User user) {
    return modelMapper.map(user, UserResource.class);
  }

  public User toModel(UserResource userResource) {
    User user = modelMapper.map(userResource, User.class);
    if (user.getId() == null) {
      user.setId(idGenerator.generateId());
    }
    return user;
  }
}
