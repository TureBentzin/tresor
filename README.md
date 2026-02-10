# Tresor

**Tresor** is a GUI TTY interface for the **Befator Banking API 2**, which is expected to be released in **February 2025
**.

## Tresor Server

The **Tresor Server** is a Java application that implements the GUI and allows **Tresor Displays** (or compatible Telnet
clients) to connect via a socket and interact with individual windows.

## Idea behind Tresor

The primary idea behind Tresor is to use a Command-Line Interface (CLI) over Telnet to reduce the computational load on
LUA-based thin clients. This approach leverages the simplicity and efficiency of Telnet communication to minimize the
processing requirements on client devices.

Tresor is designed to work seamlessly with TSOCK, which is a protocol that enables Telnet communication over HTTP
WebSockets. This integration allows the application to function effectively even in modern network environments that
primarily support web technologies. TSOCK is not yet released and is expected to become available in late February on my
GitHub.

The user interface of Tresor provides a 2000s-style GUI, focusing on easy navigation.

## Legacy API

The application was initially designed around the following legacy API:

[Steamcoin API Documentation](http://wiki.befatorinc.de/index.php/Steamcoin_API_Documentation)

This project will implement the new API as soon as it gets released. I have no authority over the API project and can't
provide information apart from the website.

## Tresor Display Windows

**removed in current version**

## Building and Running the Tresor Server

To build and run the current version of the Tresor Server, follow these steps:

1. **Install Required Software**:
    - **Java Development Kit (JDK)**: Ensure you have JDK 21 or later installed. You can download it
      from [Adoptium](https://adoptium.net/de/temurin/releases/) or [Oracle](https://www.oracle.com/java/technologies/downloads/).
    - **Maven**: Apache Maven is required for building the project. Install it
      from [Maven Download](https://maven.apache.org/download.cgi) and follow
      the [installation guide](https://maven.apache.org/install.html).
    - **Git**: Install Git from [Git SCM](https://git-scm.com/downloads) and ensure it's correctly configured.
    - **Telnet Client**: On Windows, install the Telnet client via the Control Panel. On Linux, use:
        - Debian/Ubuntu:
          ```bash
          sudo apt install telnet
          ```
        - Fedora:
          ```bash
            sudo dnf install telnet
            ```
        - NIX (nix-shell):
          ```bash
            nix-shell -p inetutils
          ```

2. **Clone the repository**:
   ```bash
   git clone https://github.com/TureBentzin/tresor/
   ```

3. **Build the application with Maven**:
   ```bash
   mvn clean install
   ```
   **Important**: Ensure that Git is correctly configured, as the Maven Git plugin requires it. If not configured, the
   build process will fail.

    - Set your Git username and email:
      ```bash
      git config --global user.name "Your Name"
      git config --global user.email "you@example.com"
      ```

   The Git configuration details are compiled into the version information and will be displayed to the user.

4. **Run the server**:
   ```bash
   java -jar target/Tresor-<version>.jar
   ```

5. **Connect clients**:
   Use any Telnet client to connect to the server:
   ```bash
   telnet <host> 23
   ```

6. **Exit the GUI**:
   Navigate to "Settings" -> "Quit" -> "Yes" to close the application safely.

## Using the Nix Flake

This repo provides a nix flake (flake.nix), but because of the way the project is structured, it is not possible to
build the project using the flake right now.
Im actively working on a solution to this problem, but for now, you have to use the manual build instructions above.
Alternatively, you can use the nix-shell command to get a shell with all the required dependencies.

### Refactoring

The Project was refactored and restructured on 21.02.2025.
The Nix flakes are now even less functional than before...

```bash
nix-shell -p jdk23 maven git
```

If you are familiar with nix, I would appreciate any help with this issue.
Feel free to open a pull request if you have a solution.
For this reason i will keep the `.nix` files in the repository for now.

## IntellJ IDEA

The project is developed with IntelliJ IDEA, but the `.idea` folder is not included in the repository.
I provide a run configuration for the server in the `.run` folder.
You can import this configuration into your IntelliJ IDEA to build and run the server from the IDE.

## Translations

Tresor uses "langfiles" in the `src/main/resources/langfiles` directory to provide translations.
The files are in the format `langfile_<language id>.json`.
The default language is English (en), and the application will fall back to English if a translation is missing.

### Translating Tresor

Tresor is heavily work in progress, and the translations (with their keys, params, etc.) are subject to change.
For testing purposes, I have included a German translation file (`langfile_de.json`).
I may update the german translation file as I develop the application further, but i cant guarantee that it will always
be up to date.

## Themes

Tresor supports themes, which are located in the `src/main/resources/themes` directory.
The default theme is currently set to `blaster.properties`.

Themes in the correct folder and format are loaded automatically when the application starts.

### Creating Themes

To create a new theme, create a new `.properties` file in the `src/main/resources/themes` directory.
The filename will be the name of the theme (excluding the `.properties` extension).

I suggest copying an existing theme file and modifying it to create a new theme.
