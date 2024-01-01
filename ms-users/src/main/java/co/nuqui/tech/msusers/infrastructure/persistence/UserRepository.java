package co.nuqui.tech.msusers.infrastructure.persistence;

import co.nuqui.tech.msusers.domain.dto.User;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public User findByEmail(String email) {
        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withIndexName("emailindex") // Use the global secondary index name
                .withConsistentRead(false) // Adjust as needed
                .withKeyConditionExpression("email = :val")
                .withExpressionAttributeValues(
                        Map.of(":val", new AttributeValue().withS(email.toLowerCase(Locale.ROOT)))
                );

        PaginatedQueryList<User> result = dynamoDBMapper.query(User.class, queryExpression);

        return result.stream().findFirst().orElse(null);
    }

    public User findByUsernameIgnoreCase(String username) {

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withIndexName("usernameindex") // Use the global secondary index name
                .withConsistentRead(false) // Adjust as needed
                .withKeyConditionExpression("username = :val")
                .withExpressionAttributeValues(
                        Map.of(":val", new AttributeValue().withS(username.toLowerCase(Locale.ROOT)))
                );

        PaginatedQueryList<User> result = dynamoDBMapper.query(User.class, queryExpression);

        return result.stream().findFirst().orElse(null);
    }

    public User save(User user) {
        user.setEmail(user.getEmail().toLowerCase(Locale.ROOT));
        user.setUsername(user.getUsername().toLowerCase(Locale.ROOT));
        dynamoDBMapper.save(user);
        return user;
    }

    public String getUserStatusByEmail(String email) {
        return findByEmail(email).getStatus();
    }

    public User update(User user) {
        dynamoDBMapper.save(user,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("id",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(user.getId())
                                )));
        return user;
    }
}
