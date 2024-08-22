package com.ufund.persistence;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.model.User;
import org.springframework.stereotype.Service;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final String USER_FILE_PATH = "C:/Users/q1585/IdeaProjects/ufund/data/users.json";


    public Optional<User> findUserByUsername(String name){
        List<User> users = loadUserJson();
        return users.stream().filter(user -> user.getUsername().equalsIgnoreCase(name)).findFirst();
    }
    private List<User> loadUserJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(USER_FILE_PATH);

        System.out.println("Trying to load file from: " + file.getAbsolutePath());

        try {
            return objectMapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load users from JSON file", e);
        }
    }
    public User createUser(User newUser){
        List<User> user = loadUserJson();
        int newId = user.isEmpty() ? 1 : loadUserJson().size();
        newUser.setId(newId);
        user.add(newUser);
        saveUserJson(user);
        return newUser;
    }
    private void saveUserJson(List<User> users){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(USER_FILE_PATH), users);
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to load users from JSON file", e);
        }
    }

}
