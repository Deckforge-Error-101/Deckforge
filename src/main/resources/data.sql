INSERT INTO Users (username, email, password, roleType)
VALUES ('AdminTobias', 'admin@deckforge.dk', '$2a$12$5pyBW/v1LCOuq5bO24Vj7uXnvWUWhwKHYNVDSBkQS3XuQAoel0pba', 'ADMIN'),
       ('SpillerMads', 'mads@mail.dk', '$2a$12$5pyBW/v1LCOuq5bO24Vj7uXnvWUWhwKHYNVDSBkQS3XuQAoel0pba', 'USER'),
       ('ManagerMette', 'mette@deckforge.dk', '$2a$12$5pyBW/v1LCOuq5bO24Vj7uXnvWUWhwKHYNVDSBkQS3XuQAoel0pba', 'MANAGER');

INSERT INTO Cards (cardName, typeId, rarity, setType)
VALUES ('Black Lotus', 'ARTIFACT', 'MYTHIC', 'SET_2005'),
       ('Pikachu', 'CREATURE', 'UNCOMMON', 'SET_2010'),
       ('Island', 'LAND', 'COMMON', 'SET_2005'),
       ('Fireball', 'SORCERY', 'UNCOMMON', 'SET_2005'),
       ('Counterspell', 'INSTANT', 'RARE', 'SET_2020'),
       ('Jace, the Mind Sculptor', 'PLANESWALKER', 'MYTHIC', 'SET_2015');

INSERT INTO Events (title, eventType, capacity, statusType)
VALUES ('Fredags Magic', 'CASUAL_EVENT', 16, 'OPEN'),
       ('Store Commander Turnering', 'COMMANDER_EVENT', 32, 'OPEN'),
       ('Aftenhygge i Butikken', 'CASUAL_EVENT', 8, 'FULL');

INSERT INTO Decks (deckName, formatType, slots, userId)
VALUES ('Mads Lyn-Deck', 'STANDARD', 60, 1),
       ('Mads Commander King', 'COMMANDER', 100, 1),
       ('Mohammeds AutoSmasher', 'STANDARD', 100, 2),
       ('Patricks Flyvedeck', 'COMMANDER', 100, 3);

INSERT INTO Collections (userId, cardId)
VALUES (2, 2),
       (2, 3),
       (2, 6),
       (1, 1);

INSERT INTO EventRegistrations (eventId, userId, deckId, registrationDate)
VALUES (1, 2, 1, '2026-05-08');