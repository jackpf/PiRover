#ifndef H_HANDLER_MANAGER
#define H_HANDLER_MANAGER

#include <stdarg.h>
#include "handler.hpp"
#include "controllers/rover_controller.hpp"
#include "controllers/launcher_controller.hpp"
#include "controllers/shutdown_controller.hpp"
#include "controllers/sensor_controller.hpp"
#include "controllers/camera_controller.hpp"

class HandlerManager
{
private:
    int count;
    Handler **handlers;

public:
    HandlerManager(int count, ...);
    ~HandlerManager();
    Handler *get(int id);
    void cleanup();
};

#endif