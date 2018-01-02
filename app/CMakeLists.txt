# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)

#工程路径
set(pathToProject D:/AndroidProjects/MyApplication)
#OpenCV-android-sdk路径
set(pathToOpenCv D:/OpenCV-android-sdk)
#配置加载native依赖
include_directories(${pathToOpenCv}/sdk/native/jni/include/)
include_directories(src/main/cpp/)
find_library( log-lib log )

#动态方式加载
add_library( lib_opencv SHARED IMPORTED )
#引入libopencv_java3.so文件
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${pathToProject}/app/libs/${ANDROID_ABI}/libopencv_java3.so)

add_library( native-lib SHARED src/main/cpp/native-lib.cpp)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

#target_link_libraries( # Specifies the target library.
#                       native-lib
#                       # Links the target library to the log library
#                       # included in the NDK.
#                       ${log-lib} )

target_link_libraries(native-lib ${log-lib} lib_opencv)
