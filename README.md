# Grok

Grok is a learning tool that allows users to learn different facts about different topics

In this tutorial series we will be builing a limited version of Anki, using the following technologies:

- Clojure
- Datomic
- ClojureScript
- Re-Frame
- DataScript (via Re-Posh)

# Information Model

## User
 - id        (uuid)
 - full-name (string)
 - username  (string)
 - email     (string => unique)
 - password  (string => hashed)
 - token     (string)

## Decks
 - id        (uuid)
 - author    (Ref)
 - title     (string)
 - tags      (vector of strings)

## Cards
 - id              (uuid)
 - deck            (REF)
 - front           (string)
 - back            (string)
 - progress        (long)
 - next-study-date (date|instant)

# HTTP Rest End Points

Now that we are aware of our data model, lets look at how we will design/structure our rest endpoints

## Auth
/api/login
 - POST => login a user
/api/register
 - POST => register a user

## Users
/api/users/:user-id
 - GET    => get a single user by ID
 - PUT    => Update a user
 - DELETE => Delete a single user

/api/users
 - POST   => Post a new user

## Decks
/api/users/:user-id/decks/:deck-id
 - GET    => get a single deck by ID belonging to a certain user
 - PUT    => Update a deck
 - DELETE => Delete a deck

/api/users/:user-id/decks
 - POST   => Post a new deck

## Cards
/api/users/:user-id/decks/:deck-id/cards/:card-id
 - GET    => get a single cards by ID belonging to a certain deck
 - PUT    => Update a card
 - DELETE => Delete a card

/api/users/:user-id/decks/:deck-id/cards
 - POST   => Post a new card
