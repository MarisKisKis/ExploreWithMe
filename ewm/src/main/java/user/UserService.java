package user;

import user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size);

    void deleteUser(Long userId);
}
