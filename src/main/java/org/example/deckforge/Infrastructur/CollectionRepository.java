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
        String sql = "insert into collections (userid, tradeid, cardid) values (?, ?, ?)";

        jdbcTemplate.update(sql,
                collection.getUserId(),
                collection.getCardId(),
                collection.getTradeId()
        );

    }

    @Override
    public void deleteCollection(Collection collection) {
        String sql ="DELETE FROM  collections WHERE userid=?";

        jdbcTemplate.update(sql, collection.getUserId());
    }

    @Override
    public void updateCollection(Collection collection) {
        String sql = "UPDATE  collections SET tradeid=?, cardid=? WHERE collectionid=?";

        jdbcTemplate.update(sql, collection.getTradeId(), collection.getCardId(), collection.getCollectionId());
    }
}
