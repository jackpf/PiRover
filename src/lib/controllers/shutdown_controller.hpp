#include "handler.hpp"
#include "lib.hpp"

class ShutdownController : public Handler
{
public:
    void handle(char *data);
    void cleanup();
    int getDataSize();
};