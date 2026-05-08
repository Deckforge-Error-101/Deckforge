INSERT INTO Users (username, email, password, roleType)
VALUES ('AdminTobias', 'admin@deckforge.dk', 'hash_1234', 'ADMIN'),
       ('SpillerMads', 'mads@mail.dk', 'hash_5678', 'USER'),
       ('ManagerMette', 'mette@butik.dk', 'hash_9999', 'MANAGER');

INSERT INTO Cards (cardName, typeId, rarity)
VALUES ('Black Lotus', 'ARTIFACT', 'LEGENDARY'),
       ('Pikachu', 'CREATURE', 'UNCOMMON'),
       ('Island', 'LAND', 'COMMON'),
       ('Fireball', 'SORCERY', 'UNCOMMON'),
       ('Counterspell', 'INSTANT', 'RARE'),
       ('Jace, the Mind Sculptor', 'PLANEWALKER', 'LEGENDARY');

INSERT INTO Events (title, eventType, capacity, statusType)
VALUES ('Fredags Magic', 'CASUAL_EVENT', 16, 'OPEN'),
       ('Store Commander Turnering', 'COMMANDER_EVENT', 32, 'OPEN'),
       ('Aftenhygge i Butikken', 'CASUAL_EVENT', 8, 'FULL');

INSERT INTO Decks (deckName, formatType, slots, userId)
VALUES ('Mads Lyn-Deck', 'STANDARD', 60, 2),
       ('Mads Commander King', 'COMMANDER', 100, 2);

INSERT INTO Collections (userId, cardId, tradeId)
VALUES (2, 2, 'TRADE-111'),
       (2, 3, 'TRADE-222'),
       (2, 6, 'TRADE-333'),
       (1, 1, 'TRADE-444');

INSERT INTO EventRegistrations (eventId, userId, deckId, registrationDate)
VALUES (1, 2, 1, '2026-05-08');