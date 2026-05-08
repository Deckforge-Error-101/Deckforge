package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Collection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionRepository implements ICollectionRepository {
    private final JdbcTemplate jdbcTemplate;

    public CollectionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createCollection(Collection collection) {

    }

    @Override
    public void deleteCollection(Collection collection) {

    }

    @Override
    public void updateCollection(Collection collection) {

    }
}
