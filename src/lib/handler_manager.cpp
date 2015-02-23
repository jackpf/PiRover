#include "handler_manager.hpp"

HandlerManager::HandlerManager(int count, ...)
{
    handlers = (Handler **) malloc(count * sizeof(Handler));

    va_list arguments;
    va_start(arguments, count);

    for (int i = 0; i < count; i++) {
        handlers[i] = va_arg(arguments, Handler *);
    }

    va_end(arguments);

    this->count = count;
}

HandlerManager::~HandlerManager()
{
    for (int i = 0; i < count; i++) {
        delete(handlers[i]);
    }

    free(handlers);
}

Handler *HandlerManager::get(int id)
{
    if (id > count - 1) {
        return NULL;
    }

    return handlers[id];
}

void HandlerManager::cleanup()
{
    for (int i = 0; i < count; i++) {
        handlers[i]->cleanup();
    }
}