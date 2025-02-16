#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>
#include <iostream>
#include <conio.h>
#include <thread>
#include <string>
#include <nlohmann/json.hpp>

using json = nlohmann::json;
typedef websocketpp::client<websocketpp::config::asio_client> client;

websocketpp::connection_hdl global_hdl;
client ws_client;

void on_message(websocketpp::connection_hdl hdl, client::message_ptr msg) {
    system("cls");
    try {
        auto data = json::parse(msg->get_payload());
        if (data.contains("content")) {
            std::cout << data["content"].get<std::string>() << std::endl;
        }
    } catch (const std::exception &e) {
        std::cerr << "Failed to parse message: " << e.what() << std::endl;
    }
}


void key_input_loop() {
    while (true) {
        int key = _getch();
        json msg;
        msg["type"] = "input";
        msg["key"] = std::string(1, (char)key);

        std::string payload = msg.dump();
        ws_client.send(global_hdl, payload, websocketpp::frame::opcode::text);
    }
}

int main(int argc, char* argv[]) {
    if (argc != 2) {
        std::cerr << "Usage: " << argv[0] << " <websocket_url>" << std::endl;
        return 1;
    }

    std::string ws_url = argv[1];

    // Initialize client
    ws_client.init_asio();

    // Register message handler
    ws_client.set_message_handler(&on_message);

    websocketpp::lib::error_code ec;
    client::connection_ptr con = ws_client.get_connection(ws_url, ec);

    if (ec) {
        std::cerr << "Could not connect to: " << ws_url << " Error: " << ec.message() << std::endl;
        return 1;
    }

    global_hdl = con->get_handle();

    ws_client.connect(con);


    std::thread input_thread(key_input_loop);
    ws_client.run();

    input_thread.join();

    return 0;
}
