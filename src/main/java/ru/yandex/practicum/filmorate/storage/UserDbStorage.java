package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    Map<Integer, User> ls = new HashMap<>();
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getLsUsers() {
        ls.clear();
        List<User> ls = jdbcTemplate.queryForObject("select u.id as id_user, u.email, u.login, u.name," +
                " u.birthday, u2.id as id_friend, u2.email as email_friend, u2.login as login_friend, " +
                "u2.name as name_friend, u2.birthday as birthday_friend  from users u left join friends f on f.user_id =u.id " +
                "left join users u2 on u2.id =f.friend_id", userRowMapper());
        return ls;
    }

    @Override
    public User getUser(int id) {
        ls.clear();
        return jdbcTemplate.queryForObject("select u.id as id_user, u.email, u.login, u.name," +
                " u.birthday, u2.id as id_friend, u2.email as email_friend, u2.login as login_friend, " +
                "u2.name as name_friend, u2.birthday as birthday_friend  from users u left join friends f on f.user_id =u.id " +
                "left join users u2 on u2.id =f.friend_id where u.id=?", userRowMapper(), id).get(0);
    }

    @Override
    public User postUser(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = Map.of("name", user.getName(), "email", user.getEmail(),
                "login", user.getLogin(), "birthday", user.getBirthday());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("update users set email = ?, login = ?, name = ?, birthday = ? where id = ?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteUser(int id) {
        jdbcTemplate.update("delete from users where id =?", id);
    }

    private RowMapper<List<User>> userRowMapper() {
        return (rs, rowNum) -> {
            do {
                if (!ls.containsKey(rs.getInt("id_user"))) {
                    User user = new User(
                            rs.getInt("id_user"),
                            rs.getString("email"),
                            rs.getString("login"),
                            rs.getString("name"),
                            rs.getDate("birthday").toLocalDate()
                    );
                    ls.put(user.getId(), user);
                }
                if (ls.containsKey(rs.getInt("id_user")) && rs.getString("email_friend") != null) {
                    User friend = new User(
                            rs.getInt("id_friend"),
                            rs.getString("email_friend"),
                            rs.getString("login_friend"),
                            rs.getString("name_friend"),
                            rs.getDate("birthday_friend").toLocalDate());
                    ls.get(rs.getInt("id_user")).addFriend(friend);
                }
            } while (rs.next());
            List<User> ls1 = new ArrayList<>();
            for (User o : ls.values()) {
                ls1.add(o);
            }
            return ls1;
        };
    }

    @Override
    public void addFriend(int us1, int us2) {
        jdbcTemplate.update("insert into friends (user_id, friend_id) values(?,?)", us1, us2);
    }

    @Override
    public void delFriend(int us1, int us2) {
        jdbcTemplate.update("delete from friends where user_id =? and friend_id=?", us1, us2);
    }


}
