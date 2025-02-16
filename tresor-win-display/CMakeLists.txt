cmake_minimum_required(VERSION 3.16)
project(TresorDisplay LANGUAGES CXX)

# Set C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Fetch dependencies
include(FetchContent)

# Fetch websocketpp
FetchContent_Declare(
    websocketpp
    GIT_REPOSITORY https://github.com/zaphoyd/websocketpp.git
    GIT_TAG master
)
FetchContent_MakeAvailable(websocketpp)

# Fetch nlohmann/json
FetchContent_Declare(
    json
    GIT_REPOSITORY https://github.com/nlohmann/json.git
    GIT_TAG v3.11.3
)
FetchContent_MakeAvailable(json)

# Define the executable
add_executable(TresorDisplay tresor-display.cpp)

# Include directories
target_include_directories(TresorDisplay PRIVATE
    ${websocketpp_SOURCE_DIR}
    ${json_SOURCE_DIR}/single_include
)

# Link libraries
if (WIN32)
    target_link_libraries(TresorDisplay ws2_32)
endif()

# Enable higher warning levels for better code quality
if (MSVC)
    target_compile_options(TresorDisplay PRIVATE /W4)
else()
    target_compile_options(TresorDisplay PRIVATE -Wall -Wextra -pedantic)
endif()
