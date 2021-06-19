package com.javi.uned.pfgbackend.adapters.database.user;

import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.ports.database.UserDAO;
import com.javi.uned.pfgbackend.domain.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserDAOImpl implements UserDAO {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {

        UserEntity userEntity = UserEntityTransformer.toEntity(user);
        userEntity = userRepository.save(userEntity);

        return UserEntityTransformer.toDomainObject(userEntity);
    }

    @Override
    public User findById(Long id) throws EntityNotFound {

        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);

        if (optionalUserEntity.isPresent()) {
            return UserEntityTransformer.toDomainObject(optionalUserEntity.get());
        } else {
            throw new EntityNotFound("Could not find user with id '" + id + "'");
        }
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(UserEntityTransformer::toDomainObject)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        return userRepository.exists(Example.of(userEntity));
    }

    @Override
    public User findByEmail(String email) throws EntityNotFound {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        Optional<UserEntity> optionalUserEntity = userRepository.findOne(Example.of(userEntity));

        if(optionalUserEntity.isPresent()) {
            UserEntity result = optionalUserEntity.get();
            return UserEntityTransformer.toDomainObject(result);
        } else {
            throw new EntityNotFound("Could not find user with email '"+email+"'");
        }
    }
}
