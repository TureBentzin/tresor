cmake_minimum_required(VERSION 3.16)
project(TresorEcho LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

include(FetchContent)

FetchContent_Declare(
        websocketpp
        GIT_REPOSITORY https://github.com/zaphoyd/websocketpp.git
        GIT_TAG 0.8.2
)
FetchContent_MakeAvailable(websocketpp)

FetchContent_Declare(
        asio
        GIT_REPOSITORY https://github.com/chriskohlhoff/asio.git
        GIT_TAG asio-1-22-1
)
FetchContent_MakeAvailable(asio)

add_executable(TresorEcho src/tresor-echo.cpp)

target_include_directories(TresorEcho PRIVATE
        ${websocketpp_SOURCE_DIR}
        ${asio_SOURCE_DIR}/asio/include
)

if (WIN32)
    target_link_libraries(TresorEcho ws2_32)
endif()

if (MSVC)
    target_compile_options(TresorEcho PRIVATE /W4 /DASIO_STANDALONE /D_WEBSOCKETPP_NO_BOOST_ /D_WEBSOCKETPP_CPP11_STL_)
else()
    # not really tested or supported. Good Luck!
    target_compile_options(TresorEcho PRIVATE -Wall -Wextra -pedantic -DASIO_STANDALONE -D_WEBSOCKETPP_NO_BOOST_ -D_WEBSOCKETPP_CPP11_STL_)
endif()
