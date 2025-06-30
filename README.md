# Solo Red Game - Java Implementation

## Overview

This project is a text-based Java implementation of a turn-based strategy game called **Solo Red**. The game mechanics follow rules where a player (Solo Red) attempts to eliminate all enemies on a board by making a series of valid moves. This project emphasizes object-oriented programming (OOP) principles, design patterns, and thorough unit testing.

## Features

- **Modular Game Design** using interfaces and abstract classes for reusability and flexibility.
- **Text-Based Game View** to interact with the game in a console environment.
- **Game Controller** to handle user input and control game flow.
- **Fully Tested Game Logic** through comprehensive JUnit tests.

## Project Structure

### Models

- `AbstractGameModel`: A base class providing shared logic and structure for various game models.
- `SoloRedGameModelImpl`: The core game logic implementation specific to Solo Red.
- `AdvancedSoloRedGameModel`: A more complex or extended version of the basic game model.

### View and Controller

- `SoloRedGameTextView`: A view class responsible for rendering the game state in a human-readable format.
- `SoloRedTextController`: Manages game progression based on player input and game state.

### Test Suite

Includes robust unit tests to ensure correct functionality and behavior of all components:
- `AbstractGameModelTest`
- `SoloRedGameModelImplTest`
- `SoloRedGameModelTest`
- `AdvancedSoloRedGameModelTest`
- `SoloRedGameTextViewTest`
- `SoloRedTextControllerTest`

These tests cover:
- Game initialization and configuration
- Move logic and board state updates
- Input handling and controller interactions
- Edge case handling and error management

## Technologies

- Java 17+
- JUnit 4 for testing

## How to Run

1. Clone the repository.
2. Compile the code using your IDE or command line
3. Run the controller to start a sample game session
