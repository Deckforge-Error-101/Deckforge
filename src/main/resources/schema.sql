DROP TABLE IF EXISTS EventRegistrations;
DROP TABLE IF EXISTS Collections;
DROP TABLE IF EXISTS deck_cards;
DROP TABLE IF EXISTS Decks;
DROP TABLE IF EXISTS Events;
DROP TABLE IF EXISTS Cards;
DROP TABLE IF EXISTS Users;


CREATE TABLE Events
(
    eventId    INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(50) NOT NULL,
    eventType  ENUM('CASUAL_EVENT', 'COMMANDER_EVENT') NOT NULL,
    capacity   INT         NOT NULL,
    statusType ENUM('OPEN', 'FULL', 'CLOSED')
);

CREATE TABLE Users
(
    userId   INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL,
    email    VARCHAR(80)  NOT NULL,
    password VARCHAR(255) NOT NULL,
    roleType ENUM('USER', 'ADMIN', 'MANAGER') DEFAULT 'USER'
);

CREATE TABLE Decks
(
    deckId     INT AUTO_INCREMENT PRIMARY KEY,
    deckName   VARCHAR(50) NOT NULL,
    formatType ENUM('STANDARD', 'COMMANDER') NOT NULL,
    slots      INT         NOT NULL,
    is_public BOOLEAN DEFAULT FALSE,

    userId     INT,
    FOREIGN KEY (userId)
        REFERENCES Users (userId)
        ON DELETE CASCADE
);

CREATE TABLE Cards
(
    cardId   INT AUTO_INCREMENT PRIMARY KEY,
    cardName VARCHAR(50) NOT NULL,
    typeId   ENUM('CREATURE', 'LAND', 'ENCHANTMENT', 'PLANESWALKER', 'SORCERY', 'INSTANT', 'ARTIFACT', 'TRIBAL') NOT NULL,
    image    LONGBLOB,
    rarity   ENUM('RARE', 'UNCOMMON', 'COMMON', 'MYTHIC')
);

CREATE TABLE collections
(
    collectionId INT AUTO_INCREMENT PRIMARY KEY,

    userId       INT,
    FOREIGN KEY (userId)
        REFERENCES users (userId)
        ON DELETE CASCADE,

    tradeId      VARCHAR(50) UNIQUE NULL,

    cardId       INT,
    FOREIGN KEY (cardId)
        REFERENCES cards (cardId)
        ON DELETE CASCADE,

    quantity     INT DEFAULT 1,
    CONSTRAINT uniqueUserCard UNIQUE (userId, cardId)
);

CREATE TABLE EventRegistrations
(
    registrationId   INT AUTO_INCREMENT PRIMARY KEY,

    eventId          INT,
    FOREIGN KEY (eventId)
        REFERENCES Events (eventId)
        ON DELETE CASCADE,

    userId           INT,
    FOREIGN KEY (userId)
        REFERENCES Users (userId)
        ON DELETE CASCADE,

    deckId           INT NULL,
    FOREIGN KEY (deckId)
        REFERENCES Decks (deckId)
        ON DELETE CASCADE,

    registrationDate DATE NOT NULL
);

CREATE TABLE Deck_Cards
(
    deckId   INT NOT NULL,
    cardId   INT NOT NULL,
    quantity INT DEFAULT 1,
    PRIMARY KEY (deckId, cardId),
    FOREIGN KEY (deckId) REFERENCES Decks (deckId) ON DELETE CASCADE,
    FOREIGN KEY (cardId) REFERENCES Cards (cardId)
);