package co.nuqui.tech.msusers.infrastructure.persistence;

import co.nuqui.tech.msusers.domain.dto.User;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public User findByEmailIgnoreCase(String email) {
        User user = new User();
        user.setEmail(email);

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withIndexName("emailindex") // Use the global secondary index name
                .withConsistentRead(false) // Adjust as needed
                .withKeyConditionExpression("email = :val")
                .withExpressionAttributeValues(
                        Map.of(":val", new AttributeValue().withS(email))
                );

        PaginatedQueryList<User> result = dynamoDBMapper.query(User.class, queryExpression);

        return result.stream().findFirst().orElse(null);
    }

    public User findByUsernameIgnoreCase(String username) {
        User user = new User();
        user.setUsername(username);

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withIndexName("usernameindex") // Use the global secondary index name
                .withConsistentRead(false) // Adjust as needed
                .withKeyConditionExpression("username = :val")
                .withExpressionAttributeValues(
                        Map.of(":val", new AttributeValue().withS(username))
                );

        PaginatedQueryList<User> result = dynamoDBMapper.query(User.class, queryExpression);

        return result.stream().findFirst().orElse(null);
    }

    public User save(User user) {
        dynamoDBMapper.save(user);
        user.setStatus("SAVED");
        return user;
    }

//    public User update(String userId, User user) {
//        dynamoDBMapper.save(user,
//                new DynamoDBSaveExpression()
//                        .withExpectedEntry("id",
//                                new ExpectedAttributeValue(
//                                        new AttributeValue().withS(userId)
//                                )));
//        user.setStatus("UPDATED");
//        return user;
//    }
}
