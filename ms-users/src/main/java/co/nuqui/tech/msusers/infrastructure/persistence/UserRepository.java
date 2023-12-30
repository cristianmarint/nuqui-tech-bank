package co.nuqui.tech.msusers.infrastructure.persistence;

import co.nuqui.tech.msusers.domain.dto.User;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@AllArgsConstructor
public class UserRepository{
    @Autowired
    private DynamoDBMapper mapper;


    public User save(User User) {
        mapper.save(User);
        return User;
    }

    public User findUserByUserId(String UserId) {
        return mapper.load(User.class, UserId);
    }

    public String deleteUser(User User) {
        mapper.delete(User);
        return "User removed !!";
    }

    public User edit(User User) {
        mapper.save(User, buildExpression(User));
        return User;
    }

    private DynamoDBSaveExpression buildExpression(User User) {
        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
        expectedMap.put("id", new ExpectedAttributeValue(new AttributeValue().withS(String.valueOf(User.getId()))));
        dynamoDBSaveExpression.setExpected(expectedMap);
        return dynamoDBSaveExpression;
    }

    public User findByEmailIgnoreCase(String email) {
        return mapper.load(User.class, email);
    }

    public User findByUsernameIgnoreCase(String username) {
        return mapper.load(User.class, username);
    }
}
