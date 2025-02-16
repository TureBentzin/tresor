cmake_minimum_required(VERSION 3.16)
project(TresorDisplay LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

include(FetchContent)

# Fetch websocketpp
FetchContent_Declare(
        websocketpp
        GIT_REPOSITORY https://github.com/zaphoyd/websocketpp.git
        GIT_TAG 0.8.2 # Use a known stable release
)
FetchContent_MakeAvailable(websocketpp)

# Fetch standalone asio
FetchContent_Declare(
        asio
        GIT_REPOSITORY https://github.com/chriskohlhoff/asio.git
        GIT_TAG asio-1-22-1
)
FetchContent_MakeAvailable(asio)

# Fetch nlohmann/json
FetchContent_Declare(
        json
        GIT_REPOSITORY https://github.com/nlohmann/json.git
        GIT_TAG v3.11.3
)
FetchContent_MakeAvailable(json)

add_executable(TresorDisplay src/tresor-display.cpp)

target_include_directories(TresorDisplay PRIVATE
        ${websocketpp_SOURCE_DIR}
        ${asio_SOURCE_DIR}/asio/include
        ${json_SOURCE_DIR}/single_include
)

if (WIN32)
    target_link_libraries(TresorDisplay ws2_32)
endif()

if (MSVC)
    target_compile_options(TresorDisplay PRIVATE /W4)
else()
    target_compile_options(TresorDisplay PRIVATE -Wall -Wextra -pedantic)
endif()
