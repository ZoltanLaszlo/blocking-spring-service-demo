package hu.upscale.spring.demo.repository;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

/**
 * @author László Zoltán
 */
public class PascalCaseNamingStrategy extends SpringPhysicalNamingStrategy {

    @Override
    protected Identifier getIdentifier(String name, boolean quoted, JdbcEnvironment jdbcEnvironment) {
        String pascalCasedName = Arrays.stream(name.split("_"))
            .filter(Objects::nonNull)
            .filter(Predicate.not(String::isBlank))
            .map(String::trim)
            .collect(Collectors.joining());

        return super.getIdentifier(pascalCasedName, quoted, jdbcEnvironment);
    }

    @Override
    protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
        return false;
    }
}
