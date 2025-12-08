# README
Aisha Maul 20612918

---

## Table of Contents

1. [GitHub](#1-github)
2. [Compilation Instructions](#2-compilation-instructions)
3. [Implemented and Working Properly](#3-implemented-and-working-properly)
    * 3.1 [Game Modes](#31-game-modes)
    * 3.2 [Advanced Rendering & Visuals](#32-advanced-rendering--visuals)
    * 3.3 [Gameplay Mechanics](#33-gameplay-mechanics)
    * 3.4 [Audio System](#34-audio-system)
    * 3.5 [Menus and Navigation](#35-menus-and-navigation)
4. [Implemented but Not Working Properly](#4-implemented-but-not-working-properly)
5. [Features Not Implemented](#5-features-not-implemented)
6. [New Java Classes](#6-new-java-classes)
    * 6.1 [Game Logic and Modes](#61-game-logic-and-modes)
    * 6.2 [Core Game Mechanics](#62-core-game-mechanics)
    * 6.3 [UI Controllers and Menus](#63-ui-controllers-and-menus)
    * 6.4 [Rendering and Visuals](#64-rendering-and-visuals)
    * 6.5 [Input, Audio, and Utilities](#65-input-audio-and-utilities)
    * 6.6 [Bricks and Data Models](#66-bricks-and-data-models)
7. [Modified Java Classes](#7-modified-java-classes)
8. [Unexpected Problems](#8-unexpected-problems)

---

## 1. GitHub
https://github.com/aishamaul/CW2025.git


 
## 2. Compilation Instructions

To compile and run the application, ensure the following are installed:
* **Java Development Kit (JDK):** Version 23 or higher
* **Apache Maven:** Version 3.8+

**Dependencies (Managed via Maven):**
* JavaFX Controls/FXML/Media: 21.0.6
* JUnit Jupiter (Testing): 5.12.1

### Steps to Run:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/aishamaul/CW2025.git
    cd CW2025
    ```

2.  **Build the project:**
    ```bash
    mvn clean install
    ```

3.  **Run the application:**
    ```bash
    mvn javafx:run
    ```


*Note: If you do not have Maven installed globally, you can use the included wrapper script by replacing mvn with ./mvnw (Mac/Linux) or mvnw (Windows).*


## 3. Implemented and Working Properly

### 3.1 Game Modes

* **Classic Mode**
    * Standard Tetris gameplay with speed increase rate of 0.5 for every 5 lines cleared.
    * Implemented infinite progression logic in `GameProgressionManager.java`.

* **Challenge Mode (Levels 1-3)**
    * A progression system implemented using the `GameMode` interface.
    * **Level 1:** Implemented in `ChallengeLevel1.java`. Speed increases every 5 lines cleared with a rate increase of 0.6. Transition to Level 2 is triggered upon reaching a threshold of 20 cleared lines.
    * **Level 2:** Implemented in `ChallengeLevel2.java`. Introduced a rising garbage row every 15 ticks that pushes the gameplay area upwards. Introduced a “Time Freeze” mechanic where gravity stops for 10 seconds after 10 lines are cleared. Transition to Level 3 is triggered upon reaching a threshold of 15 cleared lines.
    * **Level 3:** Implemented in `ChallengeLevel3`. The board initializes with the bottom 12 rows filled with garbage blocks containing random holes (implemented in `GarbageRowFactory.java`). Introduces a custom `Level3BrickGenerator` that spawns a Bomb Brick. Landing this brick triggers a 3x3 explosion, clearing surrounding blocks. Win condition is satisfied when the line clear counter reaches 25

### 3.2 Advanced Rendering & Visuals

* **Particles and Animations**
    * Encapsulated in `GridAnimator.java`.
    * **Explosion effects:** Added `animateExplosion` method which spawns particles that fly outwards from the center of the bomb brick in Level 3.
    * **Board shake:** Added `animateBoardShake` method to translate the `gamePanel` Y-axis briefly when a brick lands hard.
    * **Hard drop splash:** Added `spawnSplashParticles` method to generate small rectangles at the impact coordinates in `GameRenderer.java`.

* **Dynamic Background**
    * Created the `BackgroundAnimator` class. It attaches a `StackPane` with a `RadialGradient` and generates floating Circle particles that move using Timeline animations.

* **Ghost Brick**
    * Implemented prediction logic in `ActivePiece.java` via the `calculateGhostY` method. This calls `grid.calculateDropPosition` to find the lowest valid Y-coordinate before a collision occurs.

* **Interactive Pause Buttons**
    * Added `PauseButtonAnimator` class. Applies a neon glow effect (using `DropShadow`) that changes intensity and spread dynamically when the user hovers over or presses the button.

* **Title Effects**
    * Added `TitleAnimator.java` that applies a continuous "rainbow" gradient shift and a breathing glow effect to the main logo.

* **Victory Effects**
    * Added `ChallengeMenuAnimator` that rewards the player upon completing levels. It renders a "Falling Bricks" animation for level transitions and a "Confetti Explosion" effect when the entire challenge mode is beaten.

### 3.3 Gameplay Mechanics

* **Hold Brick:** Implemented `HoldManger.java` class to store the `heldBrick` state. Integrated into `SimpleBoard.java` and bound to the 'H' key in `InputHandler.java`.
* **Line Cleared Tracking:** Implemented in `LineClearProcessor.java` to track the number of lines cleared.
* **Next Brick Panel:** Previews and displays the next 3 bricks in the queue. Implemented in `GuiController.java` and `GameRenderer.java`.

### 3.4 Audio System

* Added `SoundManager.java`, a singleton class managing all audio, including event-driven sound effects (move, rotate, explosion, level up, etc.).
* Added `BackgroundMusicManager.java` to handle the looping media player for the background track.

### 3.5 Menus and Navigation

* Fully functional FXML-based menus for Home, Play Selection, Pause, Controls, and Game Over screens, managed by `SceneNavigator.java` and `GameFlowCoordinator.java`.
* **Controls Overlay:** Added `ControlsMenuLoader.java` that loads the controls screen as an overlay on top of the existing game view. It handles custom back navigation logic to ensure that when the user clicks "Back," they are returned exactly to the state they left.


## 4. Implemented but Not Working Properly

* **Hold Brick Mechanics**
    * *Issue:* The `HoldManager.java` class allows the user to swap the held brick. However, standard Tetris rules only allow one swap per turn. The current implementation performs the swap but does not set a lock flag to prevent subsequent swaps on the same active piece. This allows the player to stall indefinitely by repeatedly pressing 'H'.

* **Rotation System**
    * *Issue:* The rotation logic in `BrickRotator.java` handles basic collisions but fails in tight spaces. While it implements "Wall Kicks" (horizontal kicks: offsets 0, 1, -1, 2, -2), it does not implement vertical kicks. Rotations might fail in tight vertical spaces or T-spin setups where standard Super Rotation System (SRS) rules would allow the piece to "kick" upward or downward.

* **Window Scaling**
    * *Issue:* To ensure the game looks good on any screen size, `WindowScaler.java` was implemented. It binds a Scale transform to the root pane's dimensions, automatically resizing and centering the game content. However, it currently only works for the homescreen, pause menu, and play menu, but not for gameplay.


## 5. Features Not Implemented

* **Highscore Leaderboard**
    * A system to track and display highscores for classic mode and challenge mode with player name input. This was left out because saving data to a file adds significant complexity, such as handling file errors (I/O exceptions) and data corruption. Additionally, it would necessitate a new UI view and logic to sort/filter scores.

* **User Configuration Menu**
    * A menu allowing users to customize game settings (game speed, visual themes, audio volumes). This was omitted because currently, settings like background music volume are set as fixed constants. Making these changeable would require rewriting classes to stop using constants and start listening for updates, which was deemed too risky late in the project.

* **Custom Key Bindings**
    * Allowing players to choose their own keys for movement. The input logic is currently hardcoded inside `InputHandler.java` using a simple switch statement. Making this customizable would require a new system to map variable keys to actions dynamically.


## 6. New Java Classes

### 6.1 Game Logic and Modes

| Class Name | Description | Location |
| :--- | :--- | :--- |
| **GameMode** | Interface defining the contract for game levels (win conditions, speed rules, overlays). | `com.comp2042.game.mode` |
| **ChallengeLevel1** | Implements Level 1 logic: clears 20 lines to win, speed increases by 0.6x every 5 lines. | `com.comp2042.game.mode` |
| **ChallengeLevel2** | Implements Level 2 logic: manages the "Time Freeze" timer and rising garbage row mechanics. | `com.comp2042.game.mode` |
| **ChallengeLevel3** | Implements Level 3 logic: initializes the board with garbage bricks and handles Bomb Brick explosions. | `com.comp2042.game.mode` |

### 6.2 Core Game Mechanics

| Class Name | Description | Location |
| :--- | :--- | :--- |
| **GameLifecycleManager** | Manages high-level turn sequences: locking pieces, triggering clears/explosions, and spawning next bricks. | `com.comp2042.game.core` |
| **GameLoopManager** | Wraps the JavaFX Timeline to provide pause, play, and speed adjustment controls for the game tick. | `com.comp2042.game.core` |
| **GameProgressionManager** | Monitors line clears and updates game speed based on the active GameMode. | `com.comp2042.game.core` |
| **HoldManager** | Manages the logic for swapping and storing the held brick. | `com.comp2042.game.core` |
| **ExplosionHandler** | Coordinates the detection and execution of bomb explosions on the board. | `com.comp2042.game.core` |
| **LineClearProcessor** | Handles row detection, scoring updates, and triggers the line clear animation sequence. | `com.comp2042.game.core` |
| **GarbageRowFactory** | Generates randomized "garbage" row arrays with guaranteed holes for Level 3. | `com.comp2042.game.core` |
| **ActivePiece** | Represents the currently falling brick, handling its movement and rotation state. | `com.comp2042.game.core` |
| **BoardViewDataFactory** | Creates immutable ViewData snapshots to safely pass game state to the UI renderer. | `com.comp2042.game.core` |
| **GameInitializer** | Utility class that wires up the Board, View, and Controller dependencies at startup. | `com.comp2042.game.core` |
| **ScoreEvaluator** | Calculates points for movement, hard drops, and line clears. | `com.comp2042.game.scoring` |
| **RowScoreCalculator** | Implements the non-linear scoring formula ($50 \times lines^2$). | `com.comp2042.game.scoring` |

### 6.3 UI Controllers and Menus

| Class Name | Description | Location |
| :--- | :--- | :--- |
| **HomeController** | Controls the main menu screen and its navigation buttons. | `com.comp2042.ui.view.menu` |
| **PlayMenuController** | Controls the mode selection screen (Practice, Classic, Challenge). | `com.comp2042.ui.view.menu` |
| **LevelMenuController** | Manages the overlays for Level Start, Level Complete, and Challenge Victory screens. | `com.comp2042.ui.view.menu` |
| **GameControlsController** | Displays the key bindings and instructions to the user. | `com.comp2042.ui.view.menu` |
| **ControlsMenuLoader** | Helper class to load the controls screen as an overlay on top of the pause menu. | `com.comp2042.ui.view` |
| **GameFlowCoordinator** | Orchestrates transitions between the game loop, menus, and level progression. | `com.comp2042.ui.view` |
| **GameRuntimeManager** | Manages the active game session state (Pause, Game Over properties). | `com.comp2042.ui.view` |
| **GameViewEventDelegate** | Handles complex UI events like game-over sequences and animation callbacks. | `com.comp2042.ui.view` |
| **GameViewAdapter** | Adapts the GameView interface to the concrete UI implementation. | `com.comp2042.ui.view` |
| **LevelStartManager** | Handles the logic for showing the start screen overlay before a level begins. | `com.comp2042.ui.view` |
| **PauseStateManager** | Manages the toggle logic for the pause menu and fullscreen synchronization. | `com.comp2042.ui.view` |
| **AnimationCoordinator** | Ensures the game loop remains paused while animations (like line clears) are playing. | `com.comp2042.ui.view` |
| **FreezeOverlayManager** | Controls the "Time Freeze" countdown and text overlay for Level 2. | `com.comp2042.ui.view` |
| **SceneNavigator** | Utility for switching between different FXML scenes (Home, Game, etc.). | `com.comp2042.ui.view` |
| **WindowScaler** | Handles responsive scaling of the game content to fit the window size. | `com.comp2042.ui.view` |

### 6.4 Rendering and Visuals

| Class Name | Description | Location |
| :--- | :--- | :--- |
| **GameRenderer** | Draws the board grid, active brick, ghost brick, and next/hold queues using JavaFX Rectangles. | `com.comp2042.ui.render` |
| **GridAnimator** | Handles gameplay animations: line clear flashes, block explosions, and board shake. | `com.comp2042.ui.render` |
| **BackgroundAnimator** | Creates the dynamic background with gradient shifts and floating particles. | `com.comp2042.ui.render` |
| **TitleAnimator** | Applies rainbow gradients and glow effects to the main title text. | `com.comp2042.ui.render` |
| **BrickStyler** | Applies CSS styles to bricks based on their color ID. | `com.comp2042.ui.render` |
| **ParticleAnimatorHelper** | Utility for creating generic move and fade particle effects. | `com.comp2042.ui.render` |
| **NotificationManager** | Manages the creation and lifecycle of floating score popups. | `com.comp2042.ui.components` |
| **NotificationAnimator** | Specific animator for the floating score text. | `com.comp2042.ui.components` |
| **PauseButtonAnimator** | Adds interactive neon glow effects to the pause button. | `com.comp2042.ui.components` |
| **MenuButtonAnimator** | Applies pulsing and glow animations to main menu buttons. | `com.comp2042.ui.view.menu` |
| **ChallengeMenuAnimator** | Renders "Falling Bricks" and "Confetti" effects for level completion screens. | `com.comp2042.ui.view.menu` |

### 6.5 Input, Audio, and Utilities

| Class Name | Description | Location |
| :--- | :--- | :--- |
| **InputHandler** | Listens for raw JavaFX KeyEvents and triggers game actions. | `com.comp2042.ui.input` |
| **InputController** | Binds the InputHandler to the scene's root pane. | `com.comp2042.ui.input` |
| **EventDispatcher** | Dispatches events from the input layer to the GameController. | `com.comp2042.ui.input` |
| **SoundManager** | Singleton managing AudioClip resources for sound effects (move, rotate, win). | `com.comp2042.util.audio` |
| **BackgroundMusicManager** | Singleton managing the looping background music player. | `com.comp2042.util.audio` |
| **ExplosionManager** | Calculates the affected coordinates for a radial explosion on the grid. | `com.comp2042.util` |
| **RowClearer** | Logic for detecting full rows and generating a new board matrix. | `com.comp2042.util` |
| **RowClearingOutput** | DTO containing results of a row clear check (lines removed, new matrix). | `com.comp2042.util` |

### 6.6 Bricks and Data Models

| Class Name | Description | Location |
| :--- | :--- | :--- |
| **Level3BrickGenerator** | Custom generator that includes the BombBrick in the spawn pool. | `com.comp2042.game.bricks` |
| **BombBrick** | Special brick type that triggers explosions. | `com.comp2042.game.bricks` |
| **GameConfig** | Central file for static constants (board size, speeds, delays). | `com.comp2042.game.config` |


## 7. Modified Java Classes

| Class Name | Changes Made | Reason for Modification | Location |
| :--- | :--- | :--- | :--- |
| **GameController** | Removed game loop logic. Delegated turn-end logic. | The controller originally managed the Timeline directly. Moved to `GameLifecycleManager` to adhere to Single Responsibility Principle. Separated game rules from input handling. | `com.comp2042.game.core` |
| **SimpleBoard** | Replaced `int[][]` with `BoardGrid`. Injected `BrickGenerator`. Delegated scoring logic. | Encapsulated low-level array manipulation in `BoardGrid`. Allowed swapping generation strategies for testing. Moved scoring math to `RowScoreCalculator`. | `com.comp2042.game.core` |
| **GuiController** | Moved UI updates to `GameUIManager`. Extracted pause logic. | The controller was a "Blob" class. Delegated visual tasks to `GameUIManager` and extracted pause logic to `PauseStateManager` to centralize state transitions. | `com.comp2042.ui.view` |
| **Main** | Moved setup to `GameInitializer`. | The start method was overly complex. Extracted wiring logic to `GameInitializer` to keep the entry point clean. | `com.comp2042` |
| **BrickRotator** | Updated to use `BoardGrid`. | Narrowed dependency to just `BoardGrid` (Dependency Inversion), as it only needs to check grid cells, not the full game state. | `com.comp2042.game.core` |
| **Score** | Used `IntegerProperty`. | Switched to JavaFX `IntegerProperty` to enable Data Binding, ensuring the UI updates automatically without manual refresh calls. | `com.comp2042.game.scoring` |


## 8. Unexpected Problems

* **Workflow Sequencing & Architectural Dependency**
    * *Issue:* Initially, I attempted to implement the "Additions" before Refactoring. I found that adding complex features on top of the original, tightly coupled classes resulted in fragile code that was difficult to debug and hard to test.
    * *Resolution:* I halted development on new features and reverted the branch. I refactored the base code first, then started on my additional features.


* **Time Constraints & Scope Management**
    * *Issue:* The scope of implementing three Challenge Levels plus a full architectural refactor was too large for the timeframe, delaying features like High Scores.
    * *Resolution:* I applied Project Scope Management by prioritizing the stability of the core gameplay and visual polish over auxiliary features.
