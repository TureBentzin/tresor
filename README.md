# Tresor

**Tresor** is a GUI TTY interface for the **Befator Banking API 2**, which is expected to be released in **February 2025**.

## Tresor Server

The **Tresor Server** is a Java application that implements the GUI and allows **Tresor Displays** (or compatible Telnet clients) to connect via a socket and interact with individual windows.

## Idea behind Tresor

The primary idea behind Tresor is to use a Command-Line Interface (CLI) over Telnet to reduce the computational load on LUA-based thin clients. This approach leverages the simplicity and efficiency of Telnet communication to minimize the processing requirements on client devices.

Tresor is designed to work seamlessly with TSOCK, which is a protocol that enables Telnet communication over HTTP WebSockets. This integration allows the application to function effectively even in modern network environments that primarily support web technologies. TSOCK is not yet released and is expected to become available in late February on my GitHub.

The user interface of Tresor follows a 2000s-style GUI, providing a familiar and straightforward visual experience for users familiar with traditional banking interfaces.

## Tresor Display Windows

The **Tresor Display Windows** are prototype C++ applications designed to connect to a TSOCK server. Their implementation is experimental and might deviate from the final TSOCK protocol upon its release. These display applications for Windows are subject to change or removal based on development progress and compatibility with the finalized TSOCK standard.

## Legacy API

The application was initially designed around the following legacy API:

[Steamcoin API Documentation](http://befator.befatorinc.de:8932/index.php/Steamcoin_API_Documentation)

This project will implement the new API as soon as it gets released. I have no authority over the API project and can't provide information apart from the website.

