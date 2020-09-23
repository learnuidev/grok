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
 - GET    => Browse a list of decks

## Cards
/api/users/:user-id/decks/:deck-id/cards/:card-id
 - GET    => get a single cards by ID belonging to a certain deck
 - PUT    => Update a card
 - DELETE => Delete a card

/api/users/:user-id/decks/:deck-id/cards
 - POST   => Post a new card
 - GET    => Browse a list of cards

---

# Step: 1 Data Layer

## User
- Create (done)
- Read   (done)
- Update (done)
- Delete (done)

## Decks
- List   (done)
- Read   (done)
- Create (done)
- Update (done)
- Delete (done)

## Cards
- List
- Read
- Create
- Update
- Delete
---

Before we go ahead and implement CRUD functions for Decks and Cards, lets talk about run time state

What is run time state?
Environment variables, database connection are considered run time state.
- Run time state can dependent on each other. For example in order to create database connection, you need database-uri, which comes from the env variables.
- Currently our application only has two run time state.
- As our application grows, so will our run time state.
- Since more state => more complexity. We need to make sure to tame it

Luckily clojure provides with libraries such as component, mount, integrant to manage these run time state.

In this case, I will be using mount.

Mount has two basic function
1. start -> which starts the state
2. stop  -> stop which stops the state

You can also decided which resource to start, or which resources to omit when starting mount for example

In order to use convert run time state/resource -> mount state, we have to use the macro defstate provided by mount.

Another benefit mount provide is that it enables reloaded workflow... lets much easier to explain with code... so lets get started with mount

## Step 1: Install dependency

## Step 2: Restart REPL to install dependency so that we can use it

## Step 3: Start using mount

## Step 4: Use mount start function to start life cycle of the application

We will install one more resource to start namespaces of our application
