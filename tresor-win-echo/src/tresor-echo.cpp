#include <websocketpp/config/asio_no_tls.hpp>
#include <websocketpp/server.hpp>
#include <iostream>
#include <string>

#include <type_traits>


typedef websocketpp::server<websocketpp::config::asio> server;

template<typename ConnectionHandle>
void on_message(server *s, ConnectionHandle hdl, const server::message_ptr &msg) {
    std::string received = msg->get_payload();
    std::cout << "Received: " << received << std::endl;
    //std::stringstream ss;
    //ss << R"({"content": ")" << received << "\"}";

    s->send(std::move(hdl), recieved, msg->get_opcode());
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        std::cerr << "Please care about the usage: " << argv[0] << " <port>" << std::endl;
        return 1;
    }

    int port = std::stol(argv[1]);
    server echo_server;

    if (port < 1 || port > 65535) {
        std::cerr << "Please care about port bound" << std::endl;
        return 1;
    }

    try {
        echo_server.init_asio();

        echo_server.set_message_handler(
                std::bind(&on_message<websocketpp::connection_hdl>, &echo_server, std::placeholders::_1,
                          std::placeholders::_2)); //chatgpt...

        echo_server.listen(port);
        echo_server.start_accept();
        std::cout << "Echo server listening on ws://localhost:" << port << std::endl;
        echo_server.run();

    } catch (const std::exception &e) {
        std::cerr << "Exception: " << e.what() << std::endl;
    }

    return 0;
}
