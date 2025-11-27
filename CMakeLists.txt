# Versión mínima de CMake requerida
cmake_minimum_required(VERSION 3.10)

# Nombre del proyecto
project(MiProyectoRaylib C CXX)

# Opciones de compilación
# Habilita advertencias y la opción de C++11 (o la que prefieras, C++14/17/20)
set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED True)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Directorios del proyecto
set(SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/src)
set(INCLUDE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/include)
set(RESOURCES_DIR ${CMAKE_CURRENT_SOURCE_DIR}/resources)

# ==============================================================================
# Búsqueda y configuración de Raylib (MÉTODO MANUAL)
# ==============================================================================

# 1. Ruta a la carpeta que contiene los archivos de cabecera (.h) de raylib
# ¡Ahora apunta a la carpeta 'include' dentro de w64devkit!
set(RAYLIB_INCLUDE_DIR "C:/Users/Usuario/w64devkit/include")

# 2. Ruta a la carpeta que contiene el archivo de biblioteca (.a o .lib) de raylib
# ¡Ahora apunta a la carpeta 'lib' dentro de w64devkit!
set(RAYLIB_LIB_DIR "C:/Users/Usuario/w64devkit/lib")

# Nombre del archivo de biblioteca de Raylib (usualmente 'raylib' para MinGW/GCC)
set(RAYLIB_LIBRARIES raylib)

# Incluye el directorio 'include' de tu proyecto para las cabeceras
include_directories(${INCLUDE_DIR})

# Agrega la ruta de inclusión de Raylib
include_directories(${RAYLIB_INCLUDE_DIR})

# Agrega la ruta de la biblioteca para el enlazador
link_directories(${RAYLIB_LIB_DIR})

# ==============================================================================
# Archivos fuente y ejecutable (sin cambios)
# ==============================================================================

# Recoge todos los archivos .c en el directorio src
file(GLOB SOURCE_FILES "${SOURCE_DIR}/*.c")

# Define el nombre del ejecutable
set(EXECUTABLE_NAME "juego")

# Crea el ejecutable a partir de los archivos fuente
add_executable(${EXECUTABLE_NAME} ${SOURCE_FILES})

# Vincula las bibliotecas de Raylib (y las dependencias necesarias para MinGW/GCC)
target_link_libraries(${EXECUTABLE_NAME}
    PUBLIC
    ${RAYLIB_LIBRARIES}
    mingw32
    winmm
    gdi32
    # Otras librerías necesarias para GCC/MinGW, si no están incluidas en raylib
    # opengl32
    # kernel32
)

# ==============================================================================
# Copia de Recursos (IMPORTANTE: Raylib necesita los recursos en tiempo de ejecución)
# ==============================================================================

# Define una carpeta para el destino de la compilación/ejecución
# Esto se ajusta a lo que CMake llama la carpeta donde se genera el ejecutable (CMAKE_RUNTIME_OUTPUT_DIRECTORY)
set(RUNTIME_OUTPUT_DIR ${CMAKE_BINARY_DIR}) # Opcional: define dónde se crearán los binarios

# Comando para copiar la carpeta 'resources' al directorio de ejecución/binarios
# Esto es vital para que Raylib pueda encontrar los archivos (imágenes, sonidos, etc.) en tiempo de ejecución.
add_custom_command(
    TARGET ${EXECUTABLE_NAME} POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_directory
    "${RESOURCES_DIR}"
    "$<TARGET_FILE_DIR:${EXECUTABLE_NAME}>/resources"
)

# Nota: El comando anterior copia la carpeta "resources" *dentro* de la carpeta de salida del ejecutable.
# La ruta de los recursos en tu código fuente C/C++ debería ser relativa a la carpeta de ejecución:
# EJEMPLO: LoadTexture("resources/fondo.png");