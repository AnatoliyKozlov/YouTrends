package system.access;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.mutable.MutableBoolean;
import system.shared.User;

@Log4j2
public class UserDAO extends AbstractDAO
{
    public UserDAO(DataSource dataSource)
    {
        super(dataSource);
    }

    public void registerUser(User user)
    {
        String insertQuery = "INSERT INTO Users (id, firstName, lastName, userName, language, isBot) " +
                             "VALUES (?, ?, ?, ?, ?, ?)";

        try
        {
            if (!isUserRegistered(user))
            {
                jdbcTemplate.update(insertQuery,
                                    user.getId(),
                                    user.getFirstName(),
                                    user.getLastName(),
                                    user.getUserName(),
                                    user.getLanguageCode(),
                                    user.getBot());
            }
        }
        catch (Exception e)
        {
            log.error("Error", e);
        }
    }

    public boolean isUserRegistered(User user)
    {
        String sql = "SELECT EXISTS (SELECT * FROM Users WHERE id = ?)";

        try
        {
            MutableBoolean isUserExist = new MutableBoolean(false);

            jdbcTemplate.query(sql,
                               resultSet ->
                               {
                                    isUserExist.setValue(resultSet.getBoolean(1));
                               },
                               user.getId());

           return isUserExist.getValue();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void subscribeUser(User user)
    {
        String updateQuery = "UPDATE Users SET isSubscribe = TRUE WHERE id = ?";

        try
        {
            jdbcTemplate.update(updateQuery, user.getId());
        }
        catch (Exception e)
        {
            log.error("Error", e);
        }

        log.info("Subscription: " + user.getId());
    }

    public void unsubscribeUser(User user)
    {
        String updateQuery = "UPDATE Users SET isSubscribe = FALSE WHERE id = ?";

        try
        {
            jdbcTemplate.update(updateQuery, user.getId());
        }
        catch (Exception e)
        {
            log.error("Error", e);
        }

        log.info("Unsubscribing: " + user.getId());
    }

    public List<User> getSubscribeUsers()
    {
        List<User> users = new ArrayList<>();

        String query = "SELECT * FROM Users WHERE isSubscribe = TRUE";

        try
        {
            jdbcTemplate.query(query, result ->
            {
                User user = new User();

                user.setId(result.getInt("id"));
                user.setFirstName(result.getString("firstName"));
                user.setLastName(result.getString("lastName"));
                user.setUserName(result.getString("userName"));
                user.setLanguageCode(result.getString("language"));
                user.setBot(result.getBoolean("isBot"));
                user.setBanned(result.getBoolean("isBanned"));
                user.setSubscribe(result.getBoolean("isSubscribe"));

                users.add(user);
            });

            return users;
        }
        catch (Exception e)
        {
            log.error("Error", e);
        }

        return users;
    }

    private List<String> getBannedChannels(String chatId)
    {
        String query = "SELECT channelId FROM UserBannedChannel WHERE userId = ?";

        List<String> bannedChannels = new ArrayList<>();

        try
        {
            jdbcTemplate.query(query, result ->
            {
                bannedChannels.add(result.getString("channelId"));
            }, Long.valueOf(chatId));
        }
        catch (Exception e)
        {
            log.error("Error", e);
        }

        return bannedChannels;
    }

    private List<String> getBannedTags(String chatId)
    {
        String query = "SELECT tagId FROM UserBannedTag WHERE userId = ?";

        List<String> bannedTags = new ArrayList<>();

        try
        {
            jdbcTemplate.query(query, result ->
            {
                bannedTags.add(result.getString("tagId"));
            }, Long.valueOf(chatId));
        }
        catch (Exception e)
        {
            log.error("Error", e);
        }

        return bannedTags;
    }
}
