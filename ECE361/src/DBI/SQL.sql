BEGIN;

DROP DATABASE SetGame;
CREATE DATABASE SetGame;
USE SetGame;

CREATE TABLE Users (
    username VARCHAR(20),
    password VARCHAR(40),
    games_played int DEFAULT 0,
    games_won int DEFAULT 0,
    games_lost int DEFAULT 0,
    games_tied int DEFAULT 0,
    games_quit int DEFAULT 0,
    sets_submitted int DEFAULT 0,
    sets_correct int DEFAULT 0,
    PRIMARY KEY (username)
);


INSERT INTO Users(username, password) VALUES ('Andrew',SHA1('andrew'));
INSERT INTO Users(username, password) VALUES ('Jordan',SHA1('jordan'));
INSERT INTO Users(username, password) VALUES ('Victor',SHA1('victor'));
INSERT INTO Users(username, password) VALUES ('Jason',SHA1('jason'));
INSERT INTO Users(username, password) VALUES ('Test',SHA1('test'));
INSERT INTO Users(username, password) VALUES ('lol',SHA1('lol'));
INSERT INTO Users(username, password) VALUES ('test2',SHA1('test2'));

COMMIT;
