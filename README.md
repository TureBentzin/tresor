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

[Steamcoin API Documentation](http://befator.befatorinc.de:8932/index.php/Steamcoin_API_Documentation)

This project will implement the new API as soon as it gets released. I have no authority over the API project and can't
provide information apart from the website.

## Tresor Display Windows

The **Tresor Display Windows** are prototype C++ applications designed to connect to a TSOCK server. Their
implementation is experimental and might deviate from the final TSOCK protocol upon its release. These display
applications for Windows are subject to change or removal based on development progress and compatibility with the
finalized TSOCK standard.

## Building and Running the Tresor Server

To build and run the current version of the Tresor Server, follow these steps:

1. **Install Required Software**:
    - **Java Development Kit (JDK)**: Ensure you have JDK 17 or later installed. You can download it
      from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/).
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

3. **Navigate to the server folder**:
   ```bash
   cd tresor-server/Tresor
   ```

4. **Build the application with Maven**:
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

5. **Run the server**:
   ```bash
   java -jar target/Tresor-<version>.jar
   ```

6. **Connect clients**:
   Use any Telnet client to connect to the server:
   ```bash
   telnet <host> 23
   ```

7. **Exit the GUI**:
   Navigate to "Settings" -> "Quit" -> "Yes" to close the application safely.

