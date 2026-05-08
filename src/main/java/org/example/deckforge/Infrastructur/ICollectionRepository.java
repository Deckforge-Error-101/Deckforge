package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Collection;

public interface ICollectionRepository {
    void createCollection(Collection collection);
    void deleteCollection(Collection collection);
    void updateCollection(Collection collection);
}
